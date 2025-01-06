package com.example.repcommissiontracker.Classes;

public class SalesRepresentative {
    private int id;
    private String name;
    private String imagePath; // Store the path to the image
    private String phoneNumber;
    private String startDate;
    private int supervisedLocId;

    public SalesRepresentative(int id, String name, String imagePath, String phoneNumber, String startDate, int supervisedLocId) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.supervisedLocId = supervisedLocId;
    }

    public SalesRepresentative() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getSupervisedLocId() {
        return supervisedLocId;
    }

    public void setSupervisedLocId(int supervisedLocId) {
        this.supervisedLocId = supervisedLocId;
    }
}
