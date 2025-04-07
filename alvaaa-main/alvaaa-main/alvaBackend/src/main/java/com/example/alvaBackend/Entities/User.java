package com.example.alvaBackend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Role")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String cin;
    private String matriculeNumber;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    private String adress;
    private String phoneNumber;
    private String status;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] Image;
    @JsonIgnore
    private String code;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    public User(){

    }

    public User(String cin, String matriculeNumber, String firstName, String lastName, String email, String password, String adress, String phoneNumber, String status, byte[] image, String code, Department department) {
        this.cin = cin;
        this.matriculeNumber = matriculeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.status = status;
        Image = image;
        this.code = code;
        this.department = department;
    }

    public User(String cin, String matriculeNumber, String firstName, String lastName, String email, String password, String adress, String phoneNumber, String status) {
        this.cin = cin;
        this.matriculeNumber = matriculeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public User(String cin, String matriculeNumber, String firstName, String lastName, String email, String adress, String phoneNumber, String status) {
        this.cin = cin;
        this.matriculeNumber = matriculeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    //Constructor with code attribute
    public User(int id, String cin, String matriculeNumber,
                String firstName, String lastName, String email,
                String password, String adress, String phoneNumber,
                String status, byte[] image, String code) {

        this.id = id;
        this.cin = cin;
        this.matriculeNumber = matriculeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.status = status;
        Image = image;
        this.code = code;

    }

    //Constructor with department attribute

    public User(int id, String cin, String matriculeNumber, String firstName,
                String lastName, String email, String password,
                String adress, String phoneNumber, String status,
                byte[] image, String code, Department department) {
        this.id = id;
        this.cin = cin;
        this.matriculeNumber = matriculeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.status = status;
        Image = image;
        this.code = code;
        this.department = department;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;}


    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getMatriculeNumber() {
        return matriculeNumber;
    }

    public void setMatriculeNumber(String matriculeNumber) {
        this.matriculeNumber = matriculeNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }
}