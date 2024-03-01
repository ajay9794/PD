package com.micro.pd.constants;

public final class QueryConstant {

    /** The schema name. */
    public static final String SCHEMA_NAME = "pd";

    /** The Constant SELECT_QUERY. */
    public static final String SELECT_QUERY = "SELECT * FROM ";

    /** The Constant INSERT_QUERY. */
    public static final String INSERT_QUERY = "INSERT INTO ";

    /** The Constant DELETE_QUERY. */
    public static final String DELETE_QUERY = "DELETE FROM ";

    /** The Constant GET_Table_Data */
    public static final String GET_Table_Data = "SELECT * FROM your_table_name";

    /** The Constant GET_Table_Data */
    public static final String GET_Agencies_Table_Data = "SELECT * FROM Agencies";

    /** The Constant Insert_Table_Data */
    public static final String Insert_Table_Agency_Data = "INSERT INTO Agencies (Agency, Username, Password, DOB, MPIN, MobileNo, MobileNoKey, UserKey) VALUES (?,?,?,?,?,?,?,?)";
}
