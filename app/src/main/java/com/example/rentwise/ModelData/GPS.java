package com.example.rentwise.ModelData;

public class GPS {

    private String idVehivcle;
    private String latitude;
    private String longtitude;

    public GPS() {
    }

    public GPS(String idVehivcle, String latitude, String longtitude) {
        this.idVehivcle = idVehivcle;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public String getIdVehivcle() {
        return idVehivcle;
    }

    public void setIdVehivcle(String idVehivcle) {
        this.idVehivcle = idVehivcle;
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
