package org.example.gui;

import org.example.Activity;
import org.example.TcxLoader;
import org.example.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardPanel extends JPanel {

    private JTextArea displayArea;
    private JButton btnLoadFiles;
    private UserPanel userPanel;

    // Αναφορά στην κοινή λίστα
    private List<Activity> allActivities;

    // Ο Constructor δέχεται πλέον και τη λίστα
    public DashboardPanel(UserPanel userPanel, List<Activity> allActivities) {
        this.userPanel = userPanel;
        this.allActivities = allActivities;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnLoadFiles = new JButton("Φόρτωση αρχείων .tcx");
        topPanel.add(btnLoadFiles);
        add(topPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        btnLoadFiles.addActionListener(e -> openFileChooser());
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setDialogTitle("Επιλογή αρχείων δραστηριότητας (.tcx)");

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            processFiles(selectedFiles);
        }
    }

    private void processFiles(File[] files) {
        displayArea.setText("");
        TcxLoader loader = new TcxLoader();
        User currentUser = userPanel.getCurrentUser();

        if (currentUser != null) {
            displayArea.append("Χρήστης: " + currentUser.getName() + "\n");
        } else {
            displayArea.append("ΠΡΟΣΟΧΗ: Δεν έχει οριστεί χρήστης.\n");
        }
        displayArea.append("--------------------------------------------------\n");

        for (File file : files) {
            try {
                Activity activity = loader.load(file.getAbsolutePath());
                if (activity != null) {
                    // ΣΗΜΑΝΤΙΚΟ: Προσθήκη στη λίστα για να τη δει το GoalPanel!
                    allActivities.add(activity);

                    printActivityStats(activity, currentUser);
                }
            } catch (Exception ex) {
                displayArea.append("Σφάλμα στο αρχείο: " + file.getName() + "\n");
            }
            displayArea.append("\n");
        }

        // Μήνυμα επιβεβαίωσης
        JOptionPane.showMessageDialog(this, "Φορτώθηκαν " + files.length + " αρχεία και προστέθηκαν στη μνήμη.", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
    }

    private void printActivityStats(Activity activity, User user) {
        StringBuilder sb = new StringBuilder();

        // --- ΒΕΛΤΙΩΣΗ ΕΜΦΑΝΙΣΗΣ ID (ΗΜΕΡΟΜΗΝΙΑΣ) ---
        String rawId = activity.getId();
        String formattedId = rawId; // Αρχικά κρατάμε το παλιό

        try {
            // Προσπαθούμε να το διαβάσουμε ως ημερομηνία ISO
            ZonedDateTime zdt = ZonedDateTime.parse(rawId);
            // Το μετατρέπουμε σε μορφή: Ημέρα/Μήνας/Έτος Ώρα:Λεπτά
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            formattedId = zdt.format(formatter);
        } catch (Exception e) {
            // Αν δεν είναι ημερομηνία (π.χ. είναι "Manual Input"), το αφήνουμε όπως είναι
            // Δεν κάνουμε τίποτα
        }

        sb.append("Αρχείο/ID:    ").append(formattedId).append("\n");
        // ---------------------------------------------

        sb.append("Activity:     ").append(activity.getSport()).append("\n");
        sb.append("Total Time:   ").append(activity.getFormattedTotalTime()).append("\n");
        sb.append(String.format("Total Dist:   %.2f km\n", activity.getTotalDistanceKm()));

        if ("Running".equalsIgnoreCase(activity.getSport())) {
            sb.append("Avg Pace:     ").append(activity.calculateAveragePaceMinKm()).append("\n");
        } else {
            sb.append(String.format("Avg Speed:    %.2f km/h\n", activity.calculateAverageSpeedKmH()));
        }

        int avgHr = activity.calculateAverageHeartRateOfLaps();
        if (avgHr > 0) {
            sb.append("Avg Heart Rate: ").append(avgHr).append(" bpm\n");
        }

        if (user != null) {
            double calories = activity.calculateCalories(user);
            sb.append(String.format("Calories:     %.0f kcal\n", calories));
        }

        sb.append("--------------------------------------------------\n");
        displayArea.append(sb.toString());
    }
}