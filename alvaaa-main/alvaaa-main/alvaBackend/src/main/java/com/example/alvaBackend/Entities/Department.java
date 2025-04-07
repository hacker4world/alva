package com.example.alvaBackend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int department_id;
    private String name;

    //@OneToMany(mappedBy = "department")(points to the field in Employee)
    //we don't have a department field in user so each logic will be seperated
    //with a join table with two foreign keys
    @OneToMany(mappedBy = "department")
    @JsonIgnore 
    private List<User> users = new ArrayList<>();


    public Department() {
    }

    public Department(String name) {
        this.name = name;
    }

    public Department(int department_id, String name, List<User> users) {
        this.department_id = department_id;
        this.name = name;
        this.users = users;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
