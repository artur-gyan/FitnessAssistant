package org.example;

import java.util.ArrayList;
import java.time.ZonedDateTime;

public class Lap {
    private ZonedDateTime startTime;
    private Double totalTimeSeconds;
    private Double distanceMeters;
    private Double maximumSpeed;
    private Integer calories;
    private String intensity;
    private String triggerMethod;
    private ArrayList<Track> tracks;
    private Integer averageHeartRate;
    private Integer maximumHeartRate;

    public Lap() {
        tracks = new ArrayList<>();
    }

    public void setAverageHeartRate(Integer averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }
    public void setMaximumHeartRate(Integer maximumHeartRate) {
        this.maximumHeartRate = maximumHeartRate;
    }
    public void addTrack(Track track) {
        tracks.add(track);
    }
    public ArrayList<Track> getTracks() {
        return tracks;
    }
    public void setCalories(Integer calories) {
        this.calories = calories;
    }
    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }
    public void setMaximumSpeed(Double maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }
    public void setTriggerMethod(String triggerMethod) {
        this.triggerMethod = triggerMethod;
    }
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }
    public void setTotalTimeSeconds(Double totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
    }
    public void setDistanceMeters(Double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public Integer getAverageHeartRate() {
        return averageHeartRate;
    }
    public Integer getMaximumHeartRate() {
        return maximumHeartRate;
    }
    public Integer getCalories() {
        return calories;
    }
    public String getIntensity() {
        return intensity;
    }
    public String getTriggerMethod() {
        return triggerMethod;
    }
    public Double getMaximumSpeed() {
        return maximumSpeed;
    }
    public Double getDistanceMeters() {
        return distanceMeters;
    }
    public ZonedDateTime getStartTime() {
        return startTime;
    }
    public Double getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    /// Εδώ αθροίζουμε τις αποστάσεις που έχουν γίνει σε κάθε track, για να βρούμε τη συνολική απόσταση.
    // Αρχείο: Lap.java

    public double calculateTotalDistanceOfTracks() {
        // Αν το αρχείο XML μας έδωσε έτοιμη την απόσταση του Lap το χρησιμοποιηουμε
        if (this.distanceMeters != null && this.distanceMeters > 0) {
            return this.distanceMeters;
        }

        // Αλλιώς (αν είναι 0 ή null), υπολόγιζουμε  από τα TrackPoints
        double totalDistance = 0.0;
        for (Track track : tracks) {
            totalDistance += track.calculateTotalDistanceofTrackPOints();
        }
        return totalDistance;
    }

    /// Υπολογισμος χρονου του Lap
    public double calculateTotalTimeSecondsOfTracks() {
        double totalTime = 0.0;
        for (Track track : tracks) {
            totalTime += track.calculateTotalTimeSeconds();
        }
        return totalTime;
    }

    ///  maxSpeed απο τα  Tracks
    public double getMaxSpeedOfTracks() {
        double maxSpeed = 0.0;
        for (Track track : tracks) {
            if (track.getMaxSpeed() > maxSpeed) {
                maxSpeed = track.getMaxSpeed();
            }
        }
        return maxSpeed;
    }

    /// MinSpeed Των Tracks
    public double getMinSpeedOfTracks() {
        double minSpeed = Double.MAX_VALUE;
        boolean found = false;
        for (Track track : tracks) {
            double trackMinSpeed = track.getMinSpeed();
            if (trackMinSpeed > 0 && trackMinSpeed < minSpeed){
                minSpeed = trackMinSpeed;
                found = true;
            }
        }
        if (!found) {
            return 0.0;
        }else {
            return minSpeed;
        }
    }

    /// Avg HeartRate of Tracks
    public int calculateAverageHeartRateOfTracks() {
        int totalHR = 0;
        int count = 0;
        for (Track track : tracks) {
            int trackAvgHR = track.calculateAverageHeartRate();
            if (trackAvgHR > 0) {
                totalHR += trackAvgHR;
                count++;
            }
        }
        if (count > 0) {
            return totalHR/count;
        }
        return 0;
    }

    /// Max HeartRate of Tracks
    public int calculateMaxHeartRateOfTracks() {
        int maxHR = 0;
        for (Track track : tracks) {
            int trackMaxHR = track.calculateMaxHeartRate();
            if (trackMaxHR > maxHR) {
                maxHR = trackMaxHR;
            }
        }
        return maxHR;
    }






}

