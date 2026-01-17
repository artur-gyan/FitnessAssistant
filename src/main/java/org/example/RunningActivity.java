package org.example;

public class RunningActivity extends DefaultActivity {
    public RunningActivity(String id) {
        super("Running", id);
        this.multiplier = 10.0; // Το τρέξιμο καίει πολύ (μ = 10)
    }
}