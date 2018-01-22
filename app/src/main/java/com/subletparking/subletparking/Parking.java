package com.subletparking.subletparking;
/**
 * Created by User on 20-Dec-17.
 */

public class Parking {

    private long userId;
    private double longitude;
    private double latitude;
    private String address;
    private String hours;
    private int costPerHour;
    private int ratings;
    private int numberOfRaters;

    public Parking(long id, double l1, double l2, String add, String time, int cost, int rate, int raters) {
        userId = id;
        longitude = l1;
        latitude = l2;
        address = add;
        hours = time;
        costPerHour = cost;
        ratings = rate;
        numberOfRaters = raters;
    }

    public long getUserId() {
        return userId;
    }
    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public String getAddress() { return address; }
    public String getHours() { return hours; }
    public int getCostPerHour() {
        return costPerHour;
    }
    public int getNumberOfRaters() {
        return numberOfRaters;
    }
    public int getRating() { return ratings; }

}
