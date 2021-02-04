package com.example.clup.Entities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;


public class ApplicationState extends Application {

    private String storeCity = "Milano, Italy";
    private String storeName = "Carrefour";
    private String address = "Via Golgi 1";
    private String storeKey = "1234567812345678";
    private Ticket ticket = new Ticket(0,null);
    private Integer storeID = 1;
    private Boolean isStoreManager = false;
    private Integer peopleAhead;

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
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    public void setPeopleAhead(Integer peopleInFront) { this.peopleAhead = peopleInFront; }

}