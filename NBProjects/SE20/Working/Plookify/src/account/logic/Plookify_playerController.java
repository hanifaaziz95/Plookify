/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account.logic;

import account.logic.PlookifyController;
import common.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hanifaaziz
 */
public class Plookify_playerController implements Initializable {
    
    @FXML
    private ImageView image;
    @FXML
    private Label user;
    
    @FXML
    private Button radio;
    
    @FXML
    private Label premium;
    
    private String username;
    
    @FXML
    public void setUser(String username){
        user.setText("Welcome, " + username);
        this.username = username;
    }
    
    @FXML 
    public void checkSub(ActionEvent e) throws IOException
    {
        try{
            boolean s = false;
            Database db = new Database();
            s = db.checkSub(username);
            System.out.println("SELECT * FROM CUSTOMER WHERE USERNAME =" + "'" + username + "'");

            if(s)
            {
                System.out.println("subscribed");
            }
            else{
                radio.setDisable(true);
                premium.setText("Available on subscription");
            }
        }
        catch(Exception ex){
            System.err.println(ex);
        }
    }
    
    @FXML
    public void accButton(ActionEvent event) throws IOException, ParseException, SQLException
    {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        BorderPane root = loader.load(getClass().getResource("account.fxml").openStream());
        AccountController ppController = (AccountController)loader.getController();
        ppController.paymentDays(username);
        ppController.setAccInfo(username);
        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("scroll.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @FXML
    public void signoutButton(ActionEvent e) throws IOException {
        Parent sub = FXMLLoader.load(getClass().getResource("plookify.fxml"));
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
        Image i = new Image("/common/plookifylogo.png");
        image.setImage(i);
    }   

}
