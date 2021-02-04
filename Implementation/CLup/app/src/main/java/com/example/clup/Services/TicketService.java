package com.example.clup.Services;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.OnCheckTicketListener;
import com.example.clup.OnGetDataListener;
import com.example.clup.OnGetTicketListener;
import com.example.clup.OnTaskCompleteListener;

public interface TicketService {
    public void getTicket(Store store, OnGetTicketListener onGetTicketListener);
    public void checkTicket(Ticket ticket, OnCheckTicketListener onCheckTicketListener);
    public void cancelTicket(Store store, Ticket ticket, OnTaskCompleteListener onTaskCompleteListener);
}
