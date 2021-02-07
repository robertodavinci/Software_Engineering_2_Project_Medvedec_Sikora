package com.example.clup.Entities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;


public class ApplicationState extends Application {
    // ApplicationState serves as the model for the app - it contains some attributes so that app knows who is currently
    // using it and so that it doesn't have to retrieve data from Firebase all the time, which is significantly slower than
    // retrieving it from here

    private String storeCity = "";
    private String storeName = "";
    private String address = "";
    private String storeKey = "";
    private Ticket ticket = new Ticket(0,null);
    private Integer storeID = 1;
    private Boolean isStoreManager = false;
    private Boolean storeOpened;
    private Integer peopleAhead = 0;

    // ApplicationState getters and setters
    public String getStoreCity() { return storeCity; }
    public Integer getStoreID() {
        return storeID;
    }
    public String getStoreName() {
        return storeName;
    }
    public String getAddress() { return address; }
    public String getStoreKey() { return storeKey; }
    public Boolean getStoreManager() { return isStoreManager; }
    public Ticket getTicket() { return ticket; }
    public Integer getPeopleAhead() { return peopleAhead; }
    public Boolean getStoreOpened() { return storeOpened; }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }
    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public void setAddress(String address) { this.address = address; }
    public void setStoreKey(String storeKey) { this.storeKey = storeKey; }
    public void setStoreManager(Boolean storeManager) { isStoreManager = storeManager; }
    public void setStoreOpened(Boolean storeStatus) { storeOpened = storeStatus; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    public void setPeopleAhead(Integer peopleInFront) { this.peopleAhead = peopleInFront; }

    // method for clearing ApplicationState to default values
    public void clearAppState(){
            this.storeCity = "";
            this.storeName = "";
            this.address = "";
            this.storeKey = "";
            this.ticket = new Ticket(0,null);
            this.storeID = 0;
            this.isStoreManager = false;
            this.peopleAhead = 0;
            this.storeOpened = null;
    }

    // method that prints ApplicationState variables, used for checking data in debug
    public void printAppState(){
        System.out.println("STATE PRINT");
        if(this.storeCity != null) System.out.println("storeCity: " + this.storeCity);
        if(this.storeName != null) System.out.println("storeName: " + this.storeName);
        if(this.address != null) System.out.println("address: " + this.address);
        if(this.storeKey != null) System.out.println("storeKey: " + this.storeKey);
        if(this.storeID != null) System.out.println("storeID: " + this.storeID.toString());
        if(this.isStoreManager != null) System.out.println("isStoreManager: " + this.isStoreManager.toString());
        if(this.storeOpened != null) System.out.println("storeOpened: " + this.storeOpened.toString());
        if(this.peopleAhead != null) System.out.println("peopleAhead: " + this.peopleAhead.toString());
    }

}