package com.example.rentwise.ModelData;

public class GPS {

    private String idVehicle;
    private String latitude;
    private String longtitude;

    public GPS() {
    }

    public GPS(String idVehicle, String latitude, String longtitude) {
        this.idVehicle = idVehicle;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public String getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(String idVehicle) {
        this.idVehicle = idVehicle;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
}
