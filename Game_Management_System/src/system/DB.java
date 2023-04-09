package system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//Connect to DataBase
public class DB {
    private static final String url = "jdbc:mysql://localhost:3306/GameSystem?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String username = "root";
    private static final String password = "leoliu0717";
    private Connection conn = null;

    private DB(){
    }

    private static class DBHelper{
        private static final DB INSTANCE = new DB();
    }

    public static Connection getConnection(){
        return DBHelper.INSTANCE.connect();
    }

    public static void closeConnection(){
        DBHelper.INSTANCE.disconnect();
    }

    private Connection connect() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private void disconnect() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
