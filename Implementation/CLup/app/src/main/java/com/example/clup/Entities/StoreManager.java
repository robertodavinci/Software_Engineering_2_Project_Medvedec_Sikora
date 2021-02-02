package com.example.clup.Entities;

public class StoreManager extends User {
    private Store store;

    public StoreManager(String name, String surname, String email, Store store){
        super(name, surname, email);
        this.store = store;
    }

    public Store getStore() {
        return store;
    }
    public void setStore(Store store) {
        this.store = store;
    }
}
