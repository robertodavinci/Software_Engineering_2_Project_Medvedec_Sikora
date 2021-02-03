package com.example.clup.Services;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.Timeslot;
import com.example.clup.OnCredentialCheckListener;
import com.example.clup.OnGetDataListener;

import java.util.List;

public interface DatabaseManagerService {
    public void getStoreCities(OnGetDataListener onGetDataListener);
    public void getStores(String city, OnGetDataListener onGetDataListener);
    public void getStoreOcupancy(Store store, OnGetDataListener onGetDataListener);
    public void getStoreMaxNoCustomers(Store store, OnGetDataListener onGetDataListener);
    public void getStoreAddresses(String city, String name, OnGetDataListener onGetDataListener);
    public void getTickets(Store store, OnGetDataListener onGetDataListener);
    public void getMaxTicketId(Store store, OnGetDataListener onGetDataListener);
    public void checkCredentials(String email, String password, OnCredentialCheckListener onCredentialCheckListener);
    public void getTicket(Store store, String ticketId, OnGetDataListener onGetDataListener);

    public void persistExit(Store store);
    public void persistEnter(Store store, Ticket ticket);
    public void persistTicket(Ticket ticket);
}
