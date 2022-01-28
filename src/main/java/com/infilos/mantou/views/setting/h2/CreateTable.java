package com.infilos.mantou.views.setting.h2;

import java.sql.SQLException;

public class CreateTable {

    public void createTable(String tableName) {
        // Execute a query
        System.out.println("Crweating table in given database...");
        String sql = "CREATE TABLE \"" + tableName + "\" (data VARCHAR(255))";

        try {
            Connectivity.INSTANCE.getStmt().executeUpdate(sql);

            System.out.println("Created table in given database...");
        } catch (SQLException e) {
            //check if created table name already exists
            if (e.getErrorCode() == 42101) {
                System.out.println("Table \"" + tableName + "\" already exists");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void deleteTable(String tableName) {
        // Execute a query
        String sql = "DROP TABLE IF EXISTS \"" + tableName + "\"";
        try {
            Connectivity.INSTANCE.getStmt().executeUpdate(sql);
            System.out.println("Successfully deleted \"" + tableName + "\" from database...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}