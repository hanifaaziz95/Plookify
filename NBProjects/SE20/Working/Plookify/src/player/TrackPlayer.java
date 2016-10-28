package player;

import common.MainFrameController;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Albert Nguyen
 */
public class TrackPlayer{
    private static ArrayList<Track> nowTrackPlaylist = new ArrayList<>();
    private static int currentPlaylistID;
    private static int currentIndex = 0;
    private static Scene scene;
    private static String MEDIA_URL;
    private static Media media;
    private static MediaPlayer mediaPlayer;
    private static int userID = -1;
    
    private static TrackPlayerDB tpDB = new TrackPlayerDB();
    public TrackPlayer() throws IOException { 
        Track testTrack = new Track(1,"R U Mine?","3:51","Indie/Rock","Arctic Monkeys");
        nowTrackPlaylist.add(testTrack);
        Group root = new Group();
        scene = new Scene(root, 1024, 768);
        //scene.getStylesheets().add("file:///D://test.css");
        // create media player
        setupMediaControl();


        }
    public TrackPlayer(int userID){
        Group root = new Group();
        scene = new Scene(root, 1024, 768);
        this.userID = userID;
        int lastID = tpDB.getLastPlayID(userID);
        nowTrackPlaylist = tpDB.getPlaylist(lastID);
        currentPlaylistID = lastID;
        if(nowTrackPlaylist.isEmpty()){
            Track defaultTrack = new Track(1,"R U Mine?","3:51","Indie/Rock","Arctic Monkeys");
            nowTrackPlaylist.add(defaultTrack);
        }
        setupMediaControl();
        
    }
    public static void nextSong()
    {
        if(nowTrackPlaylist.size() - 1 == currentIndex)
        {
            currentIndex = 0;
        }
        else{
            currentIndex+=1;
        }
        setupMediaControl();
    }
   
    public static void setupMediaControl()
    {        
        
        MEDIA_URL = convertTName(nowTrackPlaylist.get(currentIndex).getTName());
        if(MEDIA_URL.equals("")){
            JOptionPane.showMessageDialog(null, "This song is currently not able to be played, moving on to the next song");
            nextSong();
        }
        else{
            media = new Media (MEDIA_URL);
            mediaPlayer = new MediaPlayer(media);
            MainFrameController.setup(mediaPlayer,nowTrackPlaylist,currentIndex,userID,currentPlaylistID);     
        }
    }
    public static String convertTName(String TName){
        String newPath = "";
        try{
            
            TName = TName.replaceAll("[\\s \\W]","");
            newPath = TrackPlayer.class.getClassLoader().getResource("common/songs/" + TName + ".mp3").toString();
            
        }
        catch(Exception e){
            System.out.println(e);
        }
        return newPath;
    }
    public static void addSong(Track newSong){
        nowTrackPlaylist.add(newSong);        
        tpDB.addSongDB(currentPlaylistID, newSong.getID());
    }
    public static void removeSong(Track removeSong){
        nowTrackPlaylist.remove(removeSong);
        tpDB.removeSongDB(currentPlaylistID, removeSong.getID());
    }
    public static MediaPlayer getMP(){
    return mediaPlayer;
}
    public static ArrayList<Track> getNowPlay(){
    return nowTrackPlaylist;
}
    public static int getCurrentIndex(){
    return currentIndex;
}
    public static int getPlayID(){
    return currentPlaylistID;
}
    public static void changePlaylist(int newPlaylistID){
        currentPlaylistID = newPlaylistID;
        nowTrackPlaylist = tpDB.getPlaylist(currentPlaylistID);
        setupMediaControl();
    }
}
