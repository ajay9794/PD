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

    String userKey;

    public Agency(String agency, String agencyName, String username, String password, String dateOfBirth, String fromDate, String toDate) {
        this.agency = agency;
        this.agencyName = agencyName;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Agency() {
    }

    public String getAgency() {
        return this.agency;
    }

    public String getAgencyName() {
        return this.agencyName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public String getToDate() {
        return this.toDate;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String toString() {
        return "Agency(agency=" + this.getAgency() + ", agencyName=" + this.getAgencyName() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", dateOfBirth=" + this.getDateOfBirth() + ", fromDate=" + this.getFromDate() + ", toDate=" + this.getToDate() + ")";
    }
}
