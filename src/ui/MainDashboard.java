package ui;

import database_access_only.DataBaseHandler;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;


public class MainDashboard extends JFrame {
    private final Users currentUser;
    private final DataBaseHandler dbHandler;

    private DefaultTableModel itemModel;
    private JTable itemTable; 
    private DefaultTableModel globalFeedModel;
    private JTable globalFeedTable;

    private ItemMatchesFrame itemMatchFrame;

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
        tabs.addTab("Manage my Reports", createManageMyReportsPanel());
        tabs.addTab("Notifications", createNotificationPanel());
        tabs.addTab("Global Feed", createGlobalFeedPanel());

        add(tabs, BorderLayout.CENTER);

        tabs.addChangeListener(e -> {
            int index = tabs.getSelectedIndex();
            if (index == 2) { 
                refreshItemsTable();
            }
            if (index == 3) { 
                refreshGlobalFeed();
            }
        });

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

            nameF.setText("");
            locF.setText("");
            descF.setText("");
            brandF.setText("");
            locF.setText("");
            matF.setText("");
            shapeF.setText("");
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
            brandF.setText("");
            locF.setText("");
            matF.setText("");
            shapeF.setText("");
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

    private JPanel createGlobalFeedPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 1. Table Setup
        globalFeedModel = dbHandler.getGlobalFeed();
        globalFeedTable = new JTable(globalFeedModel);

        JButton refreshBtn = new JButton("Refresh Feed");
        refreshBtn.addActionListener(e -> refreshGlobalFeed());
        
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolBar.add(refreshBtn);

        panel.add(new JLabel(" Recent Lost Items @ SIT Pune", SwingConstants.LEFT), BorderLayout.NORTH);
        panel.add(new JScrollPane(globalFeedTable), BorderLayout.CENTER);
        panel.add(toolBar, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createManageMyReportsPanel(){
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        itemModel = dbHandler.getReportedItems(currentUser.getID());
        itemTable = new JTable(itemModel);

        itemTable.removeColumn(itemTable.getColumnModel().getColumn(0));
        itemTable.removeColumn(itemTable.getColumnModel().getColumn(1));

        JScrollPane scrollPane = new JScrollPane(itemTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton match = new JButton("Generate Matches");
        JButton delete = new JButton("Cancel Report");

        btnPanel.add(match);
        btnPanel.add(delete);
        panel.add(btnPanel, BorderLayout.SOUTH);

        match.addActionListener(e -> {
            int row = itemTable.getSelectedRow();

            if(row != -1){
                long foundid = (long) itemModel.getValueAt(row, 0);
                boolean newMatchesGenerated = dbHandler.generateMatches(foundid);
                
                String message = newMatchesGenerated ? "New matches generated successfully!" : "Matches already exist for this item.";
                JOptionPane.showMessageDialog(this, message);

                if(itemMatchFrame == null || !itemMatchFrame.isVisible()){
                    itemMatchFrame = new ItemMatchesFrame(currentUser, foundid);
                }
                else{
                    itemMatchFrame.toFront();
                    itemMatchFrame.requestFocus();
                }
                
            }
            else{
                JOptionPane.showMessageDialog(this, "Please select a row!");
            }
        });

        delete.addActionListener(e-> {
            int row = itemTable.getSelectedRow();
            boolean deleted = false;

            if(row != -1){
                long itemid = (long) itemModel.getValueAt(row, 1);
                deleted = dbHandler.deleteItem(itemid);
            }
            else{
                JOptionPane.showMessageDialog(this, "Please select a row!");
            }
            
            if(deleted){
                JOptionPane.showMessageDialog(this, "Cancelled the Item Successfully!");
                refreshItemsTable();

            }
            else{
                JOptionPane.showMessageDialog(this, "No cancellation of items!");

            }            
        });

        return panel;
    }

    //helper function to refresh after every updation in table
    private void refreshItemsTable() {
        itemModel = dbHandler.getReportedItems(currentUser.getID());
        itemTable.setModel(itemModel);

        if (itemTable.getColumnCount() >= 2) {
            itemTable.removeColumn(itemTable.getColumnModel().getColumn(0)); // Removes Found ID
            itemTable.removeColumn(itemTable.getColumnModel().getColumn(0)); // Removes Item ID
        }
    }

    private void refreshGlobalFeed(){
        globalFeedModel = dbHandler.getGlobalFeed();
        globalFeedTable.setModel(globalFeedModel);
        
        globalFeedTable.revalidate();
        globalFeedTable.repaint();
    }

    private JPanel createNotificationPanel(){
        return new notificationPage(currentUser, dbHandler);
    }
}