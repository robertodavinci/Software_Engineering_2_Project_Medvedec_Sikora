package com.example.clup.Services.Implementation;

import com.example.clup.Services.DirectorService;
import com.example.clup.Services.LoginManagerService;
import com.example.clup.Services.StoreSelectionManagerService;

public class Director implements DirectorService {
    private LoginManagerService loginManager;
    private StoreSelectionManagerService storeSelectionManager;
}
