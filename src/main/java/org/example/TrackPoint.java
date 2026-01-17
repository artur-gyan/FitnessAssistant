package org.example;

import java.time.ZonedDateTime;

public class TrackPoint {
    private ZonedDateTime timeStamp;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double distanceMeters;
    private Integer heartRate;
    private Integer runCadence;
    private Double speed;


    public TrackPoint() {
    }

    /// getters
    public ZonedDateTime getTimestamp() { return timeStamp; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Double getAltitude() { return altitude; }
    public Double getDistanceMeters() { return distanceMeters; }
    public Integer getHeartRate() { return heartRate; }
    public Integer getRunCadence() { return runCadence; }
    public Double getSpeed() { return speed; }


    /// setters
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }
    public void setDistanceMeters(Double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }
    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }
    public void setRunCadence(Integer runCadence) {
        this.runCadence = runCadence;
    }
    public void setSpeed(Double speed) {
        this.speed = speed;
    }
    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }




}