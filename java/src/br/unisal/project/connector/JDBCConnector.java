package br.unisal.project.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnector {
    private Connection connection;

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/remote_control",
                "root",
                ""
        );

        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
