package model;

import javax.swing.*;
import database_access_only.DataBaseHandler;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passField;
    private JButton loginBtn;
    private DataBaseHandler dbHandler;

    public LoginFrame() {
        dbHandler = new DataBaseHandler();
        setTitle("SIT Pune - Lost & Found Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10)); // Simple grid layout

        // UI Elements
        add(new JLabel("Email (@sitpune.edu.in):"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passField = new JPasswordField();
        add(passField);

        loginBtn = new JButton("Login");
        add(loginBtn);

        // Action Listener (CO4 - Connectivity)
        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String pass = new String(passField.getPassword());
            
            Users loggedInUser = dbHandler.validateLogin(email, pass);

        if (loggedInUser != null) {
            // 2. SUCCESS: Close the login window
            this.dispose(); 

            // 3. SUCCESS: Launch the Dashboard and pass the user object
            new MainDashboard(loggedInUser); 
            
            System.out.println("Transitioning to Dashboard...");
        } else {
            // 4. FAILURE: Show alert
            JOptionPane.showMessageDialog(this, "Invalid Email or Password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
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

        // Call the database handler using your 'Users' class
        Users loggedInUser = dbHandler.validateLogin(email, pass);

        if (loggedInUser != null) {
            // USING YOUR METHOD: getName()
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + loggedInUser.getName());
            
            // TODO: Open Main Dashboard here!
            new MainDashboard(loggedInUser);
            
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}