package com.example.rentwise.ModelData;

public class Motobike {

    private String numberPlate;
    private String name;
    private String picture;
    private String status;
    private String price;

    public Motobike() {}



    public Motobike(String name, String numberPlate, String picture, String status, String price) {
        this.name = name;
        this.numberPlate = numberPlate;
        this.picture = picture;
        this.status = status;
        this.price = price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
