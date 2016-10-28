package playlist.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class playlist {
    private final SimpleStringProperty playlistName;
    private final SimpleIntegerProperty playlistID;
    private final SimpleStringProperty playlistType;
    
    playlist(int pID, String pName, String pType) {
        this.playlistName = new SimpleStringProperty(pName);
        this.playlistID = new SimpleIntegerProperty(pID);
        this.playlistType = new SimpleStringProperty(pType);
    }
    
    public String getPlaylistType() {
        return playlistType.get();
    }
    
    public void setPlaylistType(String pType) {
        playlistType.set(pType);
    }
 
    public String getPlaylistName() {
        return playlistName.get();
    }
    
     public void setPlaylistName(String pName) {
        playlistName.set(pName);
    }
     
    public int getPlaylistID() {
        return playlistID.get();
    }
    
    public void setPlaylistID(int ID) {
        playlistID.set(ID);
    }
}
