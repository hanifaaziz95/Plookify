/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Hanifa Aziz
 */
public class Database {
    
    private Connection c = null;
    private Statement stmt = null;
    
    public ResultSet selectRecord(String query)
    {
        ResultSet rs = null;
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully1");

            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            //rs.close();
            //stmt.close();
            //c.close();
  
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return rs;
    }
    
    public void addRecord(String query){
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = query;
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }
    
    public boolean searchlogin(String username, String password)
    {
        boolean isTrue = false;
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer WHERE USERNAME= " + "'" + username + "'" + " AND PASSWORD= " + "'" + password + "'");
            
            while(rs.next()){
                if(rs.getString("USERNAME") != null && rs.getString("PASSWORD") != null){
                    String uname = rs.getString("USERNAME");
                    System.out.println(uname);
                    String pword = rs.getString("PASSWORD");
                    System.out.println(uname + ":"+pword+":logged in");
                    isTrue = true; 
                }
            }
            rs.close();
            stmt.close();
            c.close();
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        return isTrue;            
    }
    
    public void deleteRecord(String query){
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = query;
            stmt.executeUpdate(sql);

            c.commit();
            stmt.close();
            c.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }
    
    public boolean checkSub(String username){
        boolean isTrue = false;
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer WHERE USERNAME= " + "'" + username + "'");
            
            while(rs.next()){
                isTrue = rs.getBoolean("subscribed");
            }
            
            rs.close();
            stmt.close();
            c.close();
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return isTrue;     
    }
    
    public String dateSubscribed(int customerID){
        String sDate = "";
        Date d = null;
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Subscriber WHERE customerID= " + customerID+";" );
            System.out.println("SELECT * FROM Subscriber WHERE customerID= " + customerID+";");
            
            while(rs.next()){
                sDate = rs.getString("dateSubscribed");
                System.out.println(sDate + "ds");
            }
            
            rs.close();
            stmt.close();
            c.close();
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return sDate;  
    } 
    
    public int checkNumDevices(int id){
        int num = 0;
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet r = stmt.executeQuery("SELECT COUNT(*) FROM Device where customerID =" + id + ";");
            while(r.next()){
                num = r.getInt(1);
            }
            System.out.println(num);
            r.close() ;
            stmt.close();
            c.close();
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return num;  
    }
    
    public int getCustomerID(String username){
        int ID = 0; 
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer WHERE USERNAME= " + "'" + username + "'");
            while(rs.next()){
                ID = rs.getInt("customerID");
                System.out.println(ID + "ID");
            }
            rs.close();
            stmt.close();
            c.close();
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        return ID;
    }
    
    public void updateRecord(int customerID){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            stmt.executeUpdate("UPDATE Subscriber set lastPayment = '" + dateFormat.format(date) + "'" + "where customerID=" + customerID + ";");
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        System.exit(0);
        }
    }
    
    public String getLastPayment(int customerID){
        String sDate = "";
        Date d = null;
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Subscriber WHERE customerID= " + customerID+";" );
            System.out.println("SELECT * FROM Subscriber WHERE customerID= " + customerID+";");
            
            while(rs.next()){
                sDate = rs.getString("lastPayment");
                System.out.println(sDate + "ds");
            }
            
            rs.close();
            stmt.close();
            c.close();
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return sDate; 
    }
    
    public String getPaymentType(int customerID){
        String pt = "";
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Subscriber WHERE customerID= " + customerID+";" );
            
            while(rs.next()){
                pt = rs.getString("paymentType");
            }
            
            rs.close();
            stmt.close();
            c.close();
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return pt; 
    }
    
    public void replaceDeviceDB(String newDevice,int i){
        
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            //int i = getCustomerID(user);
            stmt.executeUpdate("UPDATE Device set deviceName = '" + newDevice + "', dateAdded ='" + dateFormat.format(date) + "' where deviceID=" + i + ";");
            System.out.println("UPDATE Device set deviceName = '" + newDevice + "', dateAdded ='" + dateFormat.format(date) + "' where customerID=" + i + ";");
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        System.exit(0);
        } 
    }
    
    public int getDeviceID(int customerID){
        int ID = 0; 
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Device WHERE customerID= " + customerID + ";");
            while(rs.next()){
                ID = rs.getInt("deviceID");
                System.out.println(ID + "ID");
            }
            rs.close();
            stmt.close();
            c.close();
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        return ID;
    }
    
    public void deleteRecorda(){
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            //String sql = query;
            stmt.executeUpdate("DELETE FROM Device;");
            System.out.println("device");
            c.commit();
            stmt.close();
            c.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }
    
    public String dateDeviceAdded(int id){
        String sDate = "";
        Date d = null;
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Device WHERE deviceID= " + id+";" );
            //System.out.println("SELECT * FROM Subscriber WHERE customerID= " + customerID+";");
            
            while(rs.next()){
                sDate = rs.getString("dateAdded");
                System.out.println(sDate + "ds");
            }
            
            rs.close();
            stmt.close();
            c.close();
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return sDate;  
    }
    
    public void deleteSubRecord(int id){
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            stmt.executeUpdate("DELETE FROM Subscriber WHERE customerID = " + id + ";");

            c.commit();
            stmt.close();
            c.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }
    
    public void paymentMissed(int customerID){
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            stmt.executeUpdate("UPDATE Customer set subscribed = " + 0 + ", message ='payment missed'" +" where deviceID=" + customerID + ";");
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        System.exit(0);
        } 
    }
    
    public boolean getMessage(String username){
        Boolean b = false;
        String message = "";
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer WHERE USERNAME = " + "'" + username + "';");
            System.out.println(username + " username");
            while(rs.next()){
                message = rs.getString(10);
                System.out.println(message + "message");
            }
            try{
                if(message.isEmpty()){
                    
                }
                else{
                    b = true;
                }
            }catch(NullPointerException e){
                
            }
            rs.close();
            stmt.close();
            c.close();
            
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        return b;
    }
    
    public void clearMessage(String user){
        try {
            c = DriverManager.getConnection("jdbc:sqlite::resource:common/plookifydatabase.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            stmt.executeUpdate("UPDATE Customer set message =null" +" where username='" + user + "';");
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        System.exit(0);
        } 
    }
}
    
