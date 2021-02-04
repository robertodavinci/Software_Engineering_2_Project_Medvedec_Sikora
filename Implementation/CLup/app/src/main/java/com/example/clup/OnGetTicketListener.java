package com.example.clup;

import com.example.clup.Entities.Ticket;

public interface OnGetTicketListener {
    public void onSuccess(Ticket ticket);
    public void onFailure();
}
