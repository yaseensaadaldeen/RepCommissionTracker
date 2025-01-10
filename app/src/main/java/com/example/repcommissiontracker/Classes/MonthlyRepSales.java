package com.example.repcommissiontracker.Classes;

public class MonthlyRepSales {
    private int id;
    private String month;
    private double salesValue;
    private int repId;
    private int locId;
    private int year;

    public MonthlyRepSales(int id, String month, double salesValue, int repId, int locId,int year) {
        this.id = id;
        this.month = month;
        this.salesValue = salesValue;
        this.repId = repId;
        this.locId = locId;
        this.year = year;

    }

    public MonthlyRepSales() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }
    public int getYear() {
        return year;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public double getSalesValue() {
        return salesValue;
    }

    public void setSalesValue(double salesValue) {
        this.salesValue = salesValue;
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
