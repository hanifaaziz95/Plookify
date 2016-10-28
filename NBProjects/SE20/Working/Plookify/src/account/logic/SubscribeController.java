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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import player.TrackPlayer;

/**
 * FXML Controller class
 *
 * @author hanifaaziz
 */
public class SubscribeController implements Initializable {
    
    @FXML 
    private TextField firstName, lastName, address, email, username, password,ppEmail,ppPassword,cardNum,cvv,expiryY,expiryM,cardHolder;
    
    @FXML
    private Tab ccTab;
    
    @FXML
    private ToggleGroup cardType;
    
    @FXML 
    private RadioButton amex;
    
    @FXML 
    private RadioButton visa;
    
    @FXML 
    private RadioButton mcard;
    
    
    @FXML
    private Label invalid; 
    
    @FXML 
    private Button loginPP;
    
    private String paymentType;
    
    @FXML
    public void paymentButton(ActionEvent event) throws IOException {
        if(firstName.getText().isEmpty() || lastName.getText().isEmpty() || address.getText().isEmpty() || email.getText().isEmpty() || username.getText().isEmpty() || password.getText().isEmpty()){
            invalid.setText("Complete all fields");
        }
        else{
           
            RadioButton type = (RadioButton)cardType.getSelectedToggle(); 
            try{
                if(!type.getText().equals(null)){
                paymentType = type.getText();
                }
            }catch(NullPointerException e){
                
            }
            
            ((Node)event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Database DB = new Database();
            int ID = DB.getCustomerID(username.getText());
            TrackPlayer tb = new TrackPlayer(ID);
            FXMLLoader loader = new FXMLLoader();
            Database db = new Database();
            db.addRecord("INSERT INTO CUSTOMER(firstName,lastName,address,emailAddress,subscribed,username,password) " + "VALUES( " + "'" + firstName.getText() + "'," + "'" + lastName.getText() + "'," + "'" + address.getText() + "','" + email.getText() + "'," + 1 + ",'" + username.getText() + "','" + password.getText() + "'" +");");
            makeCustomerSubscriber(username.getText());
            BorderPane root = loader.load(getClass().getResource("/common/mainFrame.fxml").openStream());
            MainFrameController mfController = (MainFrameController)loader.getController();
            mfController.setUser(username.getText());
            Scene scene = new Scene(root);
            primaryStage.setTitle("Plookify");
            primaryStage.setScene(scene);
            primaryStage.show(); 
            
        }
    }
    
    @FXML
    public void makeCustomerSubscriber(String username){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        Database db = new Database();
        int customerID = db.getCustomerID(username);
        db.addRecord("INSERT INTO SUBSCRIBER(dateSubscribed,customerID,paymentType,lastPayment)" + "VALUES(" + "'" + dateFormat.format(date) + "'," + customerID + ",'" + paymentType + "','" + dateFormat.format(date) + "');");
    }
    @FXML
    public void loginToPaypal(){
        if(!ppEmail.getText().isEmpty()||!ppPassword.getText().isEmpty()){
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Login confirmed");
            window.setMinWidth(250);
           

            Button ok = new Button("Ok"); 
            Label pay = new Label("Your payment of £4.99 is complete.");

            Label emailTag = new Label("An email confirmation has been sent to " + ppEmail.getText()); 

            ok.setOnAction(e -> window.close());


            VBox layout = new VBox(30);
            layout.getChildren().addAll(pay,emailTag, ok);
            layout.setAlignment(Pos.CENTER);
            layout.setStyle("-fx-background-color:#262626;");
            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();

            loginPP.setDisable(true);
            ppEmail.setEditable(false);
            ppEmail.setStyle("-fx-background-color: #262626");
            ppPassword.setEditable(false);
            ppPassword.setStyle("-fx-background-color: #262626");
            ccTab.setDisable(true);

            paymentType = "PayPal";
            invalid.setVisible(false);
        }
        else{
            invalid.setText("Enter PayPal email & password");
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }     
}
