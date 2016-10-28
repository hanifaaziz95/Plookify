/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account.logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author hanifaaziz
 */
public class Device{
        
        private IntegerProperty deviceID;
        private StringProperty deviceName;
        private StringProperty dateAdded;
         
 
        public Device(int dID, String dName, String dAdded) {
            deviceID = new SimpleIntegerProperty(dID);
            deviceName = new SimpleStringProperty(dName);
            dateAdded = new SimpleStringProperty(dAdded);
        }
        
        public IntegerProperty deviceIDProperty() {
            return deviceID;
        }
 
        public StringProperty deviceNameProperty() {
            return deviceName;
        }
 
        public StringProperty dateAddedProperty() {
            return dateAdded;
        }

}

