package com.micro.pd.database;

import com.micro.pd.modelDTO.Agency;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.micro.pd.constants.QueryConstant.*;

public class DatabaseHelper {
    private static final String DATABASE_NAME = "pd.db";
    private static final String JDBC_URL = "jdbc:sqlite:" + DATABASE_NAME;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }


    public static void insertData(String name, int age) {
        String sql = Insert_Table_Agency_Data;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet selectAllData() {
        String sql = GET_Table_Data;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            return preparedStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertAgencyData(Agency agency) {
        String sql = Insert_Table_Agency_Data;
//        String sql = "INSERT INTO agencies (agency, username, password, dob, mpin, mobile_no, mobile_no_key, user_key) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, agency.getAgency());
            preparedStatement.setString(2, agency.getUsername());
            preparedStatement.setString(3, agency.getPassword());
            preparedStatement.setString(4, agency.getDateOfBirth());
            preparedStatement.setInt(5, agency.getMpin());
            preparedStatement.setString(6, agency.getMobileNo());
            preparedStatement.setString(7, agency.getMobileNoKey());
            preparedStatement.setString(8, agency.getUserKey());


            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
