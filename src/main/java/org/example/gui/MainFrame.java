package org.example.gui;

import org.example.Activity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private List<Activity> globalActivityList;

    public MainFrame() {
        globalActivityList = new ArrayList<>();

        setTitle("Fitness Assistant App - Final Version");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- MENOY ---
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(60, 63, 65));
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton btnDashboard = new JButton("Dashboard");
        JButton btnAddActivity = new JButton("Προσθήκη");
        JButton btnGoals = new JButton("Στόχοι");

        // ΝΕΟ ΚΟΥΜΠΙ
        JButton btnVo2 = new JButton("VO2 Max");

        JButton btnUserProfile = new JButton("Προφίλ");

        menuPanel.add(btnDashboard);
        menuPanel.add(btnAddActivity);
        menuPanel.add(btnGoals);
        menuPanel.add(btnVo2); // Προσθήκη στο μενού
        menuPanel.add(btnUserProfile);

        add(menuPanel, BorderLayout.NORTH);

        // --- PANELS ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // 1. Profile
        UserPanel pnlProfile = new UserPanel();
        // 2. Dashboard
        DashboardPanel pnlDashboard = new DashboardPanel(pnlProfile, globalActivityList);
        // 3. Add
        AddActivityPanel pnlAdd = new AddActivityPanel(pnlProfile, globalActivityList);
        // 4. Goals
        GoalPanel pnlGoals = new GoalPanel(pnlProfile, globalActivityList);

        // 5. ΝΕΟ PANEL VO2 MAX
        Vo2MaxPanel pnlVo2 = new Vo2MaxPanel(pnlProfile);

        contentPanel.add(pnlDashboard, "DASHBOARD");
        contentPanel.add(pnlAdd, "ADD_ACTIVITY");
        contentPanel.add(pnlGoals, "GOALS");
        contentPanel.add(pnlVo2, "VO2MAX"); // Προσθήκη στην τράπουλα
        contentPanel.add(pnlProfile, "PROFILE");

        add(contentPanel, BorderLayout.CENTER);

        // --- LISTENERS ---
        btnDashboard.addActionListener(e -> cardLayout.show(contentPanel, "DASHBOARD"));
        btnAddActivity.addActionListener(e -> cardLayout.show(contentPanel, "ADD_ACTIVITY"));
        btnGoals.addActionListener(e -> cardLayout.show(contentPanel, "GOALS"));
        btnVo2.addActionListener(e -> cardLayout.show(contentPanel, "VO2MAX")); // Ενέργεια κουμπιού
        btnUserProfile.addActionListener(e -> cardLayout.show(contentPanel, "PROFILE"));
    }
}