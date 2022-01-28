package com.infilos.mantou.views.setting.h2;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertRecord {

    private PreparedStatement prep;

    public void createRecord(String table, String data) {
        // Execute a query
        System.out.println("Creating record in selected table...");
        String sql = "INSERT INTO \"" + table + "\" (data)  values (?)";

        try {
            prep = Connectivity.INSTANCE.getConn().prepareStatement(sql);
            prep.setString(1, data);
            prep.executeUpdate();
            prep.close();

            System.out.println("Created record in selected table...");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (prep != null) {
                try {
                    prep.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteRecord(String table, String record) {
        // Execute a query
        String sql = "DELETE FROM \"" + table + "\" WHERE DATA=?";
        try {
            prep = Connectivity.INSTANCE.getConn().prepareStatement(sql);
            prep.setString(1, record);
            prep.executeUpdate();
            prep.close();

            System.out.println("Successfully deleted \"" + record + "\" from " + table);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (prep != null) {
                try {
                    prep.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
