import java.sql.*;

public class DataBaseHandler{
    
    public void insertLostItem(Lost_item l){
        String sql = "{Call ReportLost(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection con = DBConnection.getConnection(); CallableStatement stmt = con.prepareCall(sql)){
            stmt.setDate(1, l.getLostDate());
            stmt.setString(2, l.getLostLocation());
            stmt.setLong(3, l.getUserId());
            stmt.setString(4, l.getItemName());
            stmt.setString(5, l.getShape());
            stmt.setString(6, l.getMaterial());
            stmt.setString(7, l.getBrand());
            //stmt.setString(8, l.getob());


        } catch (SQLException e) {
            System.out.println(e);
        }
    } 
}
