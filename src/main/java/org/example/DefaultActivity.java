package org.example;

import java.util.ArrayList;

public class DefaultActivity implements Activity{
    private String sport;
    private String id;
    protected double multiplier = 5.0;
    private ArrayList<Lap> laps;

    public DefaultActivity(String sport, String id) {
        laps = new ArrayList<>();
        this.id = id;
        this.sport = sport;

    }
    @Override
    public void addLap(Lap lap) {
        laps.add(lap);
    }

    @Override
    public ArrayList<Lap> getLaps() {
        return laps;
    }

    @Override
    public String getId() { return id; }
    @Override
    public String getSport() { return sport; }
    @Override
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public void setSport(String sport) {
        this.sport = sport;
    }


    /// Υπολογισμος Θερμιδων
    @Override
    public double calculateCalories(User user) {
        int avgHr = calculateAverageHeartRateOfLaps();
        String gender = user.getGender();
        int age = user.getAge();

        // ΕΛΕΓΧΟΙ: Για τον ακριβή τύπο χρειαζόμαστε Παλμούς, Φύλο ΚΑΙ Ηλικία
        boolean hasHeartRate = avgHr > 0;

        // Έλεγχος Φύλου (Male ή Female)
        boolean hasValidGender = gender != null &&
                (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"));

        /// Έλεγχος Ηλικίας (Πρέπει να είναι > 0)
        boolean hasValidAge = age > 0;

        /// ΠΕΡΙΠΤΩΣΗ A: Έχουμε ΤΑ ΠΑΝΤΑ  Ακριβής Τύπος (Heart Rate Formula)
        if (hasHeartRate && hasValidGender && hasValidAge) {
            double tMinutes = calculateTotalTimeSecondsOfLaps() / 60.0;
            double w = user.getWeight();
            int h = avgHr;

            if ("Male".equalsIgnoreCase(gender)) {
                /// Τύπος Ανδρών
                return ((-55.0969 + (0.6309 * h) + (0.1966 * w) + (0.2017 * age)) / 4.184) * tMinutes;
            } else {
                /// Τύπος Γυναικών
                return ((-20.4022 + (0.4472 * h) + (0.1263 * w) + (0.074 * age)) / 4.184) * tMinutes;
            }
        }

        // ΠΕΡΙΠΤΩΣΗ B: Λείπει κάτι (Παλμοί, Φύλο ή Ηλικία) -> Απλός Τύπος (METs)
        else {
            // Εδώ χρειαζόμαστε ΜΟΝΟ βάρος και χρόνο (σε ώρες)
            double tHours = calculateTotalTimeSecondsOfLaps() / 3600.0;
            return multiplier * user.getWeight() * tHours;
        }
    }
    /// Υπολογισμος αποστασης του Activity
    @Override
    public double calculateTotalDistanceOfLaps() {
        double totalDistance = 0.0;
        for (Lap lap : laps) {
            totalDistance += lap.calculateTotalDistanceOfTracks();
        }
        return totalDistance;
    }
    /// Υπολογισμος αποστασης του Activity σε km
    @Override
    public double getTotalDistanceKm() {
        // Παίρνουμε τα μέτρα και διαιρούμε με το 1000
        return calculateTotalDistanceOfLaps() / 1000.0;
    }

    /// Υπολογισμος χρονου του Activity
    @Override
    public double calculateTotalTimeSecondsOfLaps() {
        double totalTime = 0.0;
        for (Lap lap : laps) {
            totalTime += lap.calculateTotalTimeSecondsOfTracks();
        }
        return totalTime;
    }
    /// Επιστρέφει τον συνολικό χρόνο μορφοποιημένο (π.χ. "1:45:30" ή "45:30")
    @Override
    public String getFormattedTotalTime() {
        double totalSeconds = calculateTotalTimeSecondsOfLaps();

        long hours = (long) (totalSeconds / 3600);
        long minutes = (long) ((totalSeconds % 3600) / 60);
        long seconds = (long) (totalSeconds % 60);

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    /// Υπολογισμος μεσης ωριαίας ταχύτητας σε km/h
    @Override
    public double calculateAverageSpeedKmH() {
        double totalDistanceMeters = calculateTotalDistanceOfLaps();
        double totalTimeSeconds = calculateTotalTimeSecondsOfLaps();
        if (totalTimeSeconds > 0) {
            double speedMetersPerSecond = totalDistanceMeters / totalTimeSeconds;
            /// 3600 seconds in an hour και 1000 meters in a kilometer = 3.6
            return speedMetersPerSecond * 3.6;
        }
        return 0.0;
    }

    /// Υπολογισμος μεσης μέση ταχύτητα ανά χιλιόμετρο min/km
    @Override
    public String calculateAveragePaceMinKm() {
        double totalDistanceKm = getTotalDistanceKm();
        double totalTimeMinutes = calculateTotalTimeSecondsOfLaps() / 60.0;

        /// Pace = Χρόνος / Απόσταση
        if (totalDistanceKm > 0) {
            double pace = totalTimeMinutes / totalDistanceKm;
            return formatPace(pace);
        }
        return "0:00 min/km";
    }


    /// MaxSpeed απο τα Laps
    @Override
    public double getMaxSpeedOfLaps() {
        double maxSpeed = 0.0;
        for ( Lap lap : laps ) {
            double lapMaxSpeed = lap.getMaxSpeedOfTracks();
            if (lapMaxSpeed > maxSpeed) {
                maxSpeed = lapMaxSpeed;
            }
        }
        return maxSpeed;
    }
    ///  Best Pace μετατροπη του Maxspeed σε min/Km (Μέγιστη ταχύτητα ανά χλμ)
    @Override
    public String getBestPace() {
        double maxSpeed = getMaxSpeedOfLaps();
        if (maxSpeed > 0) {
            double pace = (1000.0/maxSpeed)/60.0;
            return formatPace(pace);
        }
        return "0:00 min/km";
    }

    /// MinSpeed apo ta Laps
    @Override
    public double getMinSpeedOfLaps() {
        double minSpeed = Double.MAX_VALUE;
        boolean found = false;
        for ( Lap lap : laps ) {
            double lapMinSpeed = lap.getMinSpeedOfTracks();
            if (lapMinSpeed < minSpeed && lapMinSpeed > 0) {
                minSpeed = lapMinSpeed;
                found = true;
            }

        }
        if (!found) {
            return 0.0;
        }else {
            return minSpeed;
        }
    }
    /// Ελάχιστη ταχύτητα ανά χλμ
    @Override
    public String getMinPace() {
        double minSpeed = getMinSpeedOfLaps();
        if (minSpeed > 0) {
            double pace = (1000.0/minSpeed)/60.0;
            return formatPace(pace);
        }
        return "0:00 min/km";
    }

    /// μέθοδος για μορφοποίηση (π.χ. το 5.5 γίνεται "5:30")
    private String formatPace(double pace) {
        int minutes = (int) pace;
        int seconds = (int) ((pace - minutes) * 60);
        return String.format("%d:%02d min/km", minutes, seconds);
    }

    /// Average HeartRate
    @Override
    public int calculateAverageHeartRateOfLaps() {
        int totalHr = 0;
        int count = 0;
        for (Lap lap : laps){
            int lapAvgHR = lap.calculateAverageHeartRateOfTracks();
            if (lapAvgHR > 0) {
                totalHr += lapAvgHR;
                count++;
            }
        }
        if (count > 0) {
            return totalHr/count;
        }
        return 0;
    }

    /// Max HeartRate
    @Override
    public int calculateMaxHeartRateOfLaps() {
        int maxHR = 0;
        for (Lap lap : laps){
            int lapMaxHR = lap.calculateMaxHeartRateOfTracks();
            if (lapMaxHR > maxHR) {
                maxHR = lapMaxHR;
            }
        }
        return maxHR;
    }






}
