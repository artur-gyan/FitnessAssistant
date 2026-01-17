package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.example.gui.MainFrame;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        if (args.length > 0 && args[0].equals("-gui")) {
            System.out.println("Starting GUI mode...");

            // Εκκίνηση του γραφικού περιβάλλοντος (Swing Thread)
            SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            });
            return;
        }

        /// Αν δεν υπάρχουν ορίσματα, τερματίζουμε
        if (args.length == 0) {
            System.out.println("Usage: java -jar FitnessAssistant.jar [-w weight] [-g gender] [-a age] file1.tcx file2.tcx ...");
            System.out.println("Usage for GUI:     java -jar FitnessAssistant.jar -gui");
            return;
        }

        ///  ΑΡΧΙΚΟΠΟΙΗΣΗ ΜΕΤΑΒΛΗΤΩΝ
        double weight = -1; // -1 σημαίνει ότι δεν δόθηκε
        int age = 0;
        String gender = "Unknown";
        List<String> filePaths = new ArrayList<>();


        /// Διατρέχουμε τον πίνακα args για να βρούμε flags και αρχεία
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals("-w")) {
                if (i + 1 < args.length && Double.parseDouble(args[i + 1]) > 0) {
                    try {
                        weight = Double.parseDouble(args[++i]); // Διαβάζουμε το επόμενο το παιρναμε στο weight και αυξάνουμε το i
                    } catch (NumberFormatException e) {
                        logger.warning("Invalid weight format. Ignoring weight.");
                    }
                }
            } else if (arg.equals("-g")) {
                if (i + 1 < args.length) {
                    gender = args[++i];
                }
            } else if (arg.equals("-a")) {
                if (i + 1 < args.length) {
                    try {
                        age = Integer.parseInt(args[++i]);
                    } catch (NumberFormatException e) {
                        logger.warning("Invalid age format. Ignoring age.");
                    }
                }
            } else {
                // Αν δεν είναι flag, θεωρούμε ότι είναι αρχείο .tcx
                // Καθαρίζουμε τυχόν εισαγωγικά (αν υπάρχουν από copy-paste)
                if (arg.startsWith("\"") && arg.endsWith("\"")) {
                    arg = arg.substring(1, arg.length() - 1);
                }
                filePaths.add(arg);
            }
        }

        if (filePaths.isEmpty()) {
            System.out.println("No .tcx files provided.");
            return;
        }

        // Δημιουργία Χρήστη (αν δόθηκε βάρος)
        User user = null;
        if (weight > 0) {
            user = new User("User", weight, age, gender);
        }

        ///ΕΠΕΞΕΡΓΑΣΙΑ ΠΟΛΛΑΠΛΩΝ ΑΡΧΕΙΩΝ
        TcxLoader loader = new TcxLoader();

        for (String filePath : filePaths) {
            try {
                processFile(loader, filePath, user);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error processing file: " + filePath, e);
            }
            /// Κενή γραμμή ανάμεσα στα αρχεία
            System.out.println();
        }
    }

    private static void processFile(TcxLoader loader, String filePath, User user) throws Exception {
        Activity activity = loader.load(filePath);

        if (activity == null) {
            System.out.println("Could not load activity from: " + filePath);
            return;
        }

        ///ΕΚΤΥΠΩΣΗ
        System.out.println("Activity:       " + activity.getSport());
        System.out.println("Total Time:     " + activity.getFormattedTotalTime());
        System.out.printf("Total Distance: %.2f km\n", activity.getTotalDistanceKm());

        if ("Running".equalsIgnoreCase(activity.getSport())) {
            System.out.println("Avg Pace:       " + activity.calculateAveragePaceMinKm());
        } else {
            System.out.printf("Avg Speed:      %.2f km/h\n", activity.calculateAverageSpeedKmH());
        }

        int avgHr = activity.calculateAverageHeartRateOfLaps();
        if (avgHr > 0) {
            System.out.println("Avg Heart Rate: " + avgHr + " bpm");
        }

        // Υπολογισμός θερμίδων ΜΟΝΟ αν έχει δοθεί χρήστης (βάρος)
        if (user != null) {
            double calories = activity.calculateCalories(user);
            System.out.printf("Calories:       %.0f kcal\n", calories);
        }
    }
}