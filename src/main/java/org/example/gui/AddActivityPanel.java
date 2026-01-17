package org.example.gui;

import org.example.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.IntStream;

public class AddActivityPanel extends JPanel {

    private JComboBox<String> cmbSport;

    // Νέα πεδία για την ημερομηνία
    private JComboBox<Integer> cmbDay;
    private JComboBox<Integer> cmbMonth;
    private JComboBox<Integer> cmbYear;

    private JTextField txtDistance;
    private JTextField txtTimeHours;
    private JTextField txtTimeMinutes;
    private JTextField txtTimeSeconds;
    private JTextField txtHeartRate;
    private JButton btnAdd;

    private UserPanel userPanel;
    private List<Activity> allActivities;

    public AddActivityPanel(UserPanel userPanel, List<Activity> allActivities) {
        this.userPanel = userPanel;
        this.allActivities = allActivities;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- 1. ΕΠΙΛΟΓΗ ΑΘΛΗΜΑΤΟΣ ---
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Άθλημα:"), gbc);
        String[] sports = {"Running", "Biking", "Swimming", "Other"};
        cmbSport = new JComboBox<>(sports);
        gbc.gridx = 1; add(cmbSport, gbc);

        // --- 2. ΕΠΙΛΟΓΗ ΗΜΕΡΟΜΗΝΙΑΣ (ΝΕΟ) ---
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Ημερομηνία:"), gbc);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        // Ημέρες (1-31)
        Integer[] days = IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new);
        cmbDay = new JComboBox<>(days);

        // Μήνες (1-12)
        Integer[] months = IntStream.rangeClosed(1, 12).boxed().toArray(Integer[]::new);
        cmbMonth = new JComboBox<>(months);

        // Έτη (π.χ. 2020 - 2030)
        Integer[] years = IntStream.rangeClosed(2020, 2030).boxed().toArray(Integer[]::new);
        cmbYear = new JComboBox<>(years);

        // Προεπιλογή της σημερινής ημερομηνίας
        LocalDate today = LocalDate.now();
        cmbDay.setSelectedItem(today.getDayOfMonth());
        cmbMonth.setSelectedItem(today.getMonthValue());
        cmbYear.setSelectedItem(today.getYear());

        datePanel.add(cmbDay);
        datePanel.add(new JLabel("/"));
        datePanel.add(cmbMonth);
        datePanel.add(new JLabel("/"));
        datePanel.add(cmbYear);

        gbc.gridx = 1; add(datePanel, gbc);


        // --- 3. ΑΠΟΣΤΑΣΗ ---
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Απόσταση (km):"), gbc);
        txtDistance = new JTextField(10);
        gbc.gridx = 1; add(txtDistance, gbc);

        // --- 4. ΧΡΟΝΟΣ ---
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Διάρκεια (ΩΩ:ΛΛ:ΔΔ):"), gbc);
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        txtTimeHours = new JTextField("00", 2);
        txtTimeMinutes = new JTextField("00", 2);
        txtTimeSeconds = new JTextField("00", 2);
        timePanel.add(txtTimeHours); timePanel.add(new JLabel(":"));
        timePanel.add(txtTimeMinutes); timePanel.add(new JLabel(":"));
        timePanel.add(txtTimeSeconds);
        gbc.gridx = 1; add(timePanel, gbc);

        // --- 5. ΠΑΛΜΟΙ ---
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Μέσοι Παλμοί (Προαιρετικό):"), gbc);
        txtHeartRate = new JTextField(10);
        gbc.gridx = 1; add(txtHeartRate, gbc);

        // --- 6. ΚΟΥΜΠΙ ---
        gbc.gridx = 1; gbc.gridy = 5;
        btnAdd = new JButton("Υπολογισμός & Προσθήκη");
        add(btnAdd, gbc);

        btnAdd.addActionListener(e -> createManualActivity());
    }

    private void createManualActivity() {
        try {
            String sport = (String) cmbSport.getSelectedItem();

            // Έλεγχος απόστασης
            if (txtDistance.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ δώστε απόσταση.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double distanceKm = Double.parseDouble(txtDistance.getText().trim());

            // Έλεγχος χρόνου
            int h = txtTimeHours.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtTimeHours.getText().trim());
            int m = txtTimeMinutes.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtTimeMinutes.getText().trim());
            int s = txtTimeSeconds.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtTimeSeconds.getText().trim());

            long totalSeconds = (h * 3600) + (m * 60) + s;
            if (totalSeconds <= 0) {
                JOptionPane.showMessageDialog(this, "Λάθος χρόνος.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int avgHr = 0;
            if (!txtHeartRate.getText().trim().isEmpty()) {
                avgHr = Integer.parseInt(txtHeartRate.getText().trim());
            }

            // --- ΔΗΜΙΟΥΡΓΙΑ ΗΜΕΡΟΜΗΝΙΑΣ ΑΠΟ ΤΑ DROP-DOWNS ---
            int day = (Integer) cmbDay.getSelectedItem();
            int month = (Integer) cmbMonth.getSelectedItem();
            int year = (Integer) cmbYear.getSelectedItem();

            // Δημιουργούμε το Local Date
            LocalDate selectedDate;
            try {
                selectedDate = LocalDate.of(year, month, day);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Μη έγκυρη ημερομηνία (π.χ. 30 Φεβρουαρίου).", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Συνδυάζουμε την ημερομηνία με την τρέχουσα ώρα (ή π.χ. 12:00 μεσημέρι) για να φτιάξουμε ZonedDateTime
            // Χρησιμοποιούμε τη ζώνη ώρας του συστήματος
            ZonedDateTime startTime = ZonedDateTime.of(selectedDate, LocalTime.now(), ZoneId.systemDefault());
            // ----------------------------------------------------

            ActivityFactory factory = new ActivityFactory();
            // Ως ID βάζουμε την επιλεγμένη ημερομηνία
            String id = selectedDate.toString() + " (Manual)";
            Activity activity = factory.getOrCreateActivity(sport, id);

            Lap lap = new Lap();
            Track track = new Track();
            lap.setStartTime(startTime); // Βάζουμε την επιλεγμένη ώρα έναρξης

            TrackPoint p1 = new TrackPoint();
            p1.setTimeStamp(startTime);
            p1.setDistanceMeters(0.0);
            p1.setHeartRate(avgHr);

            TrackPoint p2 = new TrackPoint();
            p2.setTimeStamp(startTime.plusSeconds(totalSeconds));
            p2.setDistanceMeters(distanceKm * 1000.0);
            p2.setHeartRate(avgHr);

            track.addTrackPoint(p1);
            track.addTrackPoint(p2);
            lap.addTrack(track);
            activity.addLap(lap);

            allActivities.add(activity);

            JOptionPane.showMessageDialog(this, "Η δραστηριότητα προστέθηκε για: " + selectedDate, "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ελέγξτε τους αριθμούς.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }
}