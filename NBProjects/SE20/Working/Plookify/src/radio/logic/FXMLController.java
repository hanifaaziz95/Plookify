/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package radio.logic;

import common.Database;
import common.MainFrameController;
import common.mediaBarController;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import player.TrackPlayer;
import player.MediaControl;

/**
 *
 * @author Priyanki Jyotindra Nirmal
 */
public class FXMLController implements Initializable{
    
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
    public TextField playlistName;
    
    @FXML
    public Button cancelButton;
    
    @FXML
    public Button saveButton;
    
    @FXML
    public Button backButton;
    
    @FXML
    public Label text;
    
    @FXML
    public Label alertText;
    
    @FXML
    private Pane sliderPane;
    
    private mediaBarController mController;
    
    @FXML
    public TextArea artists;
    
    //@FXML
    //private ImageView iv;
    
    public ObservableList<ObservableList> data;
    
    static Stage stage1 = new Stage();
    Scene scene1;
    
    String[] array = new String[10];
    
    private static int playlistID;
    private static int[] trackID1 = new int[10];
    
    private String username;
    
    
    Connection con;
    
    Statement st = null;
    Statement st1 = null;
    Statement st2 = null;
    Statement st3 = null;
    Statement st5 = null;
    Statement st6 = null;
    Statement st7 = null;
    Statement st8 = null;
    Statement st9 = null;
    Statement st10 = null;
 
    
    
    ResultSet rs = null;
    ResultSet rs1 = null;
    ResultSet rs2 = null;
    ResultSet rs3 = null;
    ResultSet rs4 = null;
    ResultSet rs5 = null;
    ResultSet rs6 = null;
    ResultSet rs7 = null;
    

    int newid = 0;
    
    
    ArrayList<String> list = new ArrayList<String>();
    private static int ID;
    
    
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //Image im = new Image("/radio/GUI/plookifylogowhite.png");
       //iv.setImage(im);
       displayArtists();
    }
    
    public void addSliderPane(){
        try{
            FXMLLoader radioLoader = new FXMLLoader(getClass().getResource("/common/slider.fxml"));
            Pane rPane =radioLoader.load();
            mController =radioLoader.getController();
            //mControl.setUser(UserID);
            //rPane.relocate(120,0);
            sliderPane.getChildren().add(rPane);
            
    }
        catch (IOException ex) {
        ex.printStackTrace();
        //Alert alert = new Alert(Alert.AlertType.ERROR);
        //alert.setTitle("Error");
        //alert.setHeaderText("");
        //alert.setContentText("Could not load FXML Loader of Overview");
        //alert.showAndWait();
       
    }
    }
    
    public static void setID(int id)
    {
        System.out.println(ID);
        ID=id;
    }
    
    @FXML
    public void setUser(String username){
        this.username = username;
    }
    
    public void displayArtists()
    {
        
        try
          {
             Class.forName("org.sqlite.JDBC");
             con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
             st10 = con.createStatement();
             rs7 = st10.executeQuery("SELECT * FROM Artist;");
             while (rs7.next())
                     {
                         String artistN = rs7.getString("artistName");
                         
                         artists.appendText(artistN + "\n");
                         artists.setWrapText(true);
                         
                     }
            
             st10.close();
             rs7.close();
             con.close();
          }
         
          catch(Exception e)
          {
             System.out.println(""+e);
          }
    }
    
    public void searchClicked()
    {
        String x = searchField.getText();
         alertText.setText("");
        
        try
          {
             Class.forName("org.sqlite.JDBC");
             con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
             st = con.createStatement();
             st1 = con.createStatement();
             st2 = con.createStatement();
             st3 = con.createStatement();
             st5 = con.createStatement();
             st6 = con.createStatement();
             
             rs = st.executeQuery("SELECT * FROM Artist;");
             rs1 = st1.executeQuery("SELECT * FROM Track;");
             rs2 = st2.executeQuery("SELECT * FROM Track;");

             int genreid=0;
             String artistName = "";
            boolean check = false;
             while(rs.next())
             {
                 
                 String name = rs.getString("artistName");
                 
                 if(name.equalsIgnoreCase(x))
                 {
                     System.out.println(check);
                     check = true;
                     int id = rs.getInt("artistID");
                     
                     while(rs1.next())
                     {
                         int artistid = rs1.getInt("artistID");
                        
                         if(artistid == id)
                         {
                             genreid = rs1.getInt("genreID"); 
                             break;
                         }
                         
                     }
                 }
                
                 
             }
             if (check==false)
             {
                 alertText.setText("No such artist!");
             } 
             
             
             while(rs2.next())
             {
                 int artID = rs2.getInt("artistID");
                 rs3 = st3.executeQuery("SELECT artistName FROM Artist WHERE artistID='" + artID + "'");
                 
             
                 int tempGenreId = rs2.getInt("genreID");
                 
                 
                 if (tempGenreId == genreid)
                 {
                     String trackName = rs2.getString("trackName");
                     int trackID = rs2.getInt("trackID");
                     artistName = rs3.getString("artistName");
                     rs4 = st5.executeQuery("SELECT genreName FROM Genre WHERE genreID='" + genreid + "'");
                     String genre = rs4.getString("genreName");
                     
                     rs5 = st6.executeQuery("SELECT trackLength FROM Track WHERE trackID='" + trackID + "'");
                     int trackLength = rs5.getInt("trackLength");
                     String tLength = String.valueOf(trackLength);
                     
                    
                   
                    list.add(trackName + "," + artistName + "," + genre + "," + tLength + "," + trackID);
                    
                 }
             }
              
             list = getRandom(list);
                
             table.setEditable(true);
             
             tracknameColumn.setCellValueFactory(new PropertyValueFactory<>("trackName"));
             artistnameColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
             genreColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));
             tracklengthColumn.setCellValueFactory(new PropertyValueFactory<>("trackLength"));
              
                
             ObservableList<data> dataSet =FXCollections.observableArrayList();
               
             for (int i=0; i<list.size(); i++)
             {
                
                String[] split = list.get(i).split(",");
                
                 String a = split[0];
                 String b = split[1];
                 String c = split[2];
                 String d = split[3];
                 dataSet.add(new data( a, b, c, d));
             }
           
             table.setItems(dataSet);
             trackID1 = makeArray(list);  
             
            
             
             rs.close();
             rs1.close();
             rs2.close();
             rs3.close();
             rs4.close();
             rs5.close();
             
             st.close();
             st1.close();
             st2.close();
             st3.close(); 
             st5.close();
             st6.close();
            
             con.close();
             
              
          }
       
          catch(Exception e)
          {
             System.out.println(""+e);
          }
        
        
    }
    
    public int[] makeArray(ArrayList<String> array)
    {
        String track[] = new String[10];
            for (int j=0; j<array.size(); j++)
             {
                
                String[] splited = array.get(j).split(",");
                
                track[j] = splited[4];
                trackID1[j] = Integer.parseInt(track[j]);
                System.out.println(trackID1[j]);
                
             }
         return trackID1;
            
    }
    
    public ArrayList getRandom(ArrayList<String> array)
     {
         
        ArrayList<String> newlist = new ArrayList<String>();
        
         Collections.shuffle(array);
        
         for (int j=0; j<10; j++)
         {
             newlist.add(array.get(j));
             
         }
         return newlist;
         
     }   
   
   public void savePlaylist() throws IOException
   {
        if (trackID1[0] == 0) 
        {
            alertText.setText("You can't save an empty playlist!");
            //System.out.println("here");
        }
        else
        {
            Parent root = FXMLLoader.load(getClass().getResource("/radio/GUI/Alert.fxml"));   
            scene1 = new Scene(root);

            stage1.setScene(scene1);
            stage1.show();
        }
   }
    
 
    public void saveDatabase()
    {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        String n = playlistName.getText();
        
        if (n.equals("")) 
        {
            text.setText("     Enter a valid playlist name!");
        }    
        else
            {
            try{

                   Class.forName("org.sqlite.JDBC");
                   con = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");

                   con.setAutoCommit(false);

                   st7 = con.createStatement();

                   String sql = "INSERT INTO Playlist (playlistName, playlistType) " + "VALUES ('"+n+"', 'Private');";

                   st7.executeUpdate(sql);
                   con.commit();


                   System.out.println("insert");
                   st8 = con.createStatement(); 
                   rs6 = st8.executeQuery("SELECT * FROM Playlist;");

                   while (rs6.next())
                   {
                   System.out.println(rs6.getInt(1));
                   }

                while (rs6.next())
                {

                    String pName = rs6.getString("playlistName");
                    if (pName.equals(n))
                    {
                       playlistID = rs6.getInt("playlistID");
                       System.out.println(playlistID);
                    }
                }

                for (int m=0; m<trackID1.length; m++)
                {
                     st9 = con.createStatement(); 

                     String sql2 = "INSERT INTO Playlist_Track (playlistID, trackID) " + "VALUES ("+playlistID+","+trackID1[m]+");";

                     st9.executeUpdate(sql2);



                }
                System.out.println("here");        

              con.commit();

              st7.close();
              st8.close();
              st9.close();
              rs6.close();
              con.close();
            }
            catch(Exception e)
            {
                System.out.println(""+e);

            }  
            stage.close();
        }
        
  
   }
    public void closePopup()
   {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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
    
    
}
        
    

