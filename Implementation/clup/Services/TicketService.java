package com.example.clup.Services;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.OnGetTicketListener;

public interface TicketService {
    public void getTicket(Store store, OnGetTicketListener onGetTicketListener);
}
