package com.kn0en.ngulirapp.models;

public class UserInfo {
    public String nameUser;
    public String bdUser;
    public String phoneUser;
    public String addressUser;

    public UserInfo() {
    }

    public UserInfo(String nameUser, String bdUser, String phoneUser, String addressUser) {
        this.nameUser = nameUser;
        this.bdUser = bdUser;
        this.phoneUser = phoneUser;
        this.addressUser = addressUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getBdUser() {
        return bdUser;
    }

    public void setBdUser(String bdUser) {
        this.bdUser = bdUser;
    }

    public String getAddressUser() {
        return addressUser;
    }

    public void setAddressUser(String addressUser) {
        this.addressUser = addressUser;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }
}
