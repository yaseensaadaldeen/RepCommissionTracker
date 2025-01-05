package com.example.repcommissiontracker.Classes;

public class Location {
    private int locId;
    private String name;
    private String address;


    public Location(int locId, String name, String address) {
        this.locId = locId;
        this.name = name;
        this.address = address;
    }

    public int getLocId() {
        return locId;
    }

    public void setLocId(int locId) {
        this.locId = locId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
