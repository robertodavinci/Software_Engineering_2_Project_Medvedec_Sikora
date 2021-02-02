package com.example.clup.Services;

import com.example.clup.OnGetDataListener;

import java.util.List;

public interface StoreSelectionManagerService {
    public void getStoreCities(OnGetDataListener onGetDataListener);
    public void getStores(String city, OnGetDataListener onGetDataListener);
    public void getStoreAddresses(String city, String name, OnGetDataListener onGetDataListener);
}
