package com.example.clup;

import java.sql.Timestamp;

public interface OnCheckTicketListener {
    public void onWaiting(int peopleAhead);
    public void onActive(Timestamp expireTime);
    public void onBadStore(String error);
}
