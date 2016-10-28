package playlist.gui;

import player.TrackPlayer;
import common.MainFrameController;
import common.mediaBarController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlaylistController implements Initializable {
    @FXML
    public TextField searchField;
    @FXML
    public TextArea textArea;
    @FXML
    public TableView<data> table;
    @FXML
    public TableColumn tracknameColumn;
    @FXML
    public TableColumn artistnameColumn;
    @FXML
    public TableColumn tracklengthColumn;
    @FXML
    public TableColumn genreColumn;
    @FXML
    
    public Button saveButton;
    @FXML
    public TextField playlistName;
    @FXML
    public RadioButton friendButton;
    @FXML
    public RadioButton privateButton;
    @FXML
    public TableView<playlist> tableview;
    @FXML
    public TableColumn playlistnameColumn;
    
    @FXML
    public TableView<data> view;
    @FXML
    public TableColumn tnColumn;
    @FXML
    public TableColumn anColumn;
    @FXML
    public TableColumn tlColumn;
    @FXML
    public TableColumn gColumn;
    
    @FXML
    public MenuItem closeButton;
    
    @FXML
    public Button saveButton1;
    @FXML 
    public TextField nameField;
    @FXML
    private Button changeButton;
    @FXML
    public Button addSongButton;
    @FXML
    public Button removeSongButton;
            
    public ObservableList<data> data = FXCollections.observableArrayList();
    public ObservableList<playlist> playlist = FXCollections.observableArrayList();
    public ObservableList<data> track = FXCollections.observableArrayList();
            
    public static int choice;
    public static int playlistID;
    public static String playlistType;
    public static int trackID;
    private static int userID;
    private String username;
    
    
    @FXML
    private mediaBarController mController;
    
    @FXML
    private Pane sliderPane;
    
    Connection con = null;
    
    @FXML
    public void setUser(String username){
        this.username = username;
        //user.setText("Welcome, " + username);
    }
    
    public void searchClicked() {
        String artistName = searchField.getText();
        String Aid = getID(artistName,"Artist");
        Statement st = null;
        ResultSet rs = null;
        
        try {
             Class.forName("org.sqlite.JDBC");
             con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
             st = con.createStatement();
             String SQL = "SELECT * FROM TRACK WHERE artistID = " + Integer.parseInt(Aid) + ";";
             rs = st.executeQuery(SQL);
             data.clear();
             while(rs.next()) {
                    int tID = rs.getInt(1);
                    String tName = rs.getString(2);
                    String tLength = secToMin(rs.getInt(3));
                    String gName = getTextDB(rs.getInt(4),"Genre");
                    String aName = getTextDB(rs.getInt(5),"Artist");
                    data track = new data(tID,tName,aName,gName,tLength);
                    data.add(track);
             }
             
             table.setEditable(true);
             
             tracknameColumn.setCellValueFactory(new PropertyValueFactory<>("trackName"));
             artistnameColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
             genreColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));
             tracklengthColumn.setCellValueFactory(new PropertyValueFactory<>("trackLength"));
             
             table.setItems(data);
             
             rs.close();
             st.close();
             con.close();
          } catch(ClassNotFoundException | SQLException | NumberFormatException e) {
             System.out.println("Error");
          }   
    }    
    
    public void addSongDB() {
                        playlist selected =  tableview.getSelectionModel().getSelectedItem();
                        playlistID = selected.getPlaylistID();
                        
                        data selected1 =  table.getSelectionModel().getSelectedItem();
                        trackID = selected1.getTrackID();
        try{
            con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            con.setAutoCommit(false);
            String SQL = "INSERT INTO Playlist_Track(playlistID,trackID) " + " VALUES( " + playlistID + "," + trackID + ");";
            con.createStatement().executeUpdate(SQL);
            con.commit();
            con.close();
            display();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on adding song to DB");
        }
    }
    
    public void removeSongDB() {
                        playlist selected =  tableview.getSelectionModel().getSelectedItem();
                        playlistID = selected.getPlaylistID();
                        
                        data selected1 =  view.getSelectionModel().getSelectedItem();
                        trackID = selected1.getTrackID();
        try{
            con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            con.setAutoCommit(false);
            String SQL = "DELETE from Playlist_Track WHERE playlistID = '" + playlistID + "' AND trackID = '" +trackID + "';";
            con.createStatement().executeUpdate(SQL);
            con.commit();
            con.close();
            display();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on removing song to DB");
        }
    }
    
    public void newPlaylist()
    {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewPlaylist.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create a new playlist");
            stage.setScene(new Scene(root1));  
            stage.show();
          } catch (IOException ex) {
            Logger.getLogger(PlaylistController.class.getName()).log(Level.SEVERE, null, ex);
          }
    }

    public void savePlaylist()
    {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        String n = playlistName.getText();
        String choiceStr;
        try{
               Class.forName("org.sqlite.JDBC");
               con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
               con.setAutoCommit(false);
               Statement st = con.createStatement();
               if(choice == 1)
               {
                   choiceStr = "Friend";
               }
               else
               {
                   choiceStr = "Private";
               }
               String sql = "INSERT INTO Playlist (playlistName,playlistType) " + "VALUES ('"+n+"', '"+choiceStr+"');";
               st.executeUpdate(sql); 
               con.commit();
               con.close();
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }  
    
    public void radioSelect()
    {
        if(friendButton.isSelected())
        {
            choice = 1;
        }
        else if(privateButton.isSelected())
        {
            choice = 0;
        }
    }
    
    public void getFriendsPlaylists()
    {
        Statement st;
        ResultSet rs;
        
        try {
             Class.forName("org.sqlite.JDBC");
             con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
             st = con.createStatement();
             Statement st1 = con.createStatement();
             String sql = "SELECT * FROM Playlist WHERE playlistType = 'Friend'";
             String sql1 = "SELECT * FROM customer_playlist;";
             rs = st.executeQuery(sql);
             ResultSet rs1 = st1.executeQuery(sql1);
             
             while(rs1.next())
             {
                 int pID = rs1.getInt(1);
                 System.out.print(pID);
                 int cID = rs1.getInt(2);
                 System.out.println(cID);
             }
             playlist.clear();
             while(rs.next())
             {
                 int pID = rs.getInt(1);
                 String pName = rs.getString(2);
                 String pType = rs.getString(3);
                 playlist p = new playlist(pID,pName,pType);
                 playlist.add(p);
             }
             
             tableview.setEditable(true);
             
             playlistnameColumn.setCellValueFactory(new PropertyValueFactory<>("playlistName"));
             
             tableview.setItems(playlist);
             
             rs.close();
             st.close();
             con.close();
          } catch(ClassNotFoundException | SQLException | NumberFormatException e) {
             System.out.println(""+e);
          }
                tableview.setOnMousePressed((MouseEvent event) -> {
                    if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                        playlist selected =  tableview.getSelectionModel().getSelectedItem();
                        int playlistID = selected.getPlaylistID();
                    
                    Statement st1;
                    ResultSet rs1;
         
                    try {
                        Class.forName("org.sqlite.JDBC");
                        con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
                        st1 = con.createStatement();
                        String SQL = "SELECT * FROM Track JOIN PLaylist_Track ON Playlist_Track.trackID = Track.trackID WHERE playlistID = " + playlistID + ";";
                        rs1 = st1.executeQuery(SQL);
                        track.clear();
                        while(rs1.next())
                        {
                            int tID = rs1.getInt(1);
                            String tName = rs1.getString(2);
                            String tLength = secToMin(rs1.getInt(3));
                            String gName = getTextDB(rs1.getInt(4),"Genre");
                            String aName = getTextDB(rs1.getInt(5),"Artist");
                            data t = new data(tID,tName,aName,gName,tLength);
                            track.add(t);
                        }
                        
                        view.setEditable(true);
             
                        tnColumn.setCellValueFactory(new PropertyValueFactory<>("trackName"));
                        anColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
                        gColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));
                        tlColumn.setCellValueFactory(new PropertyValueFactory<>("trackLength"));

                        view.setItems(track);

                        rs1.close();
                        st1.close();
                        con.close();
                    } catch (ClassNotFoundException | SQLException e) {
                        System.out.print(e);
                    }
                        
                    }
        });
    }
    
    public void getPrivatePlaylists()
    {
        Statement st;
        ResultSet rs;
        
        try {
             Class.forName("org.sqlite.JDBC");
             con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
             st = con.createStatement();
             String sql = "SELECT * FROM Playlist WHERE playlistType = 'Private'";
             rs = st.executeQuery(sql);
             playlist.clear();
             while(rs.next())
             {
                 int pID = rs.getInt(1);
                 String pName = rs.getString(2);
                 String pType = rs.getString(3);
                 playlist p = new playlist(pID,pName,pType);
                 playlist.add(p);
             }
             
             tableview.setEditable(true);
             
             playlistnameColumn.setCellValueFactory(new PropertyValueFactory<>("playlistName"));
             
             tableview.setItems(playlist);
             
             rs.close();
             st.close();
             con.close();
          } catch(ClassNotFoundException | SQLException | NumberFormatException e) {
             System.out.println(""+e);
          }
    }
    
    public String getTextDB(int ID,String table) {
        Connection c;
        String text = "";
        
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            String SQL = "SELECT * FROM " + table + ";";
            ResultSet rs = c.createStatement().executeQuery(SQL);
            while(rs.next()) {
                if(ID == rs.getInt(1)) {
                    text = rs.getString(2);
                }
            }
            c.close();
        } catch(Exception e) {
            System.out.println("Error on finding text");  
        }  
        return text;
    }
    
    public String getID(String text,String table) {
        Connection c;
        //This is a general method to get either the ID of genre or artist
        String ID="";
        
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            String SQL = "SELECT * FROM " + table;
            ResultSet rs = c.createStatement().executeQuery(SQL);
            while(rs.next()) {
                if(text.toLowerCase().equals(rs.getString(2).toLowerCase())) {
                    ID = rs.getString(1);
                }
            }
            c.close();
        }
        catch(Exception e) {
            System.out.println("Error on getting ID");  
        }  
        return ID;
    }
    
    public String secToMin(int seconds) {
        int secs = seconds % 60;
        int mins = (int) Math.floor(seconds / 60);
        if(secs < 10) {
            String newSecs = "0" + secs;
            return mins + ":" + newSecs;
        }
        return mins + ":" + secs;
    }
    
    public void renamePlaylist()
    {
        playlist selected =  tableview.getSelectionModel().getSelectedItem();
        playlistID = selected.getPlaylistID();
        playlistType = selected.getPlaylistType();
        
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewPlaylistName.fxml"));
            Parent root2 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Rename the playlist");
            stage.setScene(new Scene(root2));  
            stage.show();
          } catch (IOException ex) {
            Logger.getLogger(PlaylistController.class.getName()).log(Level.SEVERE, null, ex);
          }
    }
    
    public void saveNewName()
    {
        Stage stage = (Stage) saveButton1.getScene().getWindow();
        String name = nameField.getText();
        try{
               Class.forName("org.sqlite.JDBC");
               con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
               con.setAutoCommit(false);
               Statement st = con.createStatement();
               String sql = "UPDATE Playlist SET playlistName = '" + name + "' WHERE playlistID = " + playlistID + ";";
               st.executeUpdate(sql);
               con.commit();
               con.close();
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }
    
    public void display() {
                    Statement st1;
                    ResultSet rs1;
         
                    try {
                        Class.forName("org.sqlite.JDBC");
                        con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
                        st1 = con.createStatement();
                        String SQL = "SELECT * FROM Track JOIN PLaylist_Track ON Playlist_Track.trackID = Track.trackID WHERE playlistID = " + playlistID + ";";
                        rs1 = st1.executeQuery(SQL);
                        track.clear();
                        while(rs1.next())
                        {
                            int tID = rs1.getInt(1);
                            String tName = rs1.getString(2);
                            String tLength = secToMin(rs1.getInt(3));
                            String gName = getTextDB(rs1.getInt(4),"Genre");
                            String aName = getTextDB(rs1.getInt(5),"Artist");
                            data t = new data(tID,tName,aName,gName,tLength);
                            track.add(t);
                        }
                        
                        view.setEditable(true);
             
                        tnColumn.setCellValueFactory(new PropertyValueFactory<>("trackName"));
                        anColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
                        gColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));
                        tlColumn.setCellValueFactory(new PropertyValueFactory<>("trackLength"));

                        view.setItems(track);

                        rs1.close();
                        st1.close();
                        con.close();
                    } catch (ClassNotFoundException | SQLException e) {
                        System.out.print(e);
                    }
    }
    
    @FXML 
    public void backButton(ActionEvent event) throws IOException{
        ((Node)event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
           // Database DB = new Database();
          //  int ID = DB.getCustomerID(username);
         //   TrackPlayer tb = new TrackPlayer(ID);
            FXMLLoader loader = new FXMLLoader();
            BorderPane root = loader.load(getClass().getResource("/common/mainFrame.fxml").openStream());
            MainFrameController mfController = (MainFrameController)loader.getController();
            mfController.setUser(username);
            Scene scene = new Scene(root);
            primaryStage.setTitle("Plookify");
            primaryStage.setScene(scene);
            primaryStage.show();
    }
    
    public void addSliderPane(){
        try {
            FXMLLoader radioLoader = new FXMLLoader(getClass().getResource("/common/slider.fxml"));
            Pane rPane =radioLoader.load();
            mController =radioLoader.getController();
            //mControl.setUser(UserID);
            //rPane.relocate(120,0);
            sliderPane.getChildren().add(rPane); 
        } catch (IOException ex) {
        ex.printStackTrace();
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void changeNowPlaying()
    {
        try{
            playlist selected =  tableview.getSelectionModel().getSelectedItem();
            playlistID = selected.getPlaylistID();
            TrackPlayer.changePlaylist(playlistID);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public static void setUserID(int uID) {
        userID = uID;
    }
}