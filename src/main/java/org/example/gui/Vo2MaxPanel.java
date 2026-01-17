package org.example.gui;

import org.example.User;

import javax.swing.*;
import java.awt.*;

public class Vo2MaxPanel extends JPanel {

    private UserPanel userPanel;
    private JTextArea resultArea;
    private JButton btnCalculate;

    // Πεδίο: Διάρκεια Άσκησης
    private JTextField txtDurationMinutes;

    public Vo2MaxPanel(UserPanel userPanel) {
        this.userPanel = userPanel;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Τίτλος ---
        JLabel lblTitle = new JLabel("Υπολογισμός VO2 Max & Θερμίδων", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        // --- Κεντρικό Πάνελ Εισαγωγής ---
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        inputPanel.add(new JLabel("Διάρκεια Άσκησης (λεπτά): "));
        txtDurationMinutes = new JTextField("60", 5); // Προεπιλογή 60 λεπτά
        inputPanel.add(txtDurationMinutes);

        btnCalculate = new JButton("Υπολογισμός");
        btnCalculate.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(btnCalculate);

        // Χρησιμοποιούμε ένα ενδιάμεσο panel για σωστή στοίχιση
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(inputPanel, BorderLayout.NORTH);

        // --- Περιοχή αποτελεσμάτων ---
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        resultArea.setBorder(BorderFactory.createTitledBorder("Αναφορά Φυσικής Κατάστασης"));

        centerContainer.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);

        // --- Λειτουργία ---
        btnCalculate.addActionListener(e -> calculateVo2Max());
    }

    private void calculateVo2Max() {
        User user = userPanel.getCurrentUser();
        resultArea.setText("");

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ αποθηκεύστε πρώτα ένα Προφίλ Χρήστη.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int age = user.getAge();
            int rhr = user.getRestingHeartRate();
            double weight = user.getWeight();

            // Διάβασμα χρόνου
            if (txtDurationMinutes.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Δώστε διάρκεια άσκησης.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int timeMinutes = Integer.parseInt(txtDurationMinutes.getText().trim());

            if (rhr <= 0) {
                resultArea.append("Σφάλμα: Οι παλμοί ηρεμίας πρέπει να είναι > 0.\n");
                return;
            }

            // 1. Υπολογισμός Max Heart Rate (MHR)
            int maxHr = 220 - age;

            // 2. Υπολογισμός VO2 Max (Τύπος Uth-Sørensen-Overgaard-Pedersen)
            // VO2max = 15.3 * (MHR / RHR)
            double vo2Max = 15.3 * ((double) maxHr / rhr);

            // 3. Υπολογισμός Θερμίδων (ΒΑΣΕΙ ΤΟΥ ΤΥΠΟΥ ΤΗΣ ΕΚΦΩΝΗΣΗΣ)
            // Τύπος: C = (VO2Max * w * t) / 200
            double calories = (vo2Max * weight * timeMinutes) / 200.0;

            // 4. Εμφάνιση Στοιχείων
            resultArea.append("=== ΣΤΟΙΧΕΙΑ ΑΘΛΗΤΗ ===\n");
            resultArea.append("Όνομα: " + user.getName() + "\n");
            resultArea.append("Ηλικία: " + age + " έτη\n");
            resultArea.append("Βάρος: " + weight + " kg\n");
            resultArea.append("Παλμοί Ηρεμίας (RHR): " + rhr + " bpm\n");
            resultArea.append("Μέγιστοι Παλμοί (MHR): " + maxHr + " bpm\n\n");

            resultArea.append(String.format("VO2 Max: %.2f ml/kg/min\n", vo2Max));

            // Αξιολόγηση
            String category = evaluateVo2Max(vo2Max, user.getGender());
            resultArea.append("Κατηγορία: " + category + "\n\n");

            resultArea.append("=== ΕΚΤΙΜΗΣΗ ΘΕΡΜΙΔΩΝ ===\n");
            resultArea.append("Για άσκηση διάρκειας " + timeMinutes + " λεπτών\n");
            resultArea.append("στη μέγιστη ένταση (VO2 Max):\n");
            resultArea.append(String.format("--> %.0f kcal\n", calories));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ελέγξτε ότι η διάρκεια είναι αριθμός.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String evaluateVo2Max(double vo2, String gender) {
        boolean isMale = "Male".equalsIgnoreCase(gender);

        // Πίνακας Cooper Institute (Ενδεικτικές τιμές)
        if (isMale) {
            if (vo2 > 50) return "ΕΞΑΙΡΕΤΙΚΟ (Elite)";
            if (vo2 > 42) return "ΠΟΛΥ ΚΑΛΟ (Good)";
            if (vo2 > 34) return "ΜΕΤΡΙΟ (Average)";
            if (vo2 > 25) return "ΧΑΜΗΛΟ (Below Average)";
            return "ΠΟΛΥ ΧΑΜΗΛΟ (Poor)";
        } else {
            if (vo2 > 45) return "ΕΞΑΙΡΕΤΙΚΟ (Elite)";
            if (vo2 > 38) return "ΠΟΛΥ ΚΑΛΟ (Good)";
            if (vo2 > 30) return "ΜΕΤΡΙΟ (Average)";
            if (vo2 > 22) return "ΧΑΜΗΛΟ (Below Average)";
            return "ΠΟΛΥ ΧΑΜΗΛΟ (Poor)";
        }
    }
}