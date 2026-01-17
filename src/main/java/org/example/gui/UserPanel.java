package org.example.gui;

import org.example.User;
import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {

    private JTextField txtName;
    private JTextField txtWeight;
    private JTextField txtAge;
    private JComboBox<String> cmbGender;

    // ΝΕΟ ΠΕΔΙΟ
    private JTextField txtRestingHr;

    private JButton btnSave;
    private User currentUser;

    public UserPanel() {
        setLayout(new GridLayout(0, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Όνομα
        add(new JLabel("Όνομα:"));
        txtName = new JTextField();
        add(txtName);

        // Βάρος
        add(new JLabel("Βάρος (kg):"));
        txtWeight = new JTextField();
        add(txtWeight);

        // Ηλικία
        add(new JLabel("Ηλικία:"));
        txtAge = new JTextField();
        add(txtAge);

        // Φύλο
        add(new JLabel("Φύλο:"));
        String[] genders = {"Male", "Female"};
        cmbGender = new JComboBox<>(genders);
        add(cmbGender);

        // --- ΝΕΟ ΠΕΔΙΟ: Παλμοί Ηρεμίας ---
        add(new JLabel("Παλμοί Ηρεμίας (RHR):"));
        txtRestingHr = new JTextField("60"); // Προεπιλογή
        add(txtRestingHr);
        // --------------------------------

        btnSave = new JButton("Αποθήκευση Προφίλ");
        add(new JLabel(""));
        add(btnSave);

        btnSave.addActionListener(e -> saveUser());
    }

    private void saveUser() {
        try {
            String name = txtName.getText();
            String weightStr = txtWeight.getText();
            String ageStr = txtAge.getText();
            String rhrStr = txtRestingHr.getText();
            String gender = (String) cmbGender.getSelectedItem();

            // Έλεγχος κενών πεδίων
            if (name.isEmpty() || weightStr.isEmpty() || ageStr.isEmpty() || rhrStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ συμπληρώστε όλα τα πεδία.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double weight = Double.parseDouble(weightStr);
            int age = Integer.parseInt(ageStr);
            int rhr = Integer.parseInt(rhrStr);

            // Δημιουργία Χρήστη
            currentUser = new User(name, weight, age, gender, rhr);

            // --- ΔΗΜΙΟΥΡΓΙΑ ΩΡΑΙΟΥ ΜΗΝΥΜΑΤΟΣ ---
            StringBuilder msg = new StringBuilder();
            msg.append("Το προφίλ αποθηκεύτηκε επιτυχώς!\n\n");
            msg.append("Όνομα: ").append(name).append("\n");
            msg.append("Ηλικία: ").append(age).append(" έτη\n");
            msg.append("Βάρος: ").append(weight).append(" kg\n");
            msg.append("Φύλο: ").append(gender).append("\n");
            msg.append("Παλμοί Ηρεμίας (RHR): ").append(rhr).append(" bpm");

            JOptionPane.showMessageDialog(this, msg.toString(), "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ ελέγξτε ότι τα πεδία (Βάρος, Ηλικία, RHR) περιέχουν έγκυρους αριθμούς.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}