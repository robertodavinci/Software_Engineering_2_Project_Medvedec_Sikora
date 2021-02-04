package com.example.clup.Services.Implementation;

import com.example.clup.Services.DatabaseManagerService;
import com.example.clup.Services.DirectorService;
import com.example.clup.Services.LoginManagerService;
import com.example.clup.Services.StoreSelectionManagerService;

public class Director implements DirectorService {
    private LoginManagerService loginManager = new LoginManager();
    private StoreSelectionManagerService storeSelectionManager = new StoreSelectionManager();
    private DatabaseManagerService databaseManager = new DatabaseManager();

    public LoginManagerService getLoginManager() {
        return loginManager;
    }

    public StoreSelectionManagerService getStoreSelectionManager() {
        return storeSelectionManager;
    }

    public DatabaseManagerService getDatabaseManager() {
        return databaseManager;
    }
}