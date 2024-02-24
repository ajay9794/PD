package com.micro.pd.database;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.micro.pd.constants.QueryConstant.*;

public class DatabaseHelper {

    static URL databaseUrl = DatabaseHelper.class.getResource("/pd.db");
    private static final String DATABASE_URL = new File(databaseUrl.getPath()).getAbsolutePath();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void insertData(String name, int age) {
        String sql = Insert_Table_Data;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet selectAllData() {
        String sql = GET_Table_Data;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
