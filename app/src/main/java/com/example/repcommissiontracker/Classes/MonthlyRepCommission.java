package com.example.repcommissiontracker.Classes;

public class MonthlyRepCommission {
    private int id;
    private String month;
    private double commissionValue;
    private int repId;
    private int locId;
    public MonthlyRepCommission(int id, String month, double commissionValue, int repId, int locId) {
        this.id = id;
        this.month = month;
        this.commissionValue = commissionValue;
        this.repId = repId;
        this.locId = locId;
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

    public void setMonth(String month) {
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
