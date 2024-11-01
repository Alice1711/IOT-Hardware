package com.example.rentwise.ModelData;

import java.util.Date;

public class Rent {

    private String id_rent;
    private String idCustomer;
    private String idMotobike;
    private Date startDay;
    private Date endDay;
    private String zoneRent;

    public Rent(String id_rent, String idCustomer, String idMotobike, Date startDay, Date endDay, String zoneRent) {
        this.id_rent = id_rent;
        this.idCustomer = idCustomer;
        this.idMotobike = idMotobike;
        this.startDay = startDay;
        this.endDay = endDay;
        this.zoneRent = zoneRent;
    }

    public String getId_rent() {
        return id_rent;
    }

    public void setId_rent(String id_rent) {
        this.id_rent = id_rent;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdMotobike() {
        return idMotobike;
    }

    public void setIdMotobike(String idMotobike) {
        this.idMotobike = idMotobike;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public String getZoneRent() {
        return zoneRent;
    }

    public void setZoneRent(String zoneRent) {
        this.zoneRent = zoneRent;
    }
}
