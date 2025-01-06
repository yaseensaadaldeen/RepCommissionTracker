package com.example.repcommissiontracker.Classes;

public class Invoice {
    private int invNo;
    private String createdDate;
    private double totalPrice;
    private int salesRepId;
    private int locId;

    public Invoice(int invNo, String createdDate, double totalPrice, int salesRepId, int locId) {
        this.invNo = invNo;
        this.createdDate = createdDate;
        this.totalPrice = totalPrice;
        this.salesRepId = salesRepId;
        this.locId = locId;
    }

    public Invoice() {

    }

    public int getInvNo() {
        return invNo;
    }

    public void setInvNo(int invNo) {
        this.invNo = invNo;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getSalesRepId() {
        return salesRepId;
    }

    public void setSalesRepId(int salesRepId) {
        this.salesRepId = salesRepId;
    }

    public int getLocId() {
        return locId;
    }

    public void setLocId(int locId) {
        this.locId = locId;
    }
}
