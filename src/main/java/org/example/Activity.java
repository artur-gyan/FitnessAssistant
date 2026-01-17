package org.example;

import java.util.ArrayList;


public interface Activity {

    // Getters / Setters
    String getId();
    void setId(String id);
    String getSport();
    void setSport(String sport);

    // Διαχείριση Laps
    void addLap(Lap lap);
    ArrayList<Lap> getLaps();

    // Υπολογισμοί
    double calculateCalories(User user);
    double calculateTotalDistanceOfLaps();
    double getTotalDistanceKm();
    double calculateTotalTimeSecondsOfLaps();
    String getFormattedTotalTime();

    double calculateAverageSpeedKmH();
    String calculateAveragePaceMinKm();

    double getMaxSpeedOfLaps();
    String getBestPace();

    double getMinSpeedOfLaps();
    String getMinPace();

    int calculateAverageHeartRateOfLaps();
    int calculateMaxHeartRateOfLaps();
}