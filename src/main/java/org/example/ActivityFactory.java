package org.example;

import java.util.HashMap;
import java.util.Map;

public class ActivityFactory {
    private Map<String, Activity> instances = new HashMap<>();

    public Activity getOrCreateActivity(String sport, String id) {
        if (instances.containsKey(id)) {
            return instances.get(id);
        }

        Activity activity;

        // ΕΛΕΓΧΟΣ: Ανάλογα με το Sport φτιάχνουμε το σωστό αντικείμενο
        // Χρησιμοποιούμε .toLowerCase() για να μην έχουμε θέμα με κεφαλαία/μικρά
        switch (sport.toLowerCase()) {
            case "running":
                activity = new RunningActivity(id);
                break;
            case "biking": /// ή "cycling" ανάλογα τι γράφει το XML
            case "cycling":
                activity = new CyclingActivity(id);
                break;
            case "swimming":
                activity = new SwimmingActivity(id);
                break;
            default:
                /// Για όλα τα άλλα (π.χ. "Other"), φτιάχνουμε το απλό
                activity = new DefaultActivity(sport, id);
                break;
        }

        instances.put(id, activity);
        return activity;
    }
}
