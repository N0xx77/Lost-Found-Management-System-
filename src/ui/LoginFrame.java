package ui;

import database_access_only.DataBaseHandler;
import java.awt.*;
import javax.swing.*;
import model.Users;

public class LoginFrame extends JFrame {
    private final JTextField emailField;
    private final JPasswordField passField;
    private final JButton loginBtn;
    private final DataBaseHandler dbHandler;

    public LoginFrame() {
        dbHandler = new DataBaseHandler();
        setTitle("SIT Pune - Lost & Found Login");
        setSize(450, 300); // More standard login dimensions
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // Using a vertical box layout to keep variables exactly as they are
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Email section
        mainPanel.add(new JLabel("Email (firstname.lastname.btech[year]@sitpune.edu.in):"));
        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(emailField);
        mainPanel.add(Box.createVerticalStrut(10)); // Spacing

        // Password section
        mainPanel.add(new JLabel("Password:"));
        passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        mainPanel.add(passField);
        mainPanel.add(Box.createVerticalStrut(20)); // Spacing

        // Login Button
        loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(loginBtn);

        add(mainPanel);

        // ACTION: Allow "Enter" key to trigger login
        this.getRootPane().setDefaultButton(loginBtn);

        loginBtn.addActionListener(e -> {
            attemptLogin();
        });

        setVisible(true);
    }
    private void attemptLogin() {
        String email = emailField.getText();
        String pass = new String(passField.getPassword()); 

        if (email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Users loggedInUser = dbHandler.validateLogin(email, pass);

        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + loggedInUser.getName());
            
            MainDashboard dashboard = new MainDashboard(loggedInUser);
            dashboard.setVisible(true);
            
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}