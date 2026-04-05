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
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10)); 
        ((JComponent) getContentPane()).setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // UI Elements
        add(new JLabel("Email (@sitpune.edu.in):"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passField = new JPasswordField();
        add(passField);

        loginBtn = new JButton("Login");
        add(loginBtn);

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