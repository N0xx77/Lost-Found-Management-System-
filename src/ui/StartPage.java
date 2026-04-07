package ui;
import java.awt.*;
import javax.swing.*;


public class StartPage extends JFrame{
    private final JButton login, signup;

    public StartPage(){
        setTitle("Create Account - SIT Lost & Found");
        setSize(400,300);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        login = new JButton("Login");
        signup = new JButton("Sign Up");

        Dimension btnSize = new Dimension(150, 40);
        login.setPreferredSize(btnSize);
        signup.setPreferredSize(btnSize);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        
        add(login, gbc);
        gbc.gridy = 1;
        add(signup, gbc);

        login.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
            
        });

        signup.addActionListener(e -> {
            this.dispose();
            new SignUpFrame().setVisible(true); 
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
