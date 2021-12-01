import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class TryRemoteDB {
    private static final String USER_NAME = "V85Nsop5AG";
    private static final String DATABASE_NAME = "V85Nsop5AG";
    private static final String PASSWORD = "C2S7XoYv8m";
    private static final String PORT = "3306";
    private static final String SERVER = "remotemysql.com";

    public static void main(String[] args) {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://"+SERVER+":"+PORT, USER_NAME, PASSWORD);

            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeQuery(Connection con, String query) throws SQLException {
        con.createStatement().executeQuery(query);
    }

    public void insertToTable(String table, ArrayList<String> values) {
        
    }
}
