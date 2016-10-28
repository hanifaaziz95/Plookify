/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account.logic;

import common.Database;
import common.MainFrameController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import player.TrackPlayer;

/**
 * FXML Controller class
 *
 * @author hanifaaziz
 */
public class FreeController implements Initializable {
    
    @FXML 
    private TextField firstName;
    
    @FXML
    private TextField lastName;
    
    @FXML
    private TextField address;
    
    @FXML 
    private TextField email;
    
    @FXML
    private TextField phone_number;
    
    @FXML
    private TextField username;
    
    @FXML
    private TextField password;
    
    @FXML
    private Label invalid; 
    
    @FXML
    public void submitButton(ActionEvent event) throws IOException{
        
            
        if(firstName.getText().isEmpty() || lastName.getText().isEmpty() || address.getText().isEmpty() || email.getText().isEmpty() || username.getText().isEmpty() || password.getText().isEmpty()){
            invalid.setVisible(true);
        }
        else{
            ((Node)event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Database DB = new Database();
            int ID = DB.getCustomerID(username.getText());
            TrackPlayer tb = new TrackPlayer(ID);
            FXMLLoader loader = new FXMLLoader();
            
            BorderPane root = loader.load(getClass().getResource("/common/mainFrame.fxml").openStream());
            MainFrameController mfController = (MainFrameController)loader.getController();
            mfController.setUser(username.getText());
            Scene scene = new Scene(root);
            primaryStage.setTitle("Plookify");
            primaryStage.setScene(scene);
            primaryStage.show(); 
            Database db = new Database();
            db.addRecord("INSERT INTO CUSTOMER(firstName,lastName,address,emailAddress,subscribed,username,password) " + "VALUES( " + "'" + firstName.getText() + "'," + "'" + lastName.getText() + "'," + "'" + address.getText() + "','" + email.getText() + "', 0 ,'" + username.getText() + "','" + password.getText() + "'" +");");
        }
    }
    
    @FXML
    public void back(ActionEvent e) throws IOException{
        Parent sub = FXMLLoader.load(getClass().getResource("/account/gui/Plookify.fxml"));
        Scene sub_scene = new Scene(sub);
        Stage sub_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        sub_stage.setScene(sub_scene);
        sub_stage.show();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
