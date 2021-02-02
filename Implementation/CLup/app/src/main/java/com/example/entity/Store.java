package com.example.entity;

public class Store {
    public String name, address;
    private Integer id;

    public Store(){}

    public Store(String name, String address, Integer id){
        this.name = name;
        this.address = address;
        this.id= id;
    }
}
