package com.example.clup.Services.Implementation;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Services.EnterService;
import com.example.clup.Services.ExitService;

public class StoreManager implements EnterService, ExitService {
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    @Override
    public boolean manageEntrance(String qrCodeText) {
        //TODO: parse qrCodeText/check
        String city = qrCodeText.split(";")[0];
        String name = qrCodeText.split(";")[1];
        String address = qrCodeText.split(";")[2];
        String storeId  = qrCodeText.split(";")[3];
        String ticketId = qrCodeText.split(";")[4];
        Store store = new Store(Integer.getInteger(storeId), name, address, city);
        Ticket ticket = databaseManager.getTicket(store, ticketId);
        if(databaseManager.getStoreOcupancy(ticket.getStore()).size()>0){
            if(databaseManager.getStoreOcupancy(ticket.getStore()).get(0)<ticket.getStore().maxNoCustomers) {
                //TODO: persist change in occupancy
                return true;
            }
        }
        return false;
    }

    @Override
    public void manageExit() {
        //TODO: persist change in occupancy
    }

}
