package com.example.clup.Services.Implementation;

import com.example.clup.Services.DatabaseManagerService;
import com.example.clup.Services.StoreSelectionManagerService;
import com.example.clup.Entities.Store;

import java.util.List;

public class StoreSelectionManager implements StoreSelectionManagerService {
    private Store currentStore;
    private DatabaseManagerService dbManager = new DatabaseManager();


    public List<String> getStoreCities(){
        return dbManager.getStoreCities();
    }
    public List<String> getStores(String city){
        return dbManager.getStores(city);
    }
    public List<String> getStoreAddresses(String city, String name){
        return dbManager.getStoreAddresses(city, name);
    }

    public void setCurrentStore(Store currentStore) {
        this.currentStore = currentStore;
    }

    public Store getCurrentStore() {
        return currentStore;
    }
}
