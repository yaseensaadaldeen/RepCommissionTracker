package com.example.repcommissiontracker.Classes;

public class CommissionConfig {
    // Static commission values
    public static final double SUPERVISED_LOCATION_RATE_LOW = 0.05;  // 5% for supervised location
    public static final double SUPERVISED_LOCATION_RATE_HIGH = 0.07; // 7% for amount over 100 million

    public static final double NON_SUPERVISED_LOCATION_RATE_LOW = 0.03;  // 3% for non-supervised location
    public static final double NON_SUPERVISED_LOCATION_RATE_HIGH = 0.04; // 4% for amount over 100 million

    public static final double COMMISSION_THRESHOLD = 100_000_000;  // 100 million threshold
}
