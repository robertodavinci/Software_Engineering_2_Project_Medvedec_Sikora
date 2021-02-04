package com.example.clup;

import com.example.clup.Entities.Store;

public interface OnCredentialCheckListener {
    void onSuccess(String storeName, String storeCity, int storeId);
    void onFailure();
}