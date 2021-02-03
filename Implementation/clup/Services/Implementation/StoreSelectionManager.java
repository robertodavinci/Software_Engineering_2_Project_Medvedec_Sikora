package com.example.clup.Services.Implementation;

import com.example.clup.OnGetDataListener;
import com.example.clup.Services.DatabaseManagerService;
import com.example.clup.Services.StoreSelectionManagerService;
import com.example.clup.Entities.Store;

import java.util.List;

public class StoreSelectionManager implements StoreSelectionManagerService {
    private Store currentStore;
    private DatabaseManagerService dbManager = new DatabaseManager();


    @Override
    public void getStoreCities(OnGetDataListener onGetDataListener) {
        dbManager.getStoreCities(onGetDataListener);
    }
    public void getStores(String city, OnGetDataListener onGetDataListener){
        dbManager.getStores(city, onGetDataListener);
    }
    public void getStoreAddresses(String city, String name, OnGetDataListener onGetDataListener) {
        dbManager.getStoreAddresses(city, name, onGetDataListener);
    }

    public void setCurrentStore(Store currentStore) {
        this.currentStore = currentStore;
    }

    public Store getCurrentStore() {
        return currentStore;
    }
}
