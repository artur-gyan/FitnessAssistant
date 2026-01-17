package org.example.gui;

import org.example.Activity;
import org.example.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoalPanel extends JPanel {

    private JTextField txtGoal;
    private JTextArea resultArea;
    private JButton btnCheck;

    // Χρειαζόμαστε πρόσβαση στον Χρήστη (για βάρος) και στη Λίστα Δραστηριοτήτων
    private UserPanel userPanel;
    private List<Activity> allActivities;

    public GoalPanel(UserPanel userPanel, List<Activity> allActivities) {
        this.userPanel = userPanel;
        this.allActivities = allActivities;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ΕΙΣΑΓΩΓΗ ΣΤΟΧΟΥ
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Ημερήσιος Στόχος Θερμίδων (kcal):"));

        txtGoal = new JTextField("2000", 6);
        topPanel.add(txtGoal);

        btnCheck = new JButton("Έλεγχος Στόχων");
        topPanel.add(btnCheck);

        add(topPanel, BorderLayout.NORTH);

        // ΑΠΟΤΕΛΕΣΜΑΤΑ
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        //ΛΕΙΤΟΥΡΓΙΑ
        btnCheck.addActionListener(e -> calculateGoals());
    }

    private void calculateGoals() {
        resultArea.setText(""); // Καθαρισμός
        User user = userPanel.getCurrentUser();

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ αποθηκεύστε πρώτα ένα Προφίλ Χρήστη.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int goal = Integer.parseInt(txtGoal.getText().trim());
            if (allActivities.isEmpty()) {
                resultArea.append("Δεν υπάρχουν φορτωμένες δραστηριότητες.\n");
                return;
            }

            // 1. ΟΜΑΔΟΠΟΙΗΣΗ ΔΡΑΣΤΗΡΙΟΤΗΤΩΝ ΑΝΑ ΗΜΕΡΑ
            // Χρησιμοποιούμε ένα Map όπου το κλειδί είναι η Ημερομηνία (String) και η τιμή οι Θερμίδες (Double)
            Map<String, Double> caloriesPerDay = new HashMap<>();

            for (Activity activity : allActivities) {
                // Υπολογισμός θερμίδων για τη συγκεκριμένη δραστηριότητα
                double cals = activity.calculateCalories(user);

                // Βρίσκουμε την ημερομηνία από το πρώτο Lap (αν υπάρχει)
                String dateKey = "Άγνωστη Ημ/νία";
                if (!activity.getLaps().isEmpty() && activity.getLaps().get(0).getStartTime() != null) {
                    // Παίρνουμε μόνο το κομμάτι της ημερομηνίας (YYYY-MM-DD)
                    dateKey = activity.getLaps().get(0).getStartTime().toLocalDate().toString();
                }

                // Προσθέτουμε τις θερμίδες στο σύνολο της ημέρας
                // (Αν υπάρχει ήδη η ημερομηνία, προσθέτουμε στο υπάρχον ποσό)
                caloriesPerDay.put(dateKey, caloriesPerDay.getOrDefault(dateKey, 0.0) + cals);
            }

            // ΕΜΦΑΝΙΣΗ ΑΠΟΤΕΛΕΣΜΑΤΩΝ
            resultArea.append("ΑΝΑΦΟΡΑ ΣΤΟΧΩΝ (Στόχος: " + goal + " kcal) ===\n\n");

            // Ταξινομούμε τις ημερομηνίες για να φαίνονται με σειρά
            List<String> sortedDates = new ArrayList<>(caloriesPerDay.keySet());
            sortedDates.sort(String::compareTo);

            for (String date : sortedDates) {
                double totalCals = caloriesPerDay.get(date);
                resultArea.append("Ημερομηνία: " + date + "\n");
                resultArea.append(String.format("   Σύνολο: %.0f kcal\n", totalCals));

                if (totalCals >= goal) {
                    resultArea.append("   ΚΑΤΑΣΤΑΣΗ: ΕΠΙΤΕΥΧΘΗΚΕ! (+" + String.format("%.0f", totalCals - goal) + " kcal)\n");
                } else {
                    double missing = goal - totalCals;
                    resultArea.append("   ΚΑΤΑΣΤΑΣΗ: Μη ολοκληρωμένος\n");
                    resultArea.append(String.format("   Υπόλοιπο: %.0f kcal για τον στόχο.\n", missing));
                }
                resultArea.append("---------------------------\n");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ο στόχος πρέπει να είναι ακέραιος αριθμός.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }
}