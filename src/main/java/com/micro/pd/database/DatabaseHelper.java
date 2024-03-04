package com.micro.pd.database;

import com.micro.pd.modelDTO.Agency;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.micro.pd.constants.QueryConstant.*;

public class DatabaseHelper {
    private static final String DATABASE_NAME = "pd.db";
    public static final String JDBC_URL = "jdbc:sqlite:" + DATABASE_NAME;

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

    public static boolean agencyNotAvailable(Connection connection, Agency agency) throws SQLException {
        int count = 0;
        String sql = Get_Agency_Specific_Count;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, agency.getUsername());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
            return count > 0;
        }
    }

    public static void insertAgencyData(Agency agency) {
        String sql = Insert_Table_Agency_Data;

        try (Connection connection = getConnection()) {
            if(agencyNotAvailable(connection, agency)) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, agency.getAgency());
                preparedStatement.setString(2, agency.getUsername());
                preparedStatement.setString(3, agency.getPassword());
                preparedStatement.setString(4, agency.getDateOfBirth());
                preparedStatement.setInt(5, agency.getMpin());
                preparedStatement.setString(6, agency.getMobileNo());
                preparedStatement.setString(7, agency.getMobileNoKey());
                preparedStatement.setString(8, agency.getUserKey());
                preparedStatement.executeUpdate();
            }
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

    public static void savePolicyOrUpdateFupData(JSONArray policyList, boolean updateFUP) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             Statement ignored = connection.createStatement()) {
            // Iterate through the list of policy
            for (int j = 0; j < policyList.length(); j++) {
                JSONObject policyObject = policyList.getJSONObject(j);
                // Check if the policy already exists in the table
                if (!isPolicyRecordExist(connection, policyObject)) {
                    // If not exists, insert the policy into the table
                    insertPolicy(connection, policyObject);
                } else if(BooleanUtils.isTrue(updateFUP)) {
                    updatePolicyFUP(connection, policyObject);
                }
            }

        } catch (Exception e) {
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
        String query = "SELECT COUNT(*) FROM PolicyCalendar WHERE PolicyNoMask = ? and Premium = ?";
        int count = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, policyObject.getString("policyno"));
            preparedStatement.setString(2, policyObject.getString("premiumAmount").replace(".00", ""));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
            return count > 0;
        }
    }

    private static void updatePolicyFUP(Connection connection, JSONObject policyObject) throws SQLException {
        String query = "UPDATE PolicyCalendar SET " +
                "FUPDate = ?, " +
                "WHERE PolicyNoMask = ? AND Premium ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, StringUtils.isNotEmpty(policyObject.getString("firstUnpaid")) ? policyObject.getString("firstUnpaid").replace("00:00:00.0","").trim() : null);
            preparedStatement.setString(2, policyObject.getString("policyno"));
            preparedStatement.setString(3, StringUtils.isNotEmpty(policyObject.getString("premiumAmount")) ? policyObject.getString("premiumAmount").replace(".00", "") : null);
            preparedStatement.executeUpdate();
        }
    }
    private static void insertPolicy(Connection connection, JSONObject policyObject) throws SQLException {
        String query = "INSERT INTO PolicyCalendar (PolicyNoMask,Name,DOC,SA,Term,PremMode,AgencyCode,Premium,PlanNo,PolicyStatus,FUPDate,updateAddress,PolicyNo) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, policyObject.getString("policyno"));
            preparedStatement.setString(2, policyObject.getString("fullName"));
            preparedStatement.setString(3, policyObject.getString("doc"));
            preparedStatement.setString(4, StringUtils.isNotEmpty(policyObject.getString("sumAssured")) ? policyObject.getString("sumAssured").replace(".00", "") : null);
            preparedStatement.setString(5, StringUtils.isNotEmpty(policyObject.getString("policyTerm")) ? policyObject.getString("policyTerm").replace(".00", "") : null);
            preparedStatement.setString(6, StringUtils.isNotEmpty(policyObject.getString("paymentMode")) ? policyObject.getString("paymentMode").replace(".00", "") : null);
            preparedStatement.setString(7, StringUtils.isNotEmpty(policyObject.getString("agencyCode")) ? policyObject.getString("agencyCode").trim() : null);
            preparedStatement.setString(8, StringUtils.isNotEmpty(policyObject.getString("premiumAmount")) ? policyObject.getString("premiumAmount").replace(".00", "") : null);
            preparedStatement.setString(9, policyObject.getString("planNumber"));
            preparedStatement.setString(10, policyObject.getString("policyStatus"));
            preparedStatement.setString(11, StringUtils.isNotEmpty(policyObject.getString("firstUnpaid")) ? policyObject.getString("firstUnpaid").replace("00:00:00.0","").trim() : null);
            preparedStatement.setBoolean(12, false);
            preparedStatement.setString(13, "0");
            preparedStatement.executeUpdate();
        }
    }

    public static List<JSONObject> getPolicyForAddressAndNomineeUpdate(Agency agency) {
        List<JSONObject> policyList = new ArrayList<>();
        String sql = "select PolicyNo,Premium from PolicyCalendar where AgencyCode = ? and (updateAddress != 1 or updateAddress='false') and Policyno != '0' ";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, agency.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JSONObject object = new JSONObject();
                object.put("PolicyNo", resultSet.getString("PolicyNo"));
                object.put("Premium", resultSet.getString("Premium"));
                policyList.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policyList;
    }

    public static void updatePolicyDataDynamicallyQueryConstruct(Connection connection, JSONObject values, String policyNo) {
        if(StringUtils.isNotEmpty(policyNo)) {

            StringBuilder query = new StringBuilder();
            query.append("UPDATE PolicyCalendar SET ");
            String delimiter = ", ";
            for (String key : values.keySet()) {
                query.append(key).append("=").append("?").append(delimiter);
            }

            // Remove the trailing delimiter
            if (query.length() > 0) {
                query.setLength(query.length() - delimiter.length());
            }

            query.append(" WHERE ").append("PolicyNo").append("=?");

            try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
                int parameterIndex = 1;
                for (String key : values.keySet()) {
                    setParameter(preparedStatement, parameterIndex++, values.get(key));
                }
                setParameter(preparedStatement, parameterIndex, policyNo);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setParameter(PreparedStatement preparedStatement, int parameterIndex, Object value) throws SQLException {
        if (value instanceof String) {
            preparedStatement.setString(parameterIndex, (String) value);
        } else if (value instanceof Boolean) {
            preparedStatement.setBoolean(parameterIndex, (Boolean) value);
        } else if (value instanceof Integer) {
            preparedStatement.setInt(parameterIndex, (Integer) value);
        } else {
            // Handle other data types as needed
            preparedStatement.setString(parameterIndex, value.toString());
        }
    }

    public static List<JSONObject> getPolicyForUpdatePolicy(Agency agency) {
        List<JSONObject> policyList = new ArrayList<>();
        String sql = "select PolicyNoMask,Premium from PolicyCalendar where AgencyCode= ? and PolicyNo='0'";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, agency.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JSONObject object = new JSONObject();
                object.put("PolicyNoMask", resultSet.getString("PolicyNoMask"));
                object.put("Premium", resultSet.getString("Premium"));
                policyList.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policyList;
    }

    public static void updateMaskPolicyNo(Connection connection, JSONObject values, String policyNoMask, String premium) {
        if(StringUtils.isNotEmpty(policyNoMask) && StringUtils.isNotEmpty(values.getString("PolicyNo"))) {

            StringBuilder query = new StringBuilder();
            query.append("UPDATE PolicyCalendar SET PolicyNo = ? WHERE PolicyNoMask = ? AND Premium = ?");

            try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
                int parameterIndex = 1;
                setParameter(preparedStatement, parameterIndex++, values.getString("PolicyNo"));
                setParameter(preparedStatement, parameterIndex, policyNoMask);
                setParameter(preparedStatement, parameterIndex, premium);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<JSONObject> getAllPolicies() {
        List<JSONObject> policyList = new ArrayList<>();
        String query = GET_Policy_calender_Table_Data;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JSONObject policyObject = new JSONObject();
                policyObject.put("PolicyNo" , resultSet.getString("PolicyNo"));
                policyObject.put("Name" , resultSet.getString("Name"));
                policyObject.put("Gender" , resultSet.getString("Gender"));
                policyObject.put("DOB" , resultSet.getString("DOB"));
                policyObject.put("Age" , resultSet.getString("Age"));
                policyObject.put("DOC" , resultSet.getString("DOC"));
                policyObject.put("PlanNo" , resultSet.getString("PlanNo"));
                policyObject.put("Term" , resultSet.getString("Term"));
                policyObject.put("PPT" , resultSet.getString("PPT"));
                policyObject.put("PremMode" , resultSet.getString("PremMode"));
                policyObject.put("SA" , resultSet.getString("SA"));
                policyObject.put("Premium" , resultSet.getString("Premium"));
                policyObject.put("FUPDate" , resultSet.getString("FUPDate"));
                policyObject.put("PolicyStatus" , resultSet.getString("PolicyStatus"));
                policyObject.put("MaturityDate" , resultSet.getString("MaturityDate"));
                policyObject.put("PremEndDate" , resultSet.getString("PremEndDate"));
                policyObject.put("Mobile1" , resultSet.getString("Mobile1"));
                policyObject.put("Mobile2" , resultSet.getString("Mobile2"));
                policyObject.put("Email1" , resultSet.getString("Email1"));
                policyObject.put("Email2" , resultSet.getString("Email2"));
                policyObject.put("Address" , resultSet.getString("Address"));
                policyObject.put("Address1" , resultSet.getString("Address1"));
                policyObject.put("Address2" , resultSet.getString("Address2"));
                policyObject.put("Pin" , resultSet.getString("Pin"));
                policyObject.put("Nominee" , resultSet.getString("Nominee"));
                policyObject.put("NomineeAge" , resultSet.getString("NomineeAge"));
                policyObject.put("NomineeR" , resultSet.getString("NomineeR"));
                policyObject.put("branch" , resultSet.getString("branch"));
                policyObject.put("AgencyCode" , resultSet.getString("AgencyCode"));

                policyList.add(policyObject);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policyList;
    }
}
