package com.example.clup.Entities;

public class StoreManager extends User {
    private Store store; // StoreManager is has a store which he "belongs to"

    // StoreManager constructor
    public StoreManager(String name, String surname, String email, Store store){
        super(name, surname, email);
        this.store = store;
    }

    // StoreManager getter and setter
    public Store getStore() {
        return store;
    }
    public void setStore(Store store) {
        this.store = store;
    }
}
