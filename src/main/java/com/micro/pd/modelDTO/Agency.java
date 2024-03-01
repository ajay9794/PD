package com.micro.pd.modelDTO;

public class Agency {
    String agency;
    String agencyName;
    String username;
    String password;
    String dateOfBirth;
    String fromDate;
    String toDate;
    int mpin;
    String mobileNo;
    String mobileNoKey;
    String userKey;


    public Agency() {
    }

    public Agency(String agency, String agencyName, String username, String password, String dateOfBirth, String fromDate, String toDate, int mpin, String mobileNo, String mobileNoKey, String userKey) {
        this.agency = agency;
        this.agencyName = agencyName;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.mpin = mpin;
        this.mobileNo = mobileNo;
        this.mobileNoKey = mobileNoKey;
        this.userKey = userKey;
    }

    public Agency(String agency, String agencyName, String username, String password, String dateOfBirth, String fromDate, String toDate) {
        this.agency = agency;
        this.agencyName = agencyName;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }


    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getMpin() {
        return mpin;
    }

    public void setMpin(int mpin) {
        this.mpin = mpin;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMobileNoKey() {
        return mobileNoKey;
    }

    public void setMobileNoKey(String mobileNoKey) {
        this.mobileNoKey = mobileNoKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String toString() {
        return "Agency(agency=" + this.getAgency() + ", agencyName=" + this.getAgencyName() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", dateOfBirth=" + this.getDateOfBirth() + ", fromDate=" + this.getFromDate() + ", toDate=" + this.getToDate() + ")";
    }
}
