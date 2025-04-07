package com.example.alvaBackend.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.List;


@Entity
@DiscriminatorValue("manager")
public class Manager extends User{


    public Manager() {
    }

    public Manager(String cin, String matriculeNumber, String firstName, String lastName, String email,String password,  String adress, String phoneNumber, String status) {
        super(cin, matriculeNumber, firstName, lastName,email,password, adress, phoneNumber, status);
    }



}
