/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import player.Track;
import player.TrackPlayer;

/**
 * FXML Controller class
 *
 * @author hanifaaziz
 */
public class mediaBarController implements Initializable {
    private static MediaPlayer mp; //Needed to store the MediaPlayer class
    private static Duration duration;//Needed to use when dealing with temporal things
    private static ArrayList<Track> nowTrackPlaylist; // list of songs to be played
    private static boolean stopRequested = false;//check to see user wants to stop the song
    private static int userID; // ID of the current user
    private static int playlistID; // ID of the current playlist
    private static int currentIndex; // index of the currently playing song in the playlist 
    
    
    @FXML
    private Button playButton;
    @FXML
    private ImageView img;
    @FXML
    private Button nextButton;
    @FXML
    private ImageView img2;
    @FXML
    private  Slider timeSlider;
    @FXML
    private  Label playtime;
    @FXML
    private  Slider volumeSlider;
    @FXML
    private  Label songText;
    @FXML
    private  Label genText;
    @FXML
    private  Label artText;
    @FXML
    private Button jumpButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image image = new Image("/common/images/play.png");
        img.setImage(image);
        
        Image next = new Image("/common/images/forward.png");
        img2.setImage(next);
        
        updateInfo(); 
    }    

    @FXML
    private void playButtonClicked(ActionEvent event) {
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
    private void nextButtonClicked(ActionEvent event) {
        mp.stop();
        TrackPlayer.nextSong();
        mp.play();
        updateInfo();
    }

    @FXML
    private void jumpButtonClicked(ActionEvent event) {
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
    public void updateInfo(){
        duration = mp.getMedia().getDuration();
        updateValues();
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
    public static void setup(final MediaPlayer newMP, ArrayList<Track> songlist,int newIndex,int newPlaylistID){
        mp = newMP;
        nowTrackPlaylist = songlist;
        currentIndex = newIndex;
        playlistID = newPlaylistID;
    }
}
