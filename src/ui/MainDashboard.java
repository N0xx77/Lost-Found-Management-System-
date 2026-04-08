package ui;

import database_access_only.DataBaseHandler;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Found_item;
import model.Lost_item;
import model.Users;

public class MainDashboard extends JFrame {
    private final Users currentUser;
    private final DataBaseHandler dbHandler;

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
        tabs.addTab("Global Feed", createGlobalFeedPanel()); 

        add(tabs, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createLostReportPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameF = new JTextField(15);
        JTextField matF = new JTextField(15);
        JTextField shapeF = new JTextField(15);
        JTextField brandF = new JTextField(15);
        JTextField locF = new JTextField(15);
        JTextArea descF = new JTextArea(3, 15);

        JComboBox<String> objCB = new JComboBox<>(dbHandler.getObjectNames());
        JComboBox<String> colCB = new JComboBox<>(dbHandler.getColorNames());
        JComboBox<String> catCB = new JComboBox<>(dbHandler.getCategoryNames());

        int r = 0;
        panel.add(new JLabel("Item Name:"), getGbc(0, r, gbc)); panel.add(nameF, getGbc(1, r++, gbc));
        panel.add(new JLabel("Material:"), getGbc(0, r, gbc)); panel.add(matF, getGbc(1, r++, gbc));
        panel.add(new JLabel("Shape:"), getGbc(0, r, gbc)); panel.add(shapeF, getGbc(1, r++, gbc));
        panel.add(new JLabel("Brand:"), getGbc(0, r, gbc)); panel.add(brandF, getGbc(1, r++, gbc));
        panel.add(new JLabel("Object Type:"), getGbc(0, r, gbc)); panel.add(objCB, getGbc(1, r++, gbc));
        panel.add(new JLabel("Color:"), getGbc(0, r, gbc)); panel.add(colCB, getGbc(1, r++, gbc));
        panel.add(new JLabel("Category:"), getGbc(0, r, gbc)); panel.add(catCB, getGbc(1, r++, gbc));
        panel.add(new JLabel("Location:"), getGbc(0, r, gbc)); panel.add(locF, getGbc(1, r++, gbc));
        panel.add(new JLabel("Description:"), getGbc(0, r, gbc)); panel.add(new JScrollPane(descF), getGbc(1, r++, gbc));

        JButton submitBtn = new JButton("Report Lost");
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = r;
        panel.add(submitBtn, gbc);

        submitBtn.addActionListener(e -> {
            // 1. Get IDs from Names using the new helper functions
            int objID = dbHandler.getObjectIdByName(objCB.getSelectedItem().toString());
            int colID = dbHandler.getColorIdByName(colCB.getSelectedItem().toString());
            int catID = dbHandler.getCategoryIdByName(catCB.getSelectedItem().toString());

            // 2. Build the object using your EXACT constructor
            Lost_item newItem = new Lost_item(
                nameF.getText(),             
                matF.getText(),              
                shapeF.getText(),            
                brandF.getText(),            
                descF.getText(),             
                "active",            
                objID,                       
                colID,                       
                catID,                       
                locF.getText(),              
                new java.sql.Date(System.currentTimeMillis()), 
                currentUser.getID(),         
                0                            
            );

            dbHandler.insertLostItem(newItem);
            JOptionPane.showMessageDialog(this, "Lost Item Reported Successfully!");
        });

        return panel;
    }

    // Helper Function for GridBagLayout
    private GridBagConstraints getGbc(int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x; gbc.gridy = y;
        gbc.gridwidth = 1;
        return gbc;
    }

    private JPanel createFoundReportPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Input Fields
        JTextField nameF = new JTextField(15);
        JTextField matF = new JTextField(15);
        JTextField shapeF = new JTextField(15);
        JTextField brandF = new JTextField(15);
        JTextField locF = new JTextField(15); // found_location
        JTextArea descF = new JTextArea(3, 15);

        // 2. Dropdowns from DataBaseHandler
        JComboBox<String> objCB = new JComboBox<>(dbHandler.getObjectNames());
        JComboBox<String> colCB = new JComboBox<>(dbHandler.getColorNames());
        JComboBox<String> catCB = new JComboBox<>(dbHandler.getCategoryNames());

        // 3. Layout setup 
        int r = 0;
        addFormRow(panel, "Item Name:", nameF, gbc, r++);
        addFormRow(panel, "Material:", matF, gbc, r++);
        addFormRow(panel, "Shape:", shapeF, gbc, r++);
        addFormRow(panel, "Brand:", brandF, gbc, r++);
        addFormRow(panel, "Object Type:", objCB, gbc, r++);
        addFormRow(panel, "Color:", colCB, gbc, r++);
        addFormRow(panel, "Category:", catCB, gbc, r++);
        addFormRow(panel, "Location Found:", locF, gbc, r++);
        addFormRow(panel, "Description:", new JScrollPane(descF), gbc, r++);

        // 4. Submit Button
        JButton submitBtn = new JButton("Submit Found Report");
        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 2;
        panel.add(submitBtn, gbc);

        // 5. Button Action
        submitBtn.addActionListener(e -> {
            // Translate selection names to IDs
            int objID = dbHandler.getObjectIdByName(objCB.getSelectedItem().toString());
            int colID = dbHandler.getColorIdByName(colCB.getSelectedItem().toString());
            int catID = dbHandler.getCategoryIdByName(catCB.getSelectedItem().toString());
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

            Found_item newItem = new Found_item(
                nameF.getText(),
                matF.getText(),
                shapeF.getText(),
                brandF.getText(),
                descF.getText(),
                "active",     
                objID,
                colID,
                catID,
                locF.getText(),
                today,
                currentUser.getID(),
                0             
            );

            dbHandler.insertFoundItem(newItem);
            
            
            JOptionPane.showMessageDialog(this, "Success! Found item recorded.");
            
            // Optional: Clear fields after success
            nameF.setText("");
            locF.setText("");
            descF.setText("");
        });

        return panel;
    }

    // helper method to keep the GridBagLayout code clean
    private void addFormRow(JPanel p, String label, Component c, GridBagConstraints gbc, int row) {
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(c, gbc);
    }

    private JPanel createNotificationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 1. Table Setup
        String[] columnNames = {"Match ID", "Item Name", "Confidence", "Potential Owner", "Lost Location"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        
        // 2. Load Data from DB
        refreshTable(model);

        // 3. Confirm Button
        JButton confirmBtn = new JButton("Confirm & Return Item");
        confirmBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                long matchId = (long) table.getValueAt(selectedRow, 0);
                dbHandler.confirmMatch(matchId);
                JOptionPane.showMessageDialog(this, "Confirmation sent! The item status is now 'Returned'.");
                refreshTable(model); 
            } else {
                JOptionPane.showMessageDialog(this, "Please select a match from the table first.");
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(confirmBtn, BorderLayout.SOUTH);
        
        return panel;
    }

    private void refreshTable(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        try {
            ResultSet rs = dbHandler.getMatchesForFinder(currentUser.getID());
            while (rs != null && rs.next()) {
                Object[] row = {
                    rs.getLong("match_id"),
                    rs.getString("item_name"),
                    rs.getInt("conf_score") + "/10",
                    rs.getString("owner_name"),
                    rs.getString("loc_lost")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private JPanel createGlobalFeedPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 1. Table Setup
        String[] columns = {"Item Name", "Category", "Color", "Date Lost", "Location", "Description"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the feed read-only
            }
        };
        JTable table = new JTable(model);
        
        // 2. Load Function
        Runnable loadData = () -> {
            model.setRowCount(0);
            try (ResultSet rs = dbHandler.getGlobalFeed()) {
                while (rs != null && rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("item_name"),
                        rs.getString("category"),
                        rs.getString("colour"),
                        rs.getDate("date_lost"),
                        rs.getString("loc_lost"),
                        rs.getString("descriptions")
                    });
                }
            } catch (SQLException e) { System.out.println(e.getMessage()); }
        };

        // 3. Toolbar with Refresh
        JButton refreshBtn = new JButton("Refresh Feed");
        refreshBtn.addActionListener(e -> loadData.run());
        
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolBar.add(refreshBtn);

        // Initial load
        loadData.run();

        panel.add(new JLabel(" Recent Lost Items @ SIT Pune", SwingConstants.LEFT), BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(toolBar, BorderLayout.SOUTH);
        
        return panel;
    }
}