package com.kn0en.ngulirapp.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

@SuppressWarnings({"ALL", "unused"})
public class Restaurant implements Serializable {

    private String id;
    private String nameResto;
    private String address;
    private String phone;
    private String description;
    private String imageRestoUrl;
    private String key;

    public Restaurant() {

    }

    public Restaurant(String nameResto, String address, String phone, String description) {
        this.nameResto = nameResto;
        this.address = address;
        this.phone = phone;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameResto() {
        return nameResto;
    }

    public void setNameResto(String nameResto) {
        this.nameResto = nameResto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getImageRestoUrl() {
//        return imageRestoUrl;
//    }
//
//    public void setImageRestoUrl(String imageRestoUrl) {
//        this.imageRestoUrl = imageRestoUrl;
//    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
