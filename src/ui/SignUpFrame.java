package ui;

import database_access_only.DataBaseHandler;
import java.awt.*;
import javax.swing.*;
import service.AppException;


public class SignUpFrame extends JFrame{
    private JTextField fnField, lnField, emailField, phoneField;
    private JPasswordField passField;
    final private JButton submit, clear;
    private DataBaseHandler db = new DataBaseHandler();

    public SignUpFrame(){
        setTitle("Create Account - SIT Lost & Found");
        setSize(450,400);
        setLayout(new GridBagLayout());

        fnField = new JTextField(15);
        lnField = new JTextField(15);
        phoneField = new JTextField(15);
        emailField = new JTextField(15);
        passField = new JPasswordField(15);
        submit = new JButton("Register");
        clear = new JButton("Clear");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addLabelAndField("First Name:", fnField, 0, gbc);
        addLabelAndField("Last Name:", lnField, 1, gbc);
        addLabelAndField("Phone Number:", phoneField, 2, gbc);
        addLabelAndField("Email (SIT EMAIL):", emailField, 3, gbc);
        addLabelAndField("Password:", passField, 4, gbc);
        
        

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(submit, gbc);
        gbc.gridx = 1;
        add(clear, gbc);

        submit.addActionListener(e -> {
            try {
                String email = emailField.getText().toLowerCase().trim();
                String fname = fnField.getText().toLowerCase().trim();
                String lname = lnField.getText().toLowerCase().trim();
                String phone = phoneField.getText().trim();
                String password = new String(passField.getPassword());
                
                validateEmail(email);
                validateName(fname, lname);
                phone = validatePhone(phone);
                validatePassword(password);

                db.RegisterUser(fname, lname, email, password, phone);
                JOptionPane.showMessageDialog(this, "Success! Please Login.");
                fnField.setText("");
                lnField.setText("");
                emailField.setText("");
                passField.setText("");
                phoneField.setText("");

                JOptionPane.showMessageDialog(this, "Successfully Registered!");
                this.dispose();
                new LoginFrame().setVisible(true);
            } 
            catch (AppException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clear.addActionListener(e -> {
            fnField.setText("");
            lnField.setText("");
            emailField.setText("");
            passField.setText("");
            phoneField.setText("");

        });

        setLocationRelativeTo(null);
        setVisible(true);
        
    }

    private void addLabelAndField(String labelText, JComponent Field, int row, GridBagConstraints gbc){
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        add(Field, gbc);
    }

    private void validateEmail(String email) throws AppException{
        String emailregex = "^[a-zA-Z]+\\.[a-zA-Z]+\\.btech\\d{4}@sitpune\\.edu\\.in$";

        if(email == null || email.isEmpty()){
            throw new AppException("Email cannot be Empty!", "EMPTY_EMAIL");
        }
        else if(!email.matches(emailregex)){
            throw new AppException("Invalid Email Type! Must be SIT Email!", "INV_FORMAT");
        }
    }

    private void validateName(String fname, String lname) throws AppException{
        String nameregex = "^[a-zA-Z]+$";
        
        if(fname == null || lname == null || fname.isEmpty() || lname.isEmpty()){
            throw new AppException("First Name and Last Name cannot be Empty!", "EMPTY_NAME");
        }
        else if(!fname.matches(nameregex) || !lname.matches(nameregex)){
            throw new AppException("Invalid Name! Must be only Alphabets!", "INV_FORMAT");
        }
    }

    private String validatePhone(String phone) throws AppException{
        String phoneregex = "^(?:\\+91|91)?[6-9]\\d{9}$";
        String clean = "";

        if(phone == null || phone.isEmpty()){
            throw new AppException("Phone Number cannot be Empty!", "EMPTY_PHONE");
        }
        else if(!phone.matches(phoneregex)){
            throw new AppException("Invalid Phone Number! Must be only 10 Digits [0-9]!", "INV_FORMAT");
        }
        else if(phone.matches(phoneregex)){
            clean = phone.replaceAll("^(\\+91|91)", "");
        }

        return clean;
    }

    private void validatePassword(String password) throws AppException{
        String passregex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

        if(password == null || password.isEmpty()){
            throw new AppException("Password cannot be Empty!", "EMPTY_PASSWORD");
        }
        if(!password.matches(passregex)){
            throw new AppException("Password doesn't meet the requirements [Atleast 1 Uppercase, Lowercase, Digit with at least 8 characters!", "INV_MATCH");
        }
    }

}
