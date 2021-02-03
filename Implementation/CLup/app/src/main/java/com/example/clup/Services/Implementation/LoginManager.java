package com.example.clup.Services.Implementation;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.clup.Entities.UserType;
import com.example.clup.MainActivity;
import com.example.clup.OnCredentialCheckListener;
import com.example.clup.Services.LoginManagerService;
import com.example.clup.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginManager implements LoginManagerService {
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    public void manageLogin(String email, String password, UserType userType, OnCredentialCheckListener onCredentialCheckListener){
        // if userType == UserType.STORE_MANAGER
        databaseManager.checkCredentials(email, password, onCredentialCheckListener);
    }
}
