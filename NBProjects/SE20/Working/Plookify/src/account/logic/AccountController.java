/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account.logic;

import common.Database;
import common.MainFrameController;
import common.mediaBarController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.joda.time.Days;
import player.MediaControl;
import player.TrackPlayer;

/**
 * FXML Controller class
 *
 * @author hanifaaziz
 */
public class AccountController implements Initializable {    
    @FXML 
    private Label user, fullname, address, subscribed, payment,maxDevices,notReplace,lastPayment;
    
    @FXML
    private Button pay,add;
    
    @FXML 
    private Button rDevice5;
    
    @FXML 
    private Button rDevice4;
    
    @FXML 
    private Button rDevice3;
    
    @FXML 
    private Button rDevice2;
    
    @FXML 
    private Button rDevice1;
    
    @FXML 
    private Pane paymentPane;
    
    @FXML
    private ImageView image;
    
    @FXML 
    private TableView<Device> table;
         
    private String username;
    
    private int i;
    
    private int deviceID, deviceID2,deviceID3,deviceID4,deviceID5;
    
    @FXML private TableColumn deviceIDCol;
    @FXML private TableColumn deviceNameCol;
    @FXML private TableColumn dateAddedCol;
    
    @FXML 
    private Label paymentlabel,payDue, subscribelabel,todaylabel, lplabel;
    
    @FXML 
    private Button mkPayment,changesub;
    
    private final ObservableList<Device> data = FXCollections.observableArrayList();
    
    @FXML
    private mediaBarController mController;
    
    @FXML
    private Pane sliderPane,freePane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        deviceIDCol.setCellValueFactory(new PropertyValueFactory("deviceID"));
        deviceNameCol.setCellValueFactory(new PropertyValueFactory("deviceName"));
        dateAddedCol.setCellValueFactory(new PropertyValueFactory("dateAdded"));
        Image i = new Image("/common/plookifylogowhite.png");
        image.setImage(i);
    }
    public void addSliderPane(){
        try{
            FXMLLoader radioLoader = new FXMLLoader(getClass().getResource("/common/slider.fxml"));
            Pane rPane =radioLoader.load();
            mController =radioLoader.getController();
            //mControl.setUser(UserID);
            //rPane.relocate(120,0);
            sliderPane.getChildren().add(rPane);
            
    }
        catch (IOException ex) {
        ex.printStackTrace();
        //Alert alert = new Alert(Alert.AlertType.ERROR);
        //alert.setTitle("Error");
        //alert.setHeaderText("");
        //alert.setContentText("Could not load FXML Loader of Overview");
        //alert.showAndWait();
       
    }
    }
    
    @FXML
    public void addData(String username){
        try{
            Database db = new Database();
            int id = db.getCustomerID(username);
            System.out.println(id);
            System.out.println("SELECT * FROM DEVICE where customerID = " + id + ";");
            Connection c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM DEVICE where customerID = " + id + ";");
            i = 1;
            int idDevice = 0;
            
            
            while(rs.next()){
                data.add(new Device(
                    i,
                    rs.getString("deviceName"),
                    rs.getString("dateAdded")
                
                ));
                idDevice = rs.getInt("deviceID");
                System.out.println(idDevice + " idx");
                switch(i){
                case 1:
                    deviceID = idDevice;
                    System.out.println(deviceID + " 1");
                    break;
                case 2:
                    deviceID2 = idDevice;
                    System.out.println(deviceID2 + " 2");
                    break;
                case 3:
                    deviceID3 = idDevice;
                    System.out.println(deviceID3 + " 3");
                    break;
                case 4:
                    deviceID4 = idDevice;
                    System.out.println(deviceID4 + " 4");
                    break;
                case 5:
                    deviceID5 = idDevice;
                    System.out.println(deviceID5 + " 5");
                    break;
            }
                i++;
                table.setItems(this.data);
            }
            
            rs.close();
            stmt.close();
            c.close();
        }catch(Exception e){
            System.err.println(e);
        }  
    }
    
    @FXML
    public void setAccInfo(String username){
        try{
            user.setText(username);
            Database db = new Database();
            Boolean b = db.checkSub(username);
            if(b){
                subscribed.setText("Subscribed");
            }
            else{
                subscribed.setText("Free user");
            }
            
        
        }catch(Exception e){
            System.err.println(e);
        }
    }
    
    @FXML
    public void setUser(String username){
        this.username = username;
    }
    
    @FXML
    public void deleteCustomer()
    {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Confirm closing your account");
        window.setMinWidth(250);

        Button cancel = new Button("Cancel");        
        Button confirm = new Button("Confirm"); 
      
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    Database db = new Database();
                    db.deleteRecord("DELETE FROM Customer WHERE username = " + "'" + username + "';");
                    System.exit(0);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        cancel.setOnAction(e->window.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(cancel, confirm);
        layout.setAlignment(Pos.CENTER);
       
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); 
       
    }
    
    @FXML 
    public void backButton(ActionEvent event) throws IOException{
        ((Node)event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
           // Database DB = new Database();
          //  int ID = DB.getCustomerID(username);
         //   TrackPlayer tb = new TrackPlayer(ID);
            FXMLLoader loader = new FXMLLoader();
            
            BorderPane root = loader.load(getClass().getResource("/common/mainFrame.fxml").openStream());
            MainFrameController mfController = (MainFrameController)loader.getController();
            mfController.setUser(username);
            Scene scene = new Scene(root);
            primaryStage.setTitle("Plookify");
            primaryStage.setScene(scene);
            primaryStage.show();
    }
    
    @FXML
    public void paymentDays(String username) throws ParseException, SQLException{
        this.username = username;
        Boolean b = false;
        String paymentDate = "";
        Database db = new Database();
        b = db.checkSub(username);
        if(b){
            int daysLeft = 0;

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();

            Date d1 = null;
            Date d2 = null;

            int customerID = db.getCustomerID(username);
            String dlp = db.getLastPayment(customerID);
            
            String de = format.format(date);

            try{
                d1 = format.parse(dlp);
                d2 = format.parse(de);

                DateTime dt1 = new DateTime(d1);
                DateTime dt2 = new DateTime(d2);

                Calendar day = Calendar.getInstance();
                int days = day.getActualMaximum(Calendar.DAY_OF_MONTH);
                int daysBetween = Days.daysBetween(dt1,dt2).getDays();
                daysLeft = days - daysBetween;
                payment.setText(Integer.toString(daysLeft) + " days");
                lastPayment.setText(dlp);

            }catch(Exception e){
                    e.printStackTrace();
            }
            if(daysLeft!=0){
                pay.setDisable(true);
            }
            else if(daysLeft<0){
                db.deleteSubRecord(customerID);
                db.paymentMissed(customerID);
            }
        }
        else{
            paymentlabel.setVisible(false);
            payDue.setVisible(false);
            subscribelabel.setVisible(true);
            todaylabel.setVisible(true);
            lplabel.setVisible(false);
            pay.setVisible(false);
            payment.setVisible(false);
            lastPayment.setVisible(false);
            changesub.setVisible(true);
            
        }
          
    }
    
    @FXML
    public void makePayment(){
        Database db = new Database();
        int customerID = db.getCustomerID(username);
        String paymenttype = db.getPaymentType(customerID);
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Confirm payment");
        window.setMinWidth(250);

        Label label = new Label("You have been charged £4.99");
        Label label2 = new Label("Using: " + paymenttype);
                
        Button save = new Button("Save"); 
      
        save.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,label2, save);
        layout.setAlignment(Pos.CENTER);
       
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); 
        
        db.updateRecord(customerID);
    }
    
    @FXML
    public void becomeSub(){
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Add device");
                window.setMinWidth(250);

                Button becomeSub = new Button("Confirm subscription");

                becomeSub.setOnAction(e -> window.close());

                VBox layout = new VBox(10);
                layout.getChildren().addAll(becomeSub);
                layout.setAlignment(Pos.CENTER);
       
                Scene scene = new Scene(layout);
                window.setScene(scene);
                window.showAndWait();                     
    }
    
    @FXML
    public void makeCustomerSubscriber(String username,String name){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        Database db = new Database();
        int customerID = db.getCustomerID(username);
        //db.addRecord("INSERT INTO SUBSCRIBER(dateSubscribed,customerID,paymentType)" + "VALUES(" + "'" + dateFormat.format(date) + "'," + customerID + ",'" + paymentType + "'" + ");");
    }
    
    @FXML
    public void addDevice(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        
        int numOfDevices = 0;
        Database db = new Database();
        int id = db.getCustomerID(username);
        numOfDevices = db.checkNumDevices(id);
        System.out.println(numOfDevices);
            
            if(numOfDevices<5)
            {
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Add device");
                window.setMinWidth(250);

                ObservableList<String> options = FXCollections.observableArrayList(
                "mobile phone",
                "games console",
                "PC/Mac/Tablet"
                );
                final ComboBox comboBox = new ComboBox(options);
                comboBox.setPromptText("Choose device:");
                Button save = new Button("Save"); 
      
                save.setOnAction(e -> window.close());

                VBox layout = new VBox(10);
                layout.getChildren().addAll(comboBox, save);
                layout.setAlignment(Pos.CENTER);
       
                Scene scene = new Scene(layout);
                window.setScene(scene);
                window.showAndWait(); 
                
                String selected = comboBox.getValue().toString();
                System.out.println(selected);
                int custID = db.getCustomerID(username);
                Database db1 = new Database();
                db1.addRecord("INSERT INTO DEVICE(deviceName,dateAdded,customerID) " + "VALUES(" + "'" + selected + "','"+ dateFormat.format(date) + "'," +custID + ");");
                
                data.clear();
                addData(username);
            }
            else{
                add.setDisable(true);
                maxDevices.setVisible(true);
                rDevice1.setVisible(true);
                rDevice2.setVisible(true);
                rDevice3.setVisible(true);
                rDevice4.setVisible(true);
                rDevice5.setVisible(true);
            }
            /*Database db = new Database();
            db.deleteRecorda();*/
        }

        @FXML 
        public void replace1(ActionEvent e){
            if(checkDeviceDate(deviceID)){
                String name = replaceDevice();
                Database db = new Database();
                db.replaceDeviceDB(name,deviceID);
                data.clear();
                addData(username);
            }
            else{
              notReplace.setVisible(true);   
            }
        }
        
        @FXML 
        public void replace2(ActionEvent e){
            if(checkDeviceDate(deviceID2)){
                String name = replaceDevice();
                Database db = new Database();
                db.replaceDeviceDB(name,deviceID2);
                data.clear();
                addData(username);
            }
            else{
               notReplace.setVisible(true); 
            }
        }
        
        @FXML 
        public void replace3(ActionEvent e){
            if(checkDeviceDate(deviceID3)){
                String name = replaceDevice();
                Database db = new Database();
                db.replaceDeviceDB(name,deviceID3);
                data.clear();
                addData(username);  
            }
            else{
                notReplace.setVisible(true);
            } 
        }
        
        @FXML 
        public void replace4(ActionEvent e){
            if(checkDeviceDate(deviceID4)){
                String name = replaceDevice();
                Database db = new Database();
                db.replaceDeviceDB(name,deviceID4);
                data.clear();
                addData(username); 
            }
            else{
                notReplace.setVisible(true);
            }  
        }
        
        @FXML 
        public void replace5(ActionEvent e){
            if(checkDeviceDate(deviceID5)){
                String name = replaceDevice();
                Database db = new Database();
                db.replaceDeviceDB(name,deviceID5);
                data.clear();
                addData(username);
            }
            else{
                notReplace.setVisible(true);
            }
        }
        
        @FXML    
        public String replaceDevice(){
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Replace device");
                window.setMinWidth(250);

                ObservableList<String> options = FXCollections.observableArrayList(
                "mobile phone",
                "games console",
                "PC/Mac/Tablet"
                );
                final ComboBox comboBox = new ComboBox(options);
                comboBox.setPromptText("Choose device:");
                Button save = new Button("Save"); 
      
                save.setOnAction(e -> window.close());

                VBox layout = new VBox(10);
                layout.getChildren().addAll(comboBox, save);
                layout.setAlignment(Pos.CENTER);
       
                Scene scene = new Scene(layout);
                window.setScene(scene);
                window.showAndWait(); 
                
                String selected = comboBox.getValue().toString();
                return selected;
        }  
        
        public boolean checkDeviceDate(int deviceID){
            Boolean isOneMonth = false;
            Database db = new Database();
            String id = db.dateDeviceAdded(deviceID);
            
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();

            Date d1 = null;
            Date d2 = null; 

            String da = id;
            String de = format.format(date);
            try{
                d1 = format.parse(da);
                d2 = format.parse(de);

                DateTime dt1 = new DateTime(d1);
                DateTime dt2 = new DateTime(d1);

                int daysBetween = Days.daysBetween(dt1,dt2).getDays();
                System.out.println(daysBetween + "daysbetween");
                if(daysBetween>30){
                    isOneMonth = true;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return isOneMonth;
        }
        
        public void changeSub(){
            Database db = new Database();
            
        }
}
