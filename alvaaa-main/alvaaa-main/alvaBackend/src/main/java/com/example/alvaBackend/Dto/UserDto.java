package com.example.alvaBackend.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Lob;
import org.springframework.core.SpringVersion;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class UserDto {
    private int id;
    private String cin;
    private String matriculeNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String adress;
    private String phoneNumber;
    private String status;
    private String accountType;
    @Lob
    private MultipartFile Image;
    //
    private int department_id;

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    //

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatriculeNumber() {
        return matriculeNumber;
    }

    public void setMatriculeNumber(String matriculeNumber) {
        this.matriculeNumber = matriculeNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }


    public String getCIN() {
        return cin;
    }

    public void setCIN(String cin) {
        this.cin = cin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public MultipartFile getImage() {
        return Image;
    }

    public void setImage(MultipartFile image) {
        Image = image;
    }

}
