package com.example.alvaBackend.Dto;

import jakarta.persistence.Lob;
import org.springframework.web.multipart.MultipartFile;

public class UpdateUserDto {
    private int id;
    private String matriculeNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String adress;
    private String CIN;
    private String phoneNumber;
    private String status;
    private String accountType;
    @Lob
    private MultipartFile Image;

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
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public MultipartFile getImage() {
        return Image;
    }

    public void setImage(MultipartFile image) {
        Image = image;
    }
}
