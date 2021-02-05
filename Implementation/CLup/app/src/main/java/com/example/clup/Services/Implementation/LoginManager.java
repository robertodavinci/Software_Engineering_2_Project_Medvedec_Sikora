package com.example.clup.Services.Implementation;

import com.example.clup.Entities.UserType;
import com.example.clup.OnCredentialCheckListener;
import com.example.clup.Services.LoginManagerService;

public class LoginManager implements LoginManagerService {
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    public void manageLogin(String email, String password, UserType userType, OnCredentialCheckListener onCredentialCheckListener){
        // if userType == UserType.STORE_MANAGER
        databaseManager.checkCredentials(email, password, onCredentialCheckListener);
    }
}
