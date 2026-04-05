package model;

import javax.swing.*;
import database_access_only.DataBaseHandler;
import java.awt.*;

public class MainDashboard extends JFrame {
    private Users currentUser;
    private DataBaseHandler dbHandler;

    public MainDashboard(Users user) {
        this.currentUser = user;
        this.dbHandler = new DataBaseHandler();

        setTitle("SIT Lost & Found - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. Header with Welcome Message
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(45, 45, 45));
        JLabel welcomeLbl = new JLabel("Logged in as: " + currentUser.getName());
        welcomeLbl.setForeground(Color.WHITE);
        header.add(welcomeLbl);
        add(header, BorderLayout.NORTH);

        // 2. Tabbed Pane for Navigation
        JTabbedPane tabs = new JTabbedPane();

        // Add the different sections (We will build these next)
        tabs.addTab("Report Lost Item", createLostReportPanel());
        tabs.addTab("Report Found Item", createFoundReportPanel());
        tabs.addTab("Matches & Notifications", createNotificationPanel());
        tabs.addTab("Global Feed", new JPanel()); // Placeholder for now

        add(tabs, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createLostReportPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        // We will add the Form components (JTextFields, JComboBoxes) here
        panel.add(new JLabel("Lost Item Form Coming Soon..."));
        return panel;
    }

    private JPanel createFoundReportPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Found Item Form Coming Soon..."));
        return panel;
    }

    private JPanel createNotificationPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Your Matches will appear here."));
        return panel;
    }
}