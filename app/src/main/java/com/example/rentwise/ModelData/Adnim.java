package com.example.rentwise.ModelData;

import java.util.Date;

public class Adnim {
    private String Email;
    private String Password;
    private String fullName;
    private String address;
    private String phoneNumber;
    private Date birthDay;
    private String gender;

    public Adnim(String email, String password) {
        Email = email;
        Password = password;
    }

    public boolean SetProfile(String Email, String fullName, String address, String phoneNumber, Date birthDay, String gender) {
        if(getEmail().equals(Email)){
            this.fullName = fullName;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.birthDay = birthDay;
            this.gender = gender;
            return true;
        }
        return false;
    }

    public boolean UpdatePassword(String email, String newPassword) {
        if (getEmail().equals(email)) {
            this.Password = newPassword;
            return true;
        }
        return false;
    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
