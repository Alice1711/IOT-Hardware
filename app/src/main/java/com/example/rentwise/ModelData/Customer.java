package com.example.rentwise.ModelData;

public class Customer {

    private String cccd;
    private String phoneNumber;
    private String name;
    private String gender;

    public Customer() {
    }

    public Customer(String cccd, String phoneNumber, String name, String gender) {
        this.cccd = cccd;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.gender = gender;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
