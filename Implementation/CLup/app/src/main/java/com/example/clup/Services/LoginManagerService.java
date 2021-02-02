package com.example.clup.Services;

import com.example.clup.OnCredentialCheckListener;

public interface LoginManagerService {
    public void manageLogin(String email, String password, OnCredentialCheckListener onCredentialCheckListener);
}
