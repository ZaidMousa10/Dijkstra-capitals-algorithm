package com.example.algo_proj3;

public class Capitals {
    private String capitalName;
    private double longitude;
    private double latitude;
    private double x;
    private double y;

    public Capitals(String capitalName, double longitude, double latitude) {
        setCapitalName(capitalName);
        setLatitude(latitude);
        setLongitude(longitude);
    }
    public Capitals(String capitalName, double latitude, double longitude, double x, double y) {
        this.capitalName = capitalName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.x = x;
        this.y = y;
    }


    public String getCapitalName() {
        return capitalName;
    }
    public void setCapitalName(String countryName) {
        this.capitalName = countryName;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

}
