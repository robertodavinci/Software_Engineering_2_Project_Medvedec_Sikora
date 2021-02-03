package com.example.clup.Entities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Set;

public class ApplicationState extends Application {

    private String storeCity;
    private Integer storeID;
    private Integer ticketID;
    private String storeName;

    public String getStoreCity() {
        return storeCity;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public Integer getTicketID() {
        return ticketID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public void setTicketID(Integer ticketID) {
        this.ticketID = ticketID;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }


}