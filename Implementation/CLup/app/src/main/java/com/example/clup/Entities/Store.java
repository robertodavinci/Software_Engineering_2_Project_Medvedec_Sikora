package com.example.clup.Entities;

public class Store {
    public String name, address, city;
    public int maxNoCustomers, id;

    public Store(){}
    public Store(int id, String name, String city){
        this.id = id;
        this.name = name;
        this.city = city;
    }
    public Store(int id, String name, String address, String city){
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
    }
    public Store(int id, String name, String address, String city, int maxNoCustomers){
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.maxNoCustomers = maxNoCustomers;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getMaxNoCustomers() {
        return maxNoCustomers;
    }
}
