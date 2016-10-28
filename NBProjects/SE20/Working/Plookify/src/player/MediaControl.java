package player;
import account.*;
import account.logic.AccountController;
import account.logic.Plookify_playerController;
import common.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import radio.logic.FXMLController;

/**
 *
 * @author Albert Nguyen
 */
public class MediaControl extends BorderPane {

    //Variable Declartions
    private MediaPlayer mp; //Needed to store the MediaPlayer class
    private HBox mediaBar;//Needed to hold the media stuff, like play buttons or volume slider
    private Duration duration;//Needed to use when dealing with temporal things
    private boolean stopRequested = false;//check to see user wants to stop the song
    private Slider timeSlider;//Used to change the time the song is currently playing at
    private Label playTime;//Used to store the current runtime of the song
    private Slider volumeSlider;//used to change the volume of song
    private VBox featureBar;//used to access other features
    private VBox infoBar;
    private String artist;
    private String genre;
    private String songName;
    private Label empty = new Label();
    private TrackPlayerDB DT = new TrackPlayerDB();
    private TableView<Track> TrackTable;
    private ArrayList<Track> nowTrackPlaylist;
    private AnchorPane rightUpperPane = new AnchorPane();
    private AnchorPane rightLowerPane = new AnchorPane();
    private AnchorPane leftPane = new AnchorPane();
    private TrackPlayerDB nowPlayTable = new TrackPlayerDB();
    private TableView<Track> nowPlayView = new TableView();
    private Label removeLabel = new Label();
    private int userID;
    private static Button accButton = new Button ();
    private static Button radioButton = new Button ();
    private static Button playlistButton = new Button ();
    private static int playlistID;
    private static Scene primaryScene;
   
    //Constructor
    public MediaControl(final MediaPlayer mp, ArrayList<Track> songlist,int currentIndex, int userID,int playlistID) {
        this.userID = userID;
        this.mp = mp;
        this.nowTrackPlaylist = songlist;
        this.artist = songlist.get(currentIndex).getArtist();
        this.genre = songlist.get(currentIndex).getGenre();
        this.songName = songlist.get(currentIndex).getTName();
        this.playlistID = playlistID;
        setStyle("-fx-background-color: #262626");
////Setting up BorderPane details
    //Setting up Feature bar on the right top
        featureBar = new VBox(10);
        
        //accButton.setGraphic(new ImageView(accButtonImage));
        accButton.setText("My Account");
        accButton.setLayoutX(26);
        accButton.setLayoutY(46);
        //accButton.setStyle("-fx-background-color: Black");
        
        accButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
        //accButton.setStyle("-fx-background-color: Black");
        //accButton.setStyle("-fx-body-color: Black");
        });
        accButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
        //accButton.setStyle("-fx-background-color: Black");
        });
        
        accButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try{
                    ((Node)event.getSource()).getScene().getWindow().hide();
                    //accButton.setDisable(true);
                    //System.out.println(DT.getUserName(userID) + " payy");
                    Stage primaryStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    BorderPane root = loader.load(Plookify_playerController.class.getClassLoader().getResource("account/gui/account.fxml").openStream());
                    AccountController ppController = (AccountController)loader.getController();
                    ppController.paymentDays(DT.getUserName(userID));
                    ppController.addData(DT.getUserName(userID));
                    ppController.setAccInfo(DT.getUserName(userID));
                    Scene scene = new Scene(root);
                    //scene.getStylesheets().add(Plookify_playerController.class.getClassLoader().getResource("account/gui/scroll.css").toExternalForm());
                    primaryStage.setScene(scene);
                    primaryStage.show();
                    //mp.stop();
                }
                catch(IOException|ParseException|SQLException e){
                    
                    e.printStackTrace();
                    System.out.println("Error on moving to account stuff");  
                }
            }
        
        });
        
        
        playlistButton.setText("Playlists");
        playlistButton.setPrefWidth(91);
        playlistButton.setLayoutX(26);
        playlistButton.setLayoutY(83);
        //playlistButton.setStyle("-fx-background-color: Black");
        
        playlistButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
        //playlistButton.setStyle("-fx-background-color: Black");
        //playlistButton.setStyle("-fx-body-color: Black");
        });
        playlistButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
        //playlistButton.setStyle("-fx-background-color: Black");
        });
        
        radioButton.setText("Radio");
        radioButton.setPrefWidth(91);
        radioButton.setLayoutX(26);
        radioButton.setLayoutY(159);
        //radioButton.setStyle("-fx-background-color: Black");
        
        
        radioButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
        //radioButton.setStyle("-fx-background-color: Black");
        //radioButton.setStyle("-fx-body-color: Black");
        });
        radioButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
        //radioButton.setStyle("-fx-background-color: Black");
        });
        Label subRadio = new Label("Available on subscription");
        subRadio.setVisible(false);
        subRadio.setLayoutX(40);
        subRadio.setLayoutY(200);
        subRadio.setTextFill(Color.WHITE);
        radioButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try{
                        boolean s = false;
                        Database db = new Database();
                        s = db.checkSub(DT.getUserName(userID));
                        //System.out.println("SELECT * FROM CUSTOMER WHERE USERNAME =" + "'" + username + "'");

                        if(s)
                        {
                            FXMLController.setID(userID);
                            System.out.println("subscribed");
                            ((Node)event.getSource()).getScene().getWindow().hide();
                            Parent p = FXMLLoader.load(getClass().getResource("/radio/GUI/FXML.fxml"));
                            
                            Scene scene = new Scene(p);
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
                        }
                        else{
                            radioButton.setDisable(true);
                            subRadio.setVisible(true);
                        }
                    }catch(Exception ex){
                        System.err.println(ex);
                    }

        }});
        
        featureBar.setAlignment(Pos.BASELINE_LEFT);
        featureBar.getChildren().addAll(accButton,playlistButton,radioButton,subRadio);
//Setting up info part on the bottom right
        infoBar = new VBox();
        
        Button backButton = new Button("Sign Out");
        backButton.setOnAction(new EventHandler<ActionEvent>(){           
            public void handle(ActionEvent e) {
                try{
                    mp.stop();
                    Parent sub = FXMLLoader.load(getClass().getResource("/account/gui/plookify.fxml"));
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
        });
        Label currentText = new Label("Currently Playing:");
        Label artText = new Label("Artist: " + this.artist);
        Label genText = new Label("Genre: " + this.genre);
        Label songText = new Label("Song Name: " + this.songName);
        currentText.setTextFill(Paint.valueOf("WHITE"));
        artText.setTextFill(Paint.valueOf("WHITE"));
        genText.setTextFill(Paint.valueOf("WHITE"));
        songText.setTextFill(Paint.valueOf("WHITE"));
        
        
        
        infoBar.getChildren().addAll(backButton,currentText,songText,genText,artText);

        
        this.setRight(rightLowerPane);
        rightLowerPane.getChildren().addAll(infoBar);
        
        rightLowerPane.setBottomAnchor(infoBar,10.0);
        displayNowPlaying();
//Setting up player and Bottom HBox - Song Control
        mediaBar = new HBox();
        mediaBar.setAlignment(Pos.BOTTOM_CENTER);
        this.setAlignment(mediaBar, Pos.CENTER);
        Image playButtonImage = new Image("/common/images/play.png");
        Image pauseButtonImage = new Image("/common/images/pause.png");
        Image nextButtonImage = new Image("/common/images/forward.png");
        Button playButton = new Button ();
        ImageView iv = new ImageView();
        iv.setFitWidth(41);
        iv.setFitHeight(33);
        iv.setImage(playButtonImage);
        playButton.setGraphic(iv);
        
        playButton.setStyle("-fx-background-color: transparent");
        
        playButton.setOnAction(new EventHandler<ActionEvent>(){           
            public void handle(ActionEvent e) {
                MediaPlayer.Status status = mp.getStatus();
                if (status == MediaPlayer.Status.UNKNOWN || status == MediaPlayer.Status.HALTED) {
                    // don't do anything in these states
                    return;
                }

                if (status == MediaPlayer.Status.PAUSED
                        || status == MediaPlayer.Status.READY
                        || status == MediaPlayer.Status.STOPPED) {
                    mp.play();//This plays
                } else {
                    mp.pause();
                }
            }
        });
        playButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
        //playButton.setStyle("-fx-background-color: Black");
        //playButton.setStyle("-fx-body-color: Black");
        });
        playButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
        //playButton.setStyle("-fx-background-color: Black");
        });
        
        
        Button nextButton = new Button();
        ImageView iv2 = new ImageView();
        iv2.setFitWidth(41);
        iv2.setFitHeight(33);
        iv2.setImage(nextButtonImage);
        nextButton.setGraphic(iv2);
        nextButton.setStyle("-fx-background-color: transparent");
        nextButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
               mp.stop();
               
               TrackPlayer.nextSong();
               
            }
        
        });
        nextButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
        //nextButton.setStyle("-fx-border-color: #262626");
        //nextButton.setStyle("-fx-body-color: Black");
        });
        nextButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
        //nextButton.setStyle("-fx-background-color: Black");
        });
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
                    iv.setImage(pauseButtonImage);
                    playButton.setGraphic(iv);
                }
            }
        });
        mp.setOnPaused(new Runnable() {
            public void run() {
                iv.setImage(playButtonImage);
                playButton.setGraphic(iv);
            }
        });
        mp.setOnReady(new Runnable() {
            public void run() {
                duration = mp.getMedia().getDuration();
                updateValues();
            }
        });
        Label spacer = new Label("   ");
        Label timeLabel = new Label("Time Elapsed: ");
        timeLabel.setTextFill(Paint.valueOf("WHITE"));
        timeSlider = new Slider();
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(50);
        timeSlider.setMaxWidth(Double.MAX_VALUE);
        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                }
            }
        });

        Button jumpButton = new Button("Jump to...");
        jumpButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
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
                catch(Exception e ){
                    JOptionPane.showMessageDialog(null,"Incorrect format");
                }
               
            }
        
        });


// Add Play label
        playTime = new Label();
        //playTime.setPrefWidth(130);
        //playTime.setMinWidth(50);
        
        playTime.setTextFill(Paint.valueOf("WHITE"));
        // Add the volume label
        Label volumeLabel = new Label("Vol: ");
        
        volumeLabel.setTextFill(Paint.valueOf("WHITE"));

        // Add Volume slider
        volumeSlider = new Slider();
        volumeSlider.setPrefWidth(70);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volumeSlider.isValueChanging()) {
                    mp.setVolume(volumeSlider.getValue() / 100.0);
                }
            }
        });
        mp.setOnEndOfMedia(new Runnable() {

            @Override
            public void run() {
                mp.stop();
                
                TrackPlayer.nextSong();
            }
        });
        mediaBar.getChildren().addAll(playButton,nextButton,spacer,timeLabel,timeSlider,playTime,jumpButton,volumeLabel,volumeSlider);

        setBottom(mediaBar);
        
        setSearch();
        
        
        //mp.play();
    }//end of constructor

    protected void updateValues() {
        if (playTime != null && timeSlider != null && volumeSlider != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mp.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
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
    public void displayNowPlaying(){
        
        VBox center = new VBox();
        String playlistName = DT.getPlaylistName(playlistID);
        Label nowLabel = new Label("Now Playing...      '" + playlistName +"'");
        nowLabel.setTextFill(Paint.valueOf("WHITE"));
        nowPlayTable.setRowData(nowTrackPlaylist);
        nowPlayView = nowPlayTable.getView();
        nowPlayView.setOnMousePressed(new EventHandler<MouseEvent>(){
        @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Track selected =  nowPlayView.getSelectionModel().getSelectedItem();                   
                    TrackPlayer.removeSong(selected);
                    removeLabel.setText("Last removed '" + selected.getTName() + "' from now playing");
                    removeLabel.setTextFill(Paint.valueOf("WHITE"));
                    
                    displayNowPlaying();
                    
                }
            }
        
        });
        center.getChildren().addAll(removeLabel,nowLabel,nowPlayView);;
        this.setCenter(center);
        
    }
    public void setSearch()
    {
              
//Setting up GUI to allow search
        ImageView imv = new ImageView();
        imv.setFitWidth(81);
        imv.setFitHeight(73);
        Image image = new Image("/common/images/plookifylogowhite.png");
        imv.setImage(image);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        
        HBox buttonsBox = new HBox(10);
        TextField search = new TextField ();
        search.setPromptText("Search...");
        search.setPrefWidth(400);
        Button addButton = new Button("Add All Songs");
        
        Label instruct = new Label("Double Click the song to add/remove it to now playing!");
        
        HBox songButtonBox = new HBox();
        songButtonBox.setSpacing(10);
        Button songButton = new Button("Songs");
        songButtonBox.getChildren().addAll(addButton);
        empty.setText("Click a button to search for a particular song, genre or artist!");
        songButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                
                grid.getChildren().removeAll(TrackTable,songButtonBox,instruct);
                DT.searchDB("TRACK","trackName",search.getText());
                TrackTable = DT.getView();
                TrackTable.setPrefHeight(250);
                if (search.getText().equals("")){
                    empty.setText("Nothing searched, displaying all songs");
                }
                else{
                    empty.setText("Found " + TrackTable.getItems().size() + " songs containing : " + search.getText());
                }
                TrackTable.setOnMousePressed(new EventHandler<MouseEvent>(){
                @Override
                    public void handle(MouseEvent event) {
                        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                            Track selected =  TrackTable.getSelectionModel().getSelectedItem();                   
                            TrackPlayer.addSong(selected);
                            empty.setText("Song: " + selected.getTName() + " added to Now Playing.");
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
                            empty.setText("Song: " + selected.getTName() + " added to Now Playing.");
                            displayNowPlaying();
                        }

                    }

                });
                
            }
        
        });
        Button genreButton = new Button("Genres");
        
        genreButton.setOnAction(new EventHandler<ActionEvent>(){
        
            @Override
            public void handle(ActionEvent event) {
                
                grid.getChildren().removeAll(TrackTable,songButtonBox,instruct);
                String ID = DT.getID(search.getText(), "Genre");
                DT.searchDB("TRACK","genreID",ID);
                TrackTable = DT.getView();
                TrackTable.setPrefHeight(250);
                grid.add(TrackTable, 0, 2,2,1);
                grid.add(instruct,3,2);
                grid.add(songButtonBox,0,3);
                if (search.getText().equals("")){
                    empty.setText("Nothing searched, displaying all songs");
                }
                else{

                    empty.setText("Found " + TrackTable.getItems().size() + " songs containing in genre: " + search.getText());
                }
                TrackTable.setOnMousePressed(new EventHandler<MouseEvent>(){
                @Override
                    public void handle(MouseEvent event) {
                        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                            Track selected =  TrackTable.getSelectionModel().getSelectedItem();                   
                            TrackPlayer.addSong(selected);
                            empty.setText("Song: " + selected.getTName() + " added to Now Playing.");
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
                            empty.setText("Song: " + selected.getTName() + " added to Now Playing.");
                            displayNowPlaying();
                        }

                    }

                });
                
            }
        
        });
        Button artButton = new Button("Artists");
        artButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                String ID = DT.getID(search.getText(), "Artist");
                DT.searchDB("TRACK","artistID",ID);
                
                
                buttonsBox.getChildren().remove(empty);
                grid.getChildren().removeAll(TrackTable,songButtonBox,instruct);
                
                if(ID.equals("")){
                    empty.setText("Could not find : " + search.getText());  
                    
                }
                else{
                    empty.setText("Found songs containing with artist: " + search.getText());
                }
                buttonsBox.getChildren().add(empty);
                TrackTable = DT.getView();
                TrackTable.setPrefHeight(250);
                grid.add(TrackTable, 0, 2,2,1);
                grid.add(instruct,3,2);
                grid.add(songButtonBox,0,2);
                
                TrackTable.setOnMousePressed(new EventHandler<MouseEvent>(){
                @Override
                    public void handle(MouseEvent event) {
                        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                            Track selected =  TrackTable.getSelectionModel().getSelectedItem();                   
                            TrackPlayer.addSong(selected);
                            empty.setText("Song: " + selected.getTName() + " added to Now Playing.");
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
                            empty.setText("Song: " + selected.getTName() + " added to Now Playing.");
                            displayNowPlaying();
                        }

                    }

                });
            }
        
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                empty.setText("Added all songs in table to 'Now Playing' playlist");
                ObservableList<Track> selected =  DT.getRows();
                for(Track track: selected){
                    TrackPlayer.addSong(track);
                }
                displayNowPlaying();
            }
            
        });
        empty.setTextFill(Paint.valueOf("WHITE"));
        instruct.setTextFill(Paint.valueOf("WHITE"));
        buttonsBox.getChildren().addAll(songButton,genreButton,artButton,empty);
        grid.add(imv,0,0,1,2);
        grid.add(search,1,0);
        grid.add(buttonsBox,1,1);
        
        Label account = new Label();
        account.setTextFill(Paint.valueOf("WHITE"));
        account.setFont(new Font(18.0));
        if(userID == -1){
            account.setText("Welcome, Guest");
        }
        else{
            
            account.setText("Welcome, " + DT.getUserName(userID));
        }
        rightUpperPane.getChildren().addAll(featureBar,account,grid);
        rightUpperPane.setRightAnchor(account,200.0);
        rightUpperPane.setTopAnchor(account,10.0);
        
        rightUpperPane.setRightAnchor(featureBar,20.0);
        
        rightUpperPane.setTopAnchor(featureBar,15.0);
        rightUpperPane.setLeftAnchor(grid,0.0);
        this.setTop(rightUpperPane);
        
    }
    public static void engage(){
        accButton.setDisable(false);
        radioButton.setDisable(false);
        playlistButton.setDisable(false);
    }
    
    public static Scene getPrimaryScene(){
        Scene pScene = accButton.getScene();
        return pScene;
    }
    
}

