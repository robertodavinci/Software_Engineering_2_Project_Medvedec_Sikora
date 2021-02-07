package com.example.clup.Services;

import com.example.clup.Entities.Store;
import com.example.clup.OnTaskCompleteListener;

public interface ExitService {
    public void manageExit(Store store, OnTaskCompleteListener onTaskCompleteListener);
}
