/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account.logic;

import common.Database;
import common.MainFrameController;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import player.TrackPlayer;

/**
 * FXML Controller class
 *
 * @author hanifaaziz
 */
public class PlookifyController implements Initializable {

    @FXML
    private TextField username;
    
    @FXML
    private TextField password;
    
    @FXML 
    private Label invalid;    
    
    @FXML
    private void buttonLogin(ActionEvent event) throws IOException, SQLException {
        if(isValidCredentials()){
            ((Node)event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Database DB = new Database();
            int ID = DB.getCustomerID(username.getText());
            TrackPlayer tb = new TrackPlayer(ID);
            FXMLLoader loader = new FXMLLoader();
            
            BorderPane root = loader.load(getClass().getResource("/common/mainFrame.fxml").openStream());
            MainFrameController mfController = (MainFrameController)loader.getController();
            mfController.setUser(username.getText());
            mfController.displayMissedPayment(username.getText());
            Scene scene = new Scene(root);
            primaryStage.setTitle("Plookify");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        }
        else{
            username.clear();
            password.clear();
            invalid.setVisible(true);
        }
    }
    
    @FXML
    private void buttonSubscribe(ActionEvent event) throws IOException {
        Parent sub = FXMLLoader.load(getClass().getResource("/account/gui/Subscribe.fxml"));
        Scene sub_scene = new Scene(sub);
        Stage sub_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sub_stage.setScene(sub_scene);
        sub_stage.show();
    }
    
    @FXML
    private void buttonFree(ActionEvent event) throws IOException {
        Parent free = FXMLLoader.load(getClass().getResource("/account/gui/Free.fxml"));
        Scene free_scene = new Scene(free);
        Stage free_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        free_stage.setScene(free_scene);
        free_stage.show();
    }
    
    private boolean isValidCredentials() throws SQLException
    {
        boolean correct = false;
       
        try {
            Database db = new Database();
            correct =  db.searchlogin(username.getText(), password.getText());
            
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return correct;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    
}
