package org.example;

import java.util.ArrayList;

public class Track {
    private ArrayList<TrackPoint> trackPoints;

    public Track() {
        trackPoints = new ArrayList<>();
    }
    public void addTrackPoint(TrackPoint trackpoint) {
        trackPoints.add(trackpoint);
    }
    public ArrayList<TrackPoint> getTrackpoints() {
        return trackPoints;
    }

    /// Εδώ υπολογίζουμε πόση απόσταση έχει γίνει στο track. πχ 2510-1500=1010
    public double calculateTotalDistanceofTrackPOints() {
        if (trackPoints == null || trackPoints.isEmpty()) {
            return 0.0;
        }
        TrackPoint firstPoint = trackPoints.get(0);
        TrackPoint lastPoint = trackPoints.get(trackPoints.size() - 1);
        if (lastPoint != null && lastPoint.getDistanceMeters() != null) {
            double lastDistance = lastPoint.getDistanceMeters();

            // Αν το πρώτο σημείο δεν έχει απόσταση, θεωρούμε ότι είναι 0.0
            double firstDistance = 0.0;
            if (firstPoint != null && firstPoint.getDistanceMeters() != null) {
                firstDistance = firstPoint.getDistanceMeters();
            }

            return lastDistance - firstDistance;
        }

        return 0.0;
    }

    /// Υπολογισμος χρονου του Track
    public double calculateTotalTimeSeconds() {
        if (trackPoints == null || trackPoints.isEmpty()) {
            return 0.0;
        }
        TrackPoint firstPoint = trackPoints.get(0);
        TrackPoint lastPoint = trackPoints.get(trackPoints.size() - 1);

        if (firstPoint != null && lastPoint != null ) {
            long durationSeconds = lastPoint.getTimestamp().toEpochSecond() - firstPoint.getTimestamp().toEpochSecond();
            return (double) durationSeconds;
        }
        return 0.0;
    }

    ///  MaxSpeed των trackPoints
    public double getMaxSpeed(){
        double maxSpeed = 0.0;
        if (trackPoints != null) {
            for (TrackPoint tp : trackPoints) {
                if (tp.getSpeed() != null && tp.getSpeed() > maxSpeed) {
                    maxSpeed = tp.getSpeed();
                }
            }
        }
       return maxSpeed;
    }

    /// MinSpeed
    public double getMinSpeed(){
        double minSpeed = Double.MAX_VALUE;
        boolean found = false;
        if (trackPoints != null) {
            for (TrackPoint tp : trackPoints) {
                /// Ψάχνουμε ταχύτητες που υπάρχουν και είναι μεγαλύτερες από 0
                if (tp.getSpeed() != null && tp.getSpeed() > 0 && tp.getSpeed() < minSpeed) {
                    minSpeed = tp.getSpeed();
                    found = true;
                }
            }
        }
        if (!found) {
            return 0.0;
        }else {
            return minSpeed;
        }
    }

    /// AverageHeartRate
    public int calculateAverageHeartRate() {
        if (trackPoints == null || trackPoints.isEmpty()) {
            return 0;
        }

        int sum = 0;
        int count = 0;

        for (TrackPoint tp : trackPoints) {
            // Ελέγχουμε αν υπάρχει τιμή και αν είναι μεγαλύτερη από το 0
            if (tp.getHeartRate() != null && tp.getHeartRate() > 0) {
                sum += tp.getHeartRate();
                count++;
            }
        }

        if (count > 0) {
            return (int) (sum / count);
        }
        return 0;
    }
    /// MaxHeartRate
    public int calculateMaxHeartRate() {
        int maxHR = 0;
        if (trackPoints == null || trackPoints.isEmpty()) {
            return 0;
        }
        for (TrackPoint tp : trackPoints) {
            if (tp.getHeartRate() != null && tp.getHeartRate() > maxHR) {
                maxHR = tp.getHeartRate();
            }
        }
        return maxHR;
    }





}
