/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import account.logic.AccountController;
import account.logic.Plookify_playerController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import player.Track;
import player.TrackPlayer;
import player.TrackPlayerDB;
import radio.logic.FXMLController;
import playlist.gui.PlaylistController;

/**
 * FXML Controller class
 *
 * @author Albert Nguyen
 */
public class MainFrameController implements Initializable {
    
    //Variable Declartions
    private static MediaPlayer mp; //Needed to store the MediaPlayer class
    private static Duration duration;//Needed to use when dealing with temporal things
    private static TrackPlayerDB DT = new TrackPlayerDB(); // Used to access the database for searching songs
    private TrackPlayerDB nowPlayDB = new TrackPlayerDB(); // Used to access the database for current playlist
    private static ArrayList<Track> nowTrackPlaylist; // list of songs to be played
    private static boolean stopRequested = false;//check to see user wants to stop the song
    private static int userID; // ID of the current user
    private static int playlistID; // ID of the current playlist
    private static int currentIndex; // index of the currently playing song in the playlist 
    private static Scene primaryScene;
    private String username;
    
    @FXML 
    private Label user;
    
    @FXML
    private ImageView img,img2;
    
    @FXML
    private Label artText,genText,songText,playtime = new Label(),userText,nowLabel;
    
    @FXML
    private Label radiosub;
    @FXML
    private static Button accButton,playlistButton,backButton,jumpButton,playButton,nextButton,songButton,genreButton,artButton,addButton;
    
    @FXML
    private Button radioButton;
    @FXML
    private TableView<Track> TrackTable,nowPlayTable;
    @FXML
    private Slider volumeSlider,timeSlider;
    @FXML
    private TextField search;
    @FXML
    public TableColumn tNameCol,lengthCol,genreCol,artistCol;
    @FXML
    public TableColumn tNameCol2,lengthCol2,genreCol2,artistCol2;
    
    @FXML
    public ImageView image;
    
    @FXML 
    private Label paymissed;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Image i = new Image("/common/plookifylogowhite.png");
        image.setImage(i);
        
        Image image = new Image("/common/images/play.png");
        img.setImage(image);
        
        Image next = new Image("/common/images/forward.png");
        img2.setImage(next);
        
        tNameCol.setCellValueFactory(new PropertyValueFactory<Track,String>("tName"));
        lengthCol.setCellValueFactory(new PropertyValueFactory<Track,Integer>("length"));
        genreCol.setCellValueFactory(new PropertyValueFactory<Track,String>("genre"));
        artistCol.setCellValueFactory(new PropertyValueFactory<Track,String>("artist"));
        
        tNameCol2.setCellValueFactory(new PropertyValueFactory<Track,String>("tName"));
        lengthCol2.setCellValueFactory(new PropertyValueFactory<Track,Integer>("length"));
        genreCol2.setCellValueFactory(new PropertyValueFactory<Track,String>("genre"));
        artistCol2.setCellValueFactory(new PropertyValueFactory<Track,String>("artist"));
        
        displayNowPlaying();
        updateInfo(); 
    }  
    
    @FXML
    public void displayMissedPayment(String user){
        Database db = new Database();
        Boolean b = db.getMessage(user);
        System.out.println(b);
        if(b){
            paymissed.setVisible(true);
            db.clearMessage(user);
        }
    }
            
    
    @FXML
    public void setUser(String username){
        this.username = username;
        user.setText("Welcome, " + username);
    }
    
   
    protected void updateValues() {
        if (playtime != null && timeSlider != null && volumeSlider != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mp.getCurrentTime();
                    playtime.setText(formatTime(currentTime, duration));
                    timeSlider.setDisable(duration.isUnknown());
                    if (!timeSlider.isDisabled()
                            && duration.greaterThan(Duration.ZERO)
                            && !timeSlider.isValueChanging()) {
                        timeSlider.setValue(currentTime.divide(duration).toMillis()
                                * 100.0);
                    }
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(mp.getVolume()
                                * 100));
                    }
                }
            });
        }
    }
    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }
    public static void setup(final MediaPlayer newMP, ArrayList<Track> songlist,int newIndex, int userID,int newPlaylistID){
        userID = userID;
        mp = newMP;
        nowTrackPlaylist = songlist;
        currentIndex = newIndex;
        playlistID = newPlaylistID;
        
        
    }
    @FXML
    public void playButtonClicked(ActionEvent e){
      Image ima = new Image("/common/images/play.png");
      img.setImage(ima);  
      MediaPlayer.Status status = mp.getStatus();
                if (status == MediaPlayer.Status.UNKNOWN || status == MediaPlayer.Status.HALTED) {
                    // don't do anything in these states
                    return;
                }

                if (status == MediaPlayer.Status.PAUSED
                        || status == MediaPlayer.Status.READY
                        || status == MediaPlayer.Status.STOPPED) {
                    mp.play();
                    Image im = new Image("/common/images/pause.png");
                    img.setImage(im);
                } else {
                    mp.pause();
                    
                }  
    }
    @FXML
    public void nextButtonClicked(ActionEvent e){
        mp.stop();
        TrackPlayer.nextSong();
        mp.play();
        updateInfo();
    }
    @FXML
    public void accButtonClicked(ActionEvent e) throws ParseException, SQLException, IOException{
        ((Node)e.getSource()).getScene().getWindow().hide();
        //accButton.setDisable(true);
        //System.out.println(DT.getUserName(userID) + " payy");
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        BorderPane root = loader.load(Plookify_playerController.class.getClassLoader().getResource("account/gui/account.fxml").openStream());
        AccountController ppController = (AccountController)loader.getController();
        ppController.paymentDays(username);
        ppController.setAccInfo(username);
        ppController.addData(username);
        Pane sliderPane = new Pane();
        
        mediaBarController.setup(TrackPlayer.getMP(),TrackPlayer.getNowPlay(), TrackPlayer.getCurrentIndex(), TrackPlayer.getPlayID());
            
        FXMLLoader radioLoader = new FXMLLoader(getClass().getResource("/common/slider.fxml"));
        Pane rPane =radioLoader.load();
        //mediaBarController mController =radioLoader.getController();
        
        
        
        root.getChildren().add(sliderPane);
        ppController.addSliderPane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void playlistButtonClicked(ActionEvent e) throws ParseException, SQLException, IOException{
        ((Node)e.getSource()).getScene().getWindow().hide();
        //accButton.setDisable(true);
        //System.out.println(DT.getUserName(userID) + " payy");
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        BorderPane root = loader.load(Plookify_playerController.class.getClassLoader().getResource("playlist/gui/Playlist.fxml").openStream());
        PlaylistController pController = (PlaylistController)loader.getController();
        pController.setUser(username);
        
        Pane sliderPane = new Pane();
        
        mediaBarController.setup(TrackPlayer.getMP(),TrackPlayer.getNowPlay(), TrackPlayer.getCurrentIndex(), TrackPlayer.getPlayID());
        PlaylistController.setUserID(userID);    
        FXMLLoader radioLoader = new FXMLLoader(getClass().getResource("/common/slider.fxml"));
        Pane rPane =radioLoader.load();
        //mediaBarController mController =radioLoader.getController();
        
        
        
        root.getChildren().add(sliderPane);
        pController.addSliderPane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void radioButtonClicked(ActionEvent e){
        try{
            boolean s = false;
            Database db = new Database();
            s = db.checkSub(username);
            System.out.println(s + " username : " + username);
            //System.out.println("SELECT * FROM CUSTOMER WHERE USERNAME =" + "'" + username + "'");

            if(s)
            {
                ((Node)e.getSource()).getScene().getWindow().hide();
        //accButton.setDisable(true);
        //System.out.println(DT.getUserName(userID) + " payy");
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                BorderPane root = loader.load(Plookify_playerController.class.getClassLoader().getResource("radio/GUI/FXML.fxml").openStream());
                FXMLController rController = (FXMLController)loader.getController();
                rController.setUser(username);
                Pane sliderPane = new Pane();
        
                mediaBarController.setup(TrackPlayer.getMP(),TrackPlayer.getNowPlay(), TrackPlayer.getCurrentIndex(), TrackPlayer.getPlayID());

                FXMLLoader radioLoader = new FXMLLoader(getClass().getResource("/common/slider.fxml"));
                Pane rPane =radioLoader.load();
                //mediaBarController mController =radioLoader.getController();



                root.getChildren().add(sliderPane);
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
            else{
                radioButton.setDisable(true);
                radiosub.setVisible(true);
            }
        }catch(Exception ex){
            System.err.println(ex + "error");
        }
    }
    @FXML
    public void backButtonClicked(ActionEvent e){
        try{
            mp.stop();
            Parent sub = FXMLLoader.load(getClass().getResource("/account/gui/Plookify.fxml"));
            Scene sub_scene = new Scene(sub);
            Stage sub_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            sub_stage.setScene(sub_scene);
            sub_stage.show();
        }
        catch(IOException ex){
            ex.printStackTrace();
            System.out.println("Error on moving to account stuff");  
        }
    }    
    @FXML
    public void jumpButtonClicked(ActionEvent e){
        String time = JOptionPane.showInputDialog("Jump to what time? Format:(m ss) ");
        try{
            String[] timesplit = time.split(" ");
            int minute = Integer.parseInt(timesplit[0]);

            double second = Integer.parseInt(timesplit[1]) + (minute * 60);
            if(Duration.seconds(second).greaterThan(mp.getMedia().getDuration())){
                JOptionPane.showMessageDialog(null,"Time is greater than length of song");
            }
            else{
                mp.stop();
                mp.setStartTime(Duration.seconds(second));
                mp.play();
            }
        }
        catch(Exception ex ){
            JOptionPane.showMessageDialog(null,"Incorrect format");
        }
    }
    public void updateInfo(){
        Image playButtonImage = new Image("/common/images/play.png");
        Image pauseButtonImage = new Image("/common/images/pause.png");
        Image nextButtonImage = new Image("/common/images/forward.png");
        
        ImageView iv = new ImageView();
        iv.setImage(playButtonImage);
        
        ImageView iv2 = new ImageView();
        iv2.setImage(nextButtonImage);
        artText.setText("Artist: " + nowTrackPlaylist.get(currentIndex).getArtist());
        genText.setText("Genre: " + nowTrackPlaylist.get(currentIndex).getGenre());
        songText.setText("Song Name: " + nowTrackPlaylist.get(currentIndex).getTName());
        mp.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                updateValues();
            }
        });
        mp.setOnPlaying(new Runnable() {
            public void run() {
                if (stopRequested) {
                    mp.pause();
                    stopRequested = false;
                } else {
                   // iv.setImage(pauseButtonImage);
                 //   playButton.setGraphic(iv);
                }
            }
        });
        mp.setOnPaused(new Runnable() {
            public void run() {
              //  iv.setImage(playButtonImage);
                //playButton.setGraphic(iv);
            }
        });
        mp.setOnReady(new Runnable() {
            public void run() {
                duration = mp.getMedia().getDuration();
                updateValues();
            }
        });
        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                }
            }
        });
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volumeSlider.isValueChanging()) {
                    mp.setVolume(volumeSlider.getValue() / 100.0);
                }
            }
        });
        mp.setOnEndOfMedia(new Runnable() {
            //When a song emds
            @Override
            public void run() {
                mp.stop();
                
                TrackPlayer.nextSong();
                updateInfo();
                
            }
        });
        
    }
    public void displayNowPlaying(){
        
        String playlistName = DT.getPlaylistName(playlistID);
        nowLabel.setText("Now Playing...      '" + playlistName +"'");
        
        ObservableList<Track> playlistTracks = FXCollections.observableArrayList();
        nowPlayDB.setRowData(nowTrackPlaylist);
        playlistTracks = nowPlayDB.getRows();
        nowPlayTable.setItems(playlistTracks);
        nowPlayTable.setOnMousePressed(new EventHandler<MouseEvent>(){
        @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Track selected =  nowPlayTable.getSelectionModel().getSelectedItem();                   
                    TrackPlayer.removeSong(selected);
                    userText.setText("Last removed '" + selected.getTName() + "' from now playing");
                    displayNowPlaying();
                    
                }
            }
        
        });
    }
    @FXML
    public void songButton(){
        ObservableList<Track> searchTracks = FXCollections.observableArrayList();
        DT.searchDB("TRACK","trackName",search.getText());
        searchTracks = DT.getRows();
        TrackTable.setItems(searchTracks);
        if (search.getText().equals("")){
            userText.setText("Nothing searched, displaying all songs");
        }
        else{
            userText.setText("Found " + TrackTable.getItems().size() + " songs containing : " + search.getText());
        }
        TrackTable.setOnMousePressed(new EventHandler<MouseEvent>(){
        @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Track selected =  TrackTable.getSelectionModel().getSelectedItem();                   
                    TrackPlayer.addSong(selected);
                    userText.setText("Song: " + selected.getTName() + " added to Now Playing.");
                    displayNowPlaying();
                }
            }
        });
        TrackTable.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    Track selected =  TrackTable.getSelectionModel().getSelectedItem();                   
                    TrackPlayer.addSong(selected);
                    userText.setText("Song: " + selected.getTName() + " added to Now Playing.");
                    displayNowPlaying();
                }

            }

        });
    }
    @FXML
    public void genreButton(){
        ObservableList<Track> searchTracks = FXCollections.observableArrayList();
        String ID = DT.getID(search.getText(), "Genre");
        DT.searchDB("TRACK","genreID",ID);
        searchTracks = DT.getRows();
        TrackTable.setItems(searchTracks);
        if (search.getText().equals("")){
            userText.setText("Nothing searched, displaying all songs");
        }
        else{
            userText.setText("Found " + TrackTable.getItems().size() + " songs in genre: " + search.getText());
        }
        TrackTable.setOnMousePressed(new EventHandler<MouseEvent>(){
        @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Track selected =  TrackTable.getSelectionModel().getSelectedItem();                   
                    TrackPlayer.addSong(selected);
                    userText.setText("Song: " + selected.getTName() + " added to Now Playing.");
                    displayNowPlaying();
                }
            }
        });
        TrackTable.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    Track selected =  TrackTable.getSelectionModel().getSelectedItem();                   
                    TrackPlayer.addSong(selected);
                    userText.setText("Song: " + selected.getTName() + " added to Now Playing.");
                    displayNowPlaying();
                }

            }

        });
    }
    @FXML
    public void artButton(){
        ObservableList<Track> searchTracks = FXCollections.observableArrayList();
        String ID = DT.getID(search.getText(), "Artist");
        DT.searchDB("TRACK","artistID",ID);
        searchTracks = DT.getRows();
        TrackTable.setItems(searchTracks);
        if (search.getText().equals("")){
            userText.setText("Nothing searched, displaying all songs");
        }
        else{
            userText.setText("Listing songs with artist (and a few others): " + search.getText());
        }
        TrackTable.setOnMousePressed(new EventHandler<MouseEvent>(){
        @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Track selected =  TrackTable.getSelectionModel().getSelectedItem();                   
                    TrackPlayer.addSong(selected);
                    userText.setText("Song: " + selected.getTName() + " added to Now Playing.");
                    displayNowPlaying();
                }
            }
        });
        TrackTable.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    Track selected =  TrackTable.getSelectionModel().getSelectedItem();                   
                    TrackPlayer.addSong(selected);
                    userText.setText("Song: " + selected.getTName() + " added to Now Playing.");
                    displayNowPlaying();
                }

            }

        });
    }
    @FXML
    public void addButton(){
        userText.setText("Added all songs in table to 'Now Playing' playlist");
        ObservableList<Track> selected =  DT.getRows();
        for(Track track: selected){
            TrackPlayer.addSong(track);
        }
        displayNowPlaying();
    }
    public static Scene getScene(){
        //primaryScene = DT.getScene();
        return primaryScene;
    }
    
    
}
