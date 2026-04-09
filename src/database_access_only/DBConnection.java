package database_access_only;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DBConnection {
    
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/lost_and_found";
        String user = "root";
        String password = System.getenv("DB_PASSWORD");
        if (password == null || password.isBlank()) {
            password = readPasswordFromDotEnv();
        }
        if(password == null || password.isBlank()){
            throw new SQLException("DB_PASSWORD is missing! Check your .env and launch.json envFile.");
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found!");
        }
        
    }

    private static String readPasswordFromDotEnv() {
        Path[] candidates = new Path[] {
            Paths.get(".env"),
            Paths.get("src", ".env"),
            Paths.get("bin", ".env")
        };

        for (Path path : candidates) {
            if (!Files.exists(path)) {
                continue;
            }
            String value = readKeyFromFile(path, "DB_PASSWORD");
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private static String readKeyFromFile(Path filePath, String key) {
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int equalsIndex = line.indexOf('=');
                if (equalsIndex <= 0) {
                    continue;
                }
                String fileKey = line.substring(0, equalsIndex).trim();
                if (!key.equals(fileKey)) {
                    continue;
                }
                String value = line.substring(equalsIndex + 1).trim();
                if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                return value;
            }
        } catch (IOException ignored) {
            return null;
        }
        return null;
    }
}
