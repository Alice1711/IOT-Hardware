package com.example.rentwise.ModelData;

public class Motobike {

    private String numberPlate;
    private String name;
    private String picture;
    private String status;

    public Motobike(String numberPlate, String name, String picture, String status) {
        this.numberPlate = numberPlate;
        this.name = name;
        this.picture = picture;
        this.status = status;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
