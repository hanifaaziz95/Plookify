package player;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Albert
 */
public class TrackMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        TrackPlayer TP = new TrackPlayer(1);
    }
    

    public static void main(String[] args){
        launch(args);
    }
}
