package com.example.clup.Entities;

public class User {
    public String name, surname, email; // variables that define every User
    public UserType userType = UserType.STORE_MANAGER; // every User is store manager

    // User constructors
    public User(){}
    public User(String name, String surname, String email){
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
}
