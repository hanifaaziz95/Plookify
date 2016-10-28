/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * 
 * @author Albert Nguyen
 */

public class TrackPlayerDB{

    //TABLE VIEW AND DATA
    private TableView tableview;
    private ObservableList<Track> row = FXCollections.observableArrayList();
    //MAIN EXECUTOR
    private Connection c;
    

    //CONNECTION DATABASE
    public void searchDB(String table, String columnSearch,String search){
        //this method will search the database based on the search parameters: table, which column,what to search for, respectively
        //Used for finding specific tracks linked to specific artists or genres.
          Connection c ;
          row.removeAll(row);
          try{
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            //SQL FOR SELECTING DATA 
            
            //ResultSet
            
            
            if(table.equals("TRACK")){
                
                    System.out.println("hi");
                String SQL = "SELECT * from " + table + " WHERE " + columnSearch + " LIKE '%" + search + "%';";
                ResultSet rs = c.createStatement().executeQuery(SQL);
                while(rs.next()){
                //Iterate Row
                    int id = rs.getInt(1);
                    String tName = rs.getString(2);
                    String length = secToMin(rs.getInt(3));
                    String genre = getTextDB(rs.getInt(4),"Genre");
                    String artist = getTextDB(rs.getInt(5),"Artist");
                    Track track = new Track(id,tName,length,genre,artist);
                    row.add(track);

                }
            }
                
          
            c.close();
          }catch(Exception e){
              e.printStackTrace();
              System.out.println("Error on Building Data");             
          }
      }
    public TableView getView(){
        return tableview;
    }
    public void setRowData(ArrayList<Track> rowData){
        tableview = new TableView();
        row.removeAll(row);
        for(Track track: rowData){
            row.add(track);
        }
    }
    public void generateTrackTable(){
        //generates a table and sets all information in row variable accordingly
        TableColumn tNameCol = new TableColumn("Track Name");
        TableColumn lengthCol = new TableColumn("Length");
        TableColumn genreCol = new TableColumn("Genre");
        TableColumn artistCol = new TableColumn("Artist");

        tNameCol.prefWidthProperty().bind(tableview.widthProperty().multiply(0.5));
        lengthCol.prefWidthProperty().bind(tableview.widthProperty().multiply(0.125));
        genreCol.prefWidthProperty().bind(tableview.widthProperty().multiply(0.125));
        artistCol.prefWidthProperty().bind(tableview.widthProperty().multiply(0.25));
        
        
        tNameCol.setCellValueFactory(new PropertyValueFactory<Track,String>("tName"));
        lengthCol.setCellValueFactory(new PropertyValueFactory<Track,Integer>("length"));
        genreCol.setCellValueFactory(new PropertyValueFactory<Track,String>("genre"));
        artistCol.setCellValueFactory(new PropertyValueFactory<Track,String>("artist"));
        tableview.setItems(row);
        tableview.getColumns().addAll(tNameCol,lengthCol,genreCol,artistCol); 
    }
            
    public String getTextDB(int ID,String table){
        //gets text relevant to genre or artist ID
        String text="";
        
        try{
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            String SQL = "SELECT * from " + table + ";";
            ResultSet rs = c.createStatement().executeQuery(SQL);
            while(rs.next()){
                if(ID == rs.getInt(1)){
                    text = rs.getString(2);
                }
            }
            c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on finding text");  
        }  
        return text;
    }
    public String getID(String text, String table){
        //This is a general method to get either the ID of genre or artist
        String ID="";
        try{
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            String SQL = "SELECT * from " + table;
            ResultSet rs = c.createStatement().executeQuery(SQL);
            while(rs.next()){
                if(text.toLowerCase().equals(rs.getString(2).toLowerCase())){
                    ID = rs.getString(1);
                }
            }
            c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on getting ID");  
        }  

        return ID;
    }
    public String secToMin(int seconds){
        int secs = seconds % 60;
        int mins = (int) Math.floor(seconds / 60);
        if(secs <10){
            String newSecs = "0" + secs;
            return mins + ":" + newSecs;
        }
        return mins + ":" + secs;
    }
    public ObservableList<Track> getRows(){
        return row;
    }
    public int getLastPlayID(int ID){
        int lastID = 0;
        try{
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            String SQL = "SELECT lastPlaylistID from Customer WHERE CustomerID = '" + ID + "';";
            ResultSet rs = c.createStatement().executeQuery(SQL);
            while(rs.next()){
                lastID = rs.getInt(1);
            }
            c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on getting ID");  
        }  
        return lastID;
    }
    public ArrayList<Track> getPlaylist(int playlistID){
        ArrayList<Track> playlist = new ArrayList<Track>();
        try{
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            String SQL = "SELECT trackID from Playlist_Track WHERE playlistID = '" + playlistID + "';";
            ResultSet rs = c.createStatement().executeQuery(SQL);
            while(rs.next()){
                Track track = getTrack(rs.getInt(1));
                playlist.add(track);
            }
            c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on getting playlist");  
        }  
        return playlist;
    }
    public Track getTrack(int ID){
        Track track = null;
        try{
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            String SQL = "SELECT * from Track WHERE trackID = '" + ID + "';";
            ResultSet rs = c.createStatement().executeQuery(SQL);
            while(rs.next()){
                int id = rs.getInt(1);
                String tName = rs.getString(2);
                String length = secToMin(rs.getInt(3));
                String genre = getTextDB(rs.getInt(4),"Genre");
                String artist = getTextDB(rs.getInt(5),"Artist");
                track = new Track(id,tName,length,genre,artist);
            }
            c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on getting playlist");  
        }
        return track;
    }
    public void addSongDB(int playID, int trackID){
        try{
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            String SQL = "INSERT INTO Playlist_Track(playlistID,trackID) " + " VALUES( " + playID + "," + trackID + ");";
            c.createStatement().executeUpdate(SQL);
            c.commit();
            c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on adding song to DB");
        }
    }
    public void removeSongDB(int playID, int trackID){
        try{
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            String SQL = "DELETE from Playlist_Track WHERE playlistID = '" + playID + "' AND trackID = '" +trackID + "';";
            c.createStatement().executeUpdate(SQL);
            c.commit();
            c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on removing song from DB");
        }
    }
    public String getUserName(int userID){
        String userName = "";
        try{
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            String SQL = "SELECT username from Customer WHERE CustomerID = '" + userID + "';";
            ResultSet rs = c.createStatement().executeQuery(SQL);
            while(rs.next()){
                userName = rs.getString(1);
            }
            c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on getting username");
        }
        return userName;
    }
    public String getPlaylistName(int playlistID){
        String pName = "";
        try{
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            String SQL = "SELECT playlistName from Playlist WHERE playlistID = '" + playlistID + "';";
            ResultSet rs = c.createStatement().executeQuery(SQL);
            while(rs.next()){
                pName = rs.getString(1);
            }
            c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on getting playlist name");
        }
        return pName;
    }
}