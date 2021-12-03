import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TryRemoteDB {
    private static final String USER_NAME = "V85Nsop5AG";
    private static final String DATABASE_NAME = "V85Nsop5AG";
    private static final String PASSWORD = "C2S7XoYv8m";
    private static final String PORT = "3306";
    private static final String SERVER = "remotemysql.com";

    public static void main(String[] args) {
        /* Ex. 1 */
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://"+SERVER+":"+PORT, USER_NAME, PASSWORD);
            String myTable = "dogs";

            /* Ex. 2 */
            Dog dog1 = new Dog("Pulse", 2, "jack russell terrier");
            Dog dog2 = new Dog("Ribbon", 3, "cane corso");
            Dog dog3 = new Dog("Milton", 2, "german shepherd");
            ArrayList<Dog> dogs = new ArrayList<>();
            dogs.add(dog1);
            dogs.add(dog2);
            dogs.add(dog3);
            ArrayList<ArrayList<String>> values = new ArrayList<>();
            for (Dog dog: dogs) {
                ArrayList<String> dogValues = new ArrayList<>();
                dogValues.add(dog.getName());
                dogValues.add(String.valueOf(dog.getAge()));
                dogValues.add(dog.getBreed());
                values.add(dogValues);
            }
            ArrayList<String> fields = new ArrayList<>();
            fields.add("name");
            fields.add("age");
            fields.add("breed");
            String insertThreeDogsQuery = insertQuery(myTable, values, fields);
            executeQuery(db, insertThreeDogsQuery, false);

            /* Ex. 3 */
            HashMap<String, String> setValues = new HashMap<>();
            HashMap<String, String> updateConditions = new HashMap<>();
            setValues.put("age", "2");
            updateConditions.put("id", "= 2");
            String updateDogTwoAgeQuery = updateQuery(myTable, setValues, updateConditions);
            executeQuery(db, updateDogTwoAgeQuery, false);

            /* Ex. 4 */
            HashMap<String, String> deleteConditions = new HashMap<>();
            deleteConditions.put("id", "= 3");
            String deleteThirdDogQuery = deleteQuery(myTable, deleteConditions);
            executeQuery(db, deleteThirdDogQuery, false);

            /* Ex. 5 */
            String queryTable = selectQuery(myTable);
            executeQuery(db, queryTable, true);
            
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeQuery(Connection con, String query, boolean isSelect) throws SQLException {
        if (!isSelect)
            con.createStatement().execute(query);
        else {
            ResultSet rs = con.createStatement().executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("id: " + id + " | " + "name: " + name);
            }
        }
    }

    public static String selectQuery(String table) {
        return "SELECT * FROM " + DATABASE_NAME + "." + table;
    }

    public static String selectQuery(String table, HashMap<String, String> conditions) {
        String selectAllFrom = "SELECT * FROM " + DATABASE_NAME + "." + table + " ";
        String whereConditions = whereClause(conditions);
        return selectAllFrom.concat(whereConditions);
    }

    public static String selectQuery(String table, ArrayList<String> columns, HashMap<String, String> conditions) {
        String select = "SELECT " + String.join(", ", columns) + " ";
        String from = "FROM " + DATABASE_NAME + "." + table + " ";
        String whereConditions = whereClause(conditions);
        return select.concat(from).concat(whereConditions);
    }

    public static String insertQuery(String table, ArrayList<ArrayList<String>> values) {
        String insertInto = "INSERT INTO " + DATABASE_NAME + "." + table + " ";
        String valuesToInsert = "VALUES ";
        for (int i = 0; i < values.size(); i++) {
            if (i == values.size() - 1)
                valuesToInsert = valuesToInsert.concat(putInParenthesis(values.get(i), false));
            else
                valuesToInsert = valuesToInsert.concat(putInParenthesis(values.get(i), false) + ", ");
        }

        return insertInto.concat(valuesToInsert);
    }

    public static String insertQuery(String table, ArrayList<ArrayList<String>> values, ArrayList<String> fields) {
        String insertInto = "INSERT INTO " + DATABASE_NAME + "." + table + " ";
        String fieldsToInsert = putInParenthesis(fields, true) + " ";
        String valuesToInsert = "VALUES ";
        for (int i = 0; i < values.size(); i++) {
            if (i == values.size() - 1)
                valuesToInsert = valuesToInsert.concat(putInParenthesis(values.get(i), false));
            else
                valuesToInsert = valuesToInsert.concat(putInParenthesis(values.get(i), false) + ", ");
        }

        return insertInto.concat(fieldsToInsert).concat(valuesToInsert);
    }

    public static String updateQuery(String table, HashMap<String, String> setValues, HashMap<String, String> conditions) {
        String updateTable = "UPDATE " + DATABASE_NAME + "." + table + " ";
        String setColumns = "SET ";
        int countSetValues = 0;
        for (String key: setValues.keySet()) {
            if (countSetValues == setValues.size() - 1)
                setColumns = setColumns.concat("`" + key + "` = '" + setValues.get(key) + "' ");
            else
                setColumns = setColumns.concat("`" + key + "` = '" + setValues.get(key) + "', ");
            countSetValues++;
        }
        if (conditions.isEmpty()) return updateTable.concat(setColumns);

        String whereConditions = whereClause(conditions);

        return updateTable.concat(setColumns).concat(whereConditions);
    }

    public static String deleteQuery(String table, HashMap<String, String> conditions) {
        String deleteFrom = "DELETE FROM " + DATABASE_NAME + "." + table + " ";
        String whereConditions = whereClause(conditions);
        return deleteFrom.concat(whereConditions);
    }

    private static String putInParenthesis(ArrayList<String> values, boolean backtick) {
        for (int i = 0; i < values.size(); i++) {
            if (backtick)
                values.set(i, "`" + values.get(i) + "`");
            else
                values.set(i, "'" + values.get(i) + "'");
        }
        return "(" + String.join(", ", values) + ")";
    }

    private static String whereClause(HashMap<String, String> conditions) {
        String whereConditions = "WHERE ";
        int countConditions = 0;
        for (String key: conditions.keySet()) {
            if (countConditions == 0)
                whereConditions = whereConditions.concat("`" + key + "` " + conditions.get(key));
            else
                whereConditions = whereConditions.concat("AND `" + key + "` " + conditions.get(key));
            countConditions++;
        }
        return whereConditions;
    }
}
