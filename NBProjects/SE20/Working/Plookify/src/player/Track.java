
package player;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Albert Nguyen
 */
public class Track {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty tName;
    private final SimpleStringProperty length;
    private final SimpleStringProperty genre;
    private final SimpleStringProperty artist;
 
    Track(int id,String tName, String length, String genre, String artist) {
        this.id = new SimpleIntegerProperty(id);
        this.tName = new SimpleStringProperty(tName);
        this.length = new SimpleStringProperty(length);
        this.genre = new SimpleStringProperty(genre);
        this.artist = new SimpleStringProperty(artist);
    }

 
    public String getTName() {
        return tName.get();
    }
    public void setTName(String TName){
        tName.set(TName);
    }
        
    public String getLength() {
        return length.get();
    }
    
    public String getGenre() {
        return genre.get();
    }
    public String getArtist() {
        return artist.get();
    }   
    public int getID() {
        return id.get();
    }   
}

