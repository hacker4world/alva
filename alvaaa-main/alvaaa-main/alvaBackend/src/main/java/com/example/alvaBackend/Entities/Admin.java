package com.example.alvaBackend.Entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity
@DiscriminatorValue("admin")
public class Admin extends User{


    public Admin() {
    }

    public Admin(String cin, String matriculeNumber, String firstName, String lastName,  String email,String password, String adress, String phoneNumber, String status) {
        super(cin, matriculeNumber, firstName, lastName,  email,password, adress, phoneNumber, status);
    }
}
