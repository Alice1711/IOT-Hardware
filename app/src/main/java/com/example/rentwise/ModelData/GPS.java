package com.example.rentwise.ModelData;

public class GPS {

    private String idVehicle;
    private String latitude;
    private String longitude;

    public GPS() {
    }

    public GPS(String idVehicle, String latitude, String longitude) {
        this.idVehicle = idVehicle;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
