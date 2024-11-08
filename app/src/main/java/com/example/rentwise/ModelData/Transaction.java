package com.example.rentwise.ModelData;

import com.google.firebase.database.Exclude;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

    private String id_rent;
    private String idCustomer;
    private String idMotobike;
    private String startDay;
    private String endDay;
    private String zoneRent;

    public Transaction() {}

    public Transaction(String id_rent, String idCustomer, String idMotobike, String startDay, String endDay, String zoneRent) {
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

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getZoneRent() {
        return zoneRent;
    }

    public void setZoneRent(String zoneRent) {
        this.zoneRent = zoneRent;
    }

    @Exclude
    public Date getStartDayAsDate() throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(this.startDay);
    }

    @Exclude
    public Date getEndDayAsDate() throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(this.endDay);
    }
}
