package com.example.clup.Services;

import com.example.clup.OnTaskCompleteListener;

public interface EnterService {
    public void manageEntrance(String qrCodeText, OnTaskCompleteListener onTaskCompleteListener);
}
