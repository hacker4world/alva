package com.example.alvaBackend.Dto;

import com.example.alvaBackend.Entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDto {

    private int department_id;
    private String name;
    private List<User> users = new ArrayList<>();


    public DepartmentDto() {
    }

    public DepartmentDto(String name) {
        this.name = name;
    }

    public DepartmentDto(String name, List<User> users) {
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

