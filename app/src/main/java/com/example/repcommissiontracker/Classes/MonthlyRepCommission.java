package com.example.repcommissiontracker.Classes;

public class MonthlyRepCommission {
    private int id;
    private int month;
    private int year;
    private double commissionValue;
    private int repId;
    private int locId;
    public MonthlyRepCommission(int id, int month, double commissionValue, int repId, int locId) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.commissionValue = commissionValue;
        this.repId = repId;
        this.locId = locId;
    }

    public MonthlyRepCommission() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    public double getCommissionValue() {
        return commissionValue;
    }

    public void setCommissionValue(double commissionValue) {
        this.commissionValue = commissionValue;
    }

    public int getRepId() {
        return repId;
    }

    public void setRepId(int repId) {
        this.repId = repId;
    }

    public int getLocId() {
        return locId;
    }

    public void setLocId(int locId) {
        this.locId = locId;
    }
}
