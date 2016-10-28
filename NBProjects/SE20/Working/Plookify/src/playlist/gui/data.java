/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playlist.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class data {
    private final SimpleIntegerProperty trackID;
    private final SimpleStringProperty trackName;
    private final SimpleStringProperty artistName;
    private final SimpleStringProperty genreName;
    private final SimpleStringProperty trackLength;
 
    data(int tID, String tName, String aName, String gName, String tLength) {
        this.trackID = new SimpleIntegerProperty(tID);
        this.trackName = new SimpleStringProperty(tName);
        this.artistName = new SimpleStringProperty(aName);
        this.genreName = new SimpleStringProperty(gName);
        this.trackLength = new SimpleStringProperty(tLength);
    }
    
    public int getTrackID() {
        return trackID.get();
    }
    
    public void setTrackID(int tID) {
        trackID.set(tID);
    }
 
    public String getTrackName() {
        return trackName.get();
    }
    
     public void setTrackName(String tName) {
        trackName.set(tName);
    }
     
    public String getArtistName() {
        return artistName.get();
    }
    
     public void setartistName(String aName) {
        artistName.set(aName);
    }
     
     public String getGenreName() {
        return genreName.get();
    }
     
    public void setGenreName(String gName) {
        genreName.set(gName);
    }
    
      public String getTrackLength() {
        return trackLength.get();
    }
      
    public void setTrackLength(String tLength) {
        trackLength.set(tLength);
    }
      
        
}
