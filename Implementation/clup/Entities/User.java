package com.example.clup.Entities;

public class User {
    public String name, surname, email;
    public UserType userType = UserType.STORE_MANAGER;

    public User(){}
    public User(String name, String surname, String email){
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
}
