package database_access_only;
import java.sql.*;
import java.util.HashMap;
import model.Found_item;
import model.Lost_item;
import model.Users;

public class DataBaseHandler{
    private HashMap<Integer, String> objectMap = new HashMap<>();
    private HashMap<Integer, String> colourMap = new HashMap<>();
    private HashMap<Integer, String> categoryMap = new HashMap<>();

    public DataBaseHandler() {
        loadMapData();
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
}
