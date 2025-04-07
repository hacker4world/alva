package com.example.alvaBackend.Entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity
@DiscriminatorValue("employee")
public class Employee extends User{

    public Employee() {
    }

    public Employee(String cin, String matriculeNumber, String firstName, String lastName,  String email,String password, String adress, String phoneNumber, String status) {
        super(cin, matriculeNumber, firstName, lastName,  email, password,adress, phoneNumber, status);
    }

    public Employee(String cin, String matriculeNumber, String firstName, String lastName, String email, String adress, String phoneNumber, String status) {
        super(cin, matriculeNumber, firstName, lastName, email, adress, phoneNumber, status);
    }
}
