package com.infilos.mantou.views.setting.h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connectivity {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./data/data;AUTO_SERVER=TRUE";

    // Database credentials
    private static final String USER = "mantou";
    private static final String PASS = "mantou";

    private Connection conn;
    private Statement stmt;

    public static final Connectivity INSTANCE = new Connectivity();

    private Connectivity() {
    }

    public void dbConnect() throws ClassNotFoundException {
        conn = null;
        stmt = null;

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Database successfully connected...");
    }

    public void dbDisconnect() {
        try {
            // Clean-up environment
            System.out.println("Disconnecting from database...");
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try

        System.out.println("Database successfully disconnected...");
    }

    public Statement getStmt() {
        return stmt;
    }

    public Connection getConn() {
        return conn;
    }
}
