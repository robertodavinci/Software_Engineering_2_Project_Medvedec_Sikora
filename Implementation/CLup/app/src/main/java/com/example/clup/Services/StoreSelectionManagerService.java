package com.example.clup.Services;

import java.util.List;

public interface StoreSelectionManagerService {
    public List<String> getStoreCities();
    public List<String> getStores(String city);
    public List<String> getStoreAddresses(String city, String name);
}
