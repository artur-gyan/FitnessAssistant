package org.example;

public class CyclingActivity extends DefaultActivity {
    public CyclingActivity(String id) {
        super("Biking", id); // Στο αρχείο  το λέει "Biking" ή "Cycling"
        this.multiplier = 8.0;  // Το ποδήλατο καίει λιγότερο από τρέξιμο
    }
}