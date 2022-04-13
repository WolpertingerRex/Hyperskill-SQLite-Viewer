package viewer;

import java.sql.*;
import java.util.*;

public class Database {
    private final String url;


    public Database(String fileName) {       
        url = "jdbc:SQLite:" + fileName;
    }


    public Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public List<String> getTables(Connection connection) {
        List<String> tables = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getTables(null, null, null, null)) {
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public MyTableModel execute (String query) throws SQLException {
            MyTableModel result;

        try (PreparedStatement statement = this.connect().prepareStatement(query)) {

            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            List<Object[]> data = new ArrayList<>();

            while(rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);

                }
                data.add(row);
            }
            result = new MyTableModel(columnNames, data);

        } catch (SQLException e) {
           throw new SQLException();
        }

        return result;
    }
}
