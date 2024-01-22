package com.example.galleryapp.model;

public class UserModel {
    String name;
    String surname;
    String email;
    String userid;

    public UserModel(String name, String surname, String email, String userid) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }



}
