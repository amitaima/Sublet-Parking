package com.subletparking.subletparking;
/**
 * Created by User on 20-Dec-17.
 */

public class Parking {

    int userId;
    int longitude;
    int latitude;
    String address;
    String hours;
    int costPerHour;
    int rating;
    int numberOfRaters;

    public int getUserId() {
        return userId;
    }
    public int getLongitude() {
        return longitude;
    }
    public int getLatitude() {
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
    public int getRating() { return rating; }

}
