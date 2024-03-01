package com.micro.pd.database;

import com.micro.pd.modelDTO.Agency;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Agency> getStoredAgency() {
        List<Agency> storedAgency = new ArrayList<>();
        String sql = GET_Agencies_Table_Data;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Agency agency = new Agency();
                agency.setAgency(resultSet.getString("Agency"));
                agency.setAgencyName(resultSet.getString("Agency"));
                agency.setUsername(resultSet.getString("Username"));
                agency.setPassword(resultSet.getString("Password"));
                agency.setDateOfBirth(resultSet.getString("DOB"));
                agency.setMpin(resultSet.getInt("MPIN"));
                agency.setMobileNo(resultSet.getString("MobileNo"));
                agency.setMobileNoKey(resultSet.getString("MobileNoKey"));
                agency.setUserKey(resultSet.getString("UserKey"));
                storedAgency.add(agency);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storedAgency;
    }

    public static void savePolicyData(JSONArray policyList) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             Statement statement = connection.createStatement()) {
            // Iterate through the list of policy
            for (int j = 0; j < policyList.length(); j++) {
                JSONObject policyObject = policyList.getJSONObject(j);
                // Check if the policy already exists in the table
                if (!isPolicyRecordExist(connection, policyObject)) {
                    // If not exists, insert the policy into the table
                    insertPolicy(connection, policyObject);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePolicyData(JSONArray policyList) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             Statement statement = connection.createStatement()) {
            // Iterate through the list of policy
            for (int j = 0; j < policyList.length(); j++) {
                JSONObject policyObject = policyList.getJSONObject(j);
                updatePolicy(connection, policyObject);
//                // Check if the policy already exists in the table
//                if (!isPolicyRecordExist(connection, policyObject)) {
//                    // If exists, update the policy into the table
//                    updatePolicy(connection, policyObject);
//                } else {
//                    // If not exists, insert the policy into the table
//                    insertPolicy(connection, policyObject);
//                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updatePolicy(Connection connection, JSONObject policyObject) throws SQLException{
        String query = "UPDATE PolicyCalendar SET " +
                "PolicyNo = ?, " +
                "Email1 = ?, " +
                "Mobile1 = ?, " +
                "Mobile2 = '', " +
                "Email2 = '', " +
                "Age = ?, " +
                "PPT = ?, " +
                "DOB = ?, " +
                "branch = ? " +
                "WHERE SUBSTR(PolicyNoMask, 6, 4) = ? AND PlanNo = ? AND Term = ? AND SA = ? AND FUPDate = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String policyNo = policyObject.getString("policyNumber").replace("\n", "").substring(5);
            String plan = policyObject.getString("plan").replace("\n", "");
            String term = policyObject.getString("policyTerm").replace("\n", "");
            String fupdate = policyObject.getString("nextFup").replace("\n", "");
            String sum = policyObject.getString("sumAssured").replace("\n", "");

            preparedStatement.setString(1,  policyObject.getString("policyNumber"));
            preparedStatement.setString(2,  policyObject.optString("email", "").trim().replace("\n", ""));
            preparedStatement.setString(3,  policyObject.optString("mobileNo", "").trim().replace("\n", ""));
            preparedStatement.setString(4,  policyObject.getString("ageAtEntry"));
            preparedStatement.setString(5,  policyObject.getString("premiumPayingTerm"));
            preparedStatement.setString(6,  policyObject.getString("dob"));
            preparedStatement.setString(7,  policyObject.getString("serviceBranch").trim());
            preparedStatement.setString(8, policyNo);
            preparedStatement.setString(9, plan);
            preparedStatement.setString(10, term);
            preparedStatement.setString(11, sum);
            preparedStatement.setString(12, fupdate);
            preparedStatement.executeUpdate();
        }
    }

    private static boolean isPolicyRecordExist(Connection connection, JSONObject policyObject) throws SQLException {
        String query = "SELECT COUNT(*) FROM PolicyCalender WHERE PolicyNoMask = ? and Premium = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, (String) policyObject.get("policyno"));
            preparedStatement.setString(2, (String) policyObject.get(policyObject.getString("premiumAmount").replace(".00", "")));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getInt(1) > 0;
        }
    }

    private static void insertPolicy(Connection connection, JSONObject policyObject) throws SQLException {
        String query = "INSERT INTO PolicyCalender (PolicyNoMask,Name,DOC,SA,Term,PremMode,AgencyCode,Premium,PlanNo,PolicyStatus,FUPDate,updateAddress,PolicyNo) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, policyObject.getString("policyno"));
            preparedStatement.setString(2, policyObject.getString("fullName"));
            preparedStatement.setString(3, policyObject.getString("doc"));
            preparedStatement.setString(4, policyObject.getString(".getString(\"sumAssured\").replace(\".00\", \"\")"));
            preparedStatement.setString(5, policyObject.getString("policyTerm").replace(".00", ""));
            preparedStatement.setString(6, policyObject.getString("paymentMode").replace(".00", ""));
            preparedStatement.setString(7, policyObject.getString("agencyCode").trim());
            preparedStatement.setString(8, policyObject.getString("premiumAmount").replace(".00", ""));
            preparedStatement.setString(9, policyObject.getString("planNumber"));
            preparedStatement.setString(10, policyObject.getString("policyStatus"));
            preparedStatement.setString(11, policyObject.getString("firstUnpaid").replace("00:00:00.0","").trim());
            preparedStatement.setBoolean(12, false);
            preparedStatement.setString(13, "0");
            preparedStatement.executeUpdate();
        }
    }
    
}
