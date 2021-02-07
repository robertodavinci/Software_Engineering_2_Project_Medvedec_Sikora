package com.example.clup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface OnGetDataListener {
    void onSuccess(DataSnapshot dataSnapshot);
    void onFailure(DatabaseError databaseError);
}
