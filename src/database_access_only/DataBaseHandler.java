package database_access_only;
import java.sql.*;
import java.util.HashMap;
import model.Found_item;
import model.Lost_item;
import model.Users;
// import model.Item;

public class DataBaseHandler{
    private HashMap<Integer, String> objectMap = new HashMap<>();
    private HashMap<Integer, String> colourMap = new HashMap<>();
    private HashMap<Integer, String> categoryMap = new HashMap<>();

    public DataBaseHandler() {
        loadMapData();
    }

    public int getCategoryId(String name) { return categoryMap.entrySet().stream().filter(e -> e.getValue().equals(name)).map(e -> e.getKey()).findFirst().orElse(1); }
    public int getColorId(String name) { return colourMap.entrySet().stream().filter(e -> e.getValue().equals(name)).map(e -> e.getKey()).findFirst().orElse(1); }
    public int getObjectId(String name) { return objectMap.entrySet().stream().filter(e -> e.getValue().equals(name)).map(e -> e.getKey()).findFirst().orElse(1); }

    public String[] getCategoryNames() {
        return categoryMap.values().toArray(new String[0]);
    }

    public String[] getColorNames() {
        return colourMap.values().toArray(new String[0]);
    }

    public String[] getObjectNames() {
        return objectMap.values().toArray(new String[0]);
    }

    // Helper methods to get ID from Name (needed for the Lost_item constructor)
    public int getCategoryIdByName(String name) {
        return categoryMap.entrySet().stream()
                .filter(entry -> name.equals(entry.getValue()))
                .map(java.util.Map.Entry::getKey).findFirst().orElse(1);
    }

    public int getColorIdByName(String name) {
        return colourMap.entrySet().stream()
                .filter(entry -> name.equals(entry.getValue()))
                .map(java.util.Map.Entry::getKey).findFirst().orElse(1);
    }

    public int getObjectIdByName(String name) {
        return objectMap.entrySet().stream()
                .filter(entry -> name.equals(entry.getValue()))
                .map(java.util.Map.Entry::getKey).findFirst().orElse(1);
    }

    private void loadMapData(){
        try (Connection con = DBConnection.getConnection(); Statement stmt = con.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT category_id, category FROM category;");
            while(rs.next()){
                categoryMap.put(rs.getInt("category_id"), rs.getString("category"));
            }

            rs = stmt.executeQuery("SELECT colour_id, colour FROM colour;");
            while(rs.next()){
                colourMap.put(rs.getInt("colour_id"), rs.getString("colour"));
            }

            rs = stmt.executeQuery("SELECT object_id, object_type FROM object;");
            while(rs.next()){
                objectMap.put(rs.getInt("object_id"), rs.getString("object_type"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void insertLostItem(Lost_item l){
        String sql = "{Call ReportLost(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection con = DBConnection.getConnection(); CallableStatement stmt = con.prepareCall(sql)){
            
            String object = objectMap.get(l.getObjectID());
            String colour = colourMap.get(l.getColourID());
            String category = categoryMap.get(l.getCategoryID());

            stmt.setDate(1, l.getLostDate());
            stmt.setString(2, l.getLostLocation());
            stmt.setLong(3, l.getUserId());
            stmt.setString(4, l.getItemName());
            stmt.setString(5, l.getShape());
            stmt.setString(6, l.getMaterial());
            stmt.setString(7, l.getBrand());
            stmt.setString(8, object);
            stmt.setString(9, colour);
            stmt.setString(10, category);
            stmt.setString(11, l.getDescriptions());

            stmt.execute();
            System.out.println("Lost Item reported!\nExecution Successful");


        } catch (SQLException e) {
            System.out.println("Database Error: "+e);
        }
    }


    public void insertFoundItem(Found_item f){
        String sql = "{Call ReportLost(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection con = DBConnection.getConnection(); CallableStatement stmt = con.prepareCall(sql)){
            
            String object = objectMap.get(f.getObjectID());
            String colour = colourMap.get(f.getColourID());
            String category = categoryMap.get(f.getCategoryID());

            stmt.setDate(1, f.getFoundDate());
            stmt.setString(2, f.getFoundLocation());
            stmt.setLong(3, f.getUserId());
            stmt.setString(4, f.getItemName());
            stmt.setString(5, f.getShape());
            stmt.setString(6, f.getMaterial());
            stmt.setString(7, f.getBrand());
            stmt.setString(8, object);
            stmt.setString(9, colour);
            stmt.setString(10, category);
            stmt.setString(11, f.getDescriptions());

            stmt.execute();
            System.out.println("Found Item reported!\nExecution Successful");


        } catch (SQLException e) {
            System.out.println("Database Error: "+e);
        }
    }

    public Users validateLogin(String email, String password) {
        String sql = "{CALL sp_ValidateLogin(?, ?, ?, ?)}";

        try (Connection con = DBConnection.getConnection(); 
            CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            stmt.registerOutParameter(3, Types.BIGINT);  
            stmt.registerOutParameter(4, Types.VARCHAR); 

            stmt.execute();

            long userId = stmt.getLong(3);
            String firstName = stmt.getString(4);

            if (firstName != null && !firstName.trim().isEmpty()) {
                System.out.println("✅ Login Successful! Welcome " + firstName);
                
                return new Users(firstName, "", userId); 
                
            } else {
                System.out.println("Invalid Email or Password.");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Database Error during login: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getMatchesForFinder(long userId) {
        String sql = "SELECT m.match_id, i.item_name, m.conf_score, u.fname as owner_name, l.loc_lost " +
                    "FROM matches m " +
                    "JOIN lost_item l ON m.lost_id = l.lost_id " +
                    "JOIN found_item f ON m.found_id = f.found_id " +
                    "JOIN item i ON l.item_id = i.item_id " +
                    "JOIN users u ON l.user_id = u.user_id " +
                    "WHERE f.user_id = ? AND i.statuss = 'matched'";
        
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, userId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void confirmMatch(long matchId) {
        String sql = "{CALL ConfirmClaim(?)}";
        try (Connection con = DBConnection.getConnection();
            CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setLong(1, matchId);
            stmt.execute();
            System.out.println("✅ Match confirmed and items marked as returned!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ResultSet getGlobalFeed() {
        String sql = "SELECT i.item_name, c.category, col.colour, l.date_lost, l.loc_lost, i.descriptions " +
                    "FROM item i " +
                    "JOIN lost_item l ON i.item_id = l.item_id " +
                    "JOIN category c ON i.category_id = c.category_id " +
                    "JOIN colour col ON i.colour_id = col.colour_id " +
                    "WHERE i.statuss = 'active' " +
                    "ORDER BY l.date_lost DESC";
        
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
