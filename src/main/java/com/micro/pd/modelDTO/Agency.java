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

    public int getMpin() {
        return this.mpin;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public String getMobileNoKey() {
        return this.mobileNoKey;
    }

    public String getUserKey() {
        return this.userKey;
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

    public void setMpin(int mpin) {
        this.mpin = mpin;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setMobileNoKey(String mobileNoKey) {
        this.mobileNoKey = mobileNoKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Agency)) return false;
        final Agency other = (Agency) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$agency = this.getAgency();
        final Object other$agency = other.getAgency();
        if (this$agency == null ? other$agency != null : !this$agency.equals(other$agency)) return false;
        final Object this$agencyName = this.getAgencyName();
        final Object other$agencyName = other.getAgencyName();
        if (this$agencyName == null ? other$agencyName != null : !this$agencyName.equals(other$agencyName))
            return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$dateOfBirth = this.getDateOfBirth();
        final Object other$dateOfBirth = other.getDateOfBirth();
        if (this$dateOfBirth == null ? other$dateOfBirth != null : !this$dateOfBirth.equals(other$dateOfBirth))
            return false;
        final Object this$fromDate = this.getFromDate();
        final Object other$fromDate = other.getFromDate();
        if (this$fromDate == null ? other$fromDate != null : !this$fromDate.equals(other$fromDate)) return false;
        final Object this$toDate = this.getToDate();
        final Object other$toDate = other.getToDate();
        if (this$toDate == null ? other$toDate != null : !this$toDate.equals(other$toDate)) return false;
        if (this.getMpin() != other.getMpin()) return false;
        final Object this$mobileNo = this.getMobileNo();
        final Object other$mobileNo = other.getMobileNo();
        if (this$mobileNo == null ? other$mobileNo != null : !this$mobileNo.equals(other$mobileNo)) return false;
        final Object this$mobileNoKey = this.getMobileNoKey();
        final Object other$mobileNoKey = other.getMobileNoKey();
        if (this$mobileNoKey == null ? other$mobileNoKey != null : !this$mobileNoKey.equals(other$mobileNoKey))
            return false;
        final Object this$userKey = this.getUserKey();
        final Object other$userKey = other.getUserKey();
        if (this$userKey == null ? other$userKey != null : !this$userKey.equals(other$userKey)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Agency;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $agency = this.getAgency();
        result = result * PRIME + ($agency == null ? 43 : $agency.hashCode());
        final Object $agencyName = this.getAgencyName();
        result = result * PRIME + ($agencyName == null ? 43 : $agencyName.hashCode());
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $dateOfBirth = this.getDateOfBirth();
        result = result * PRIME + ($dateOfBirth == null ? 43 : $dateOfBirth.hashCode());
        final Object $fromDate = this.getFromDate();
        result = result * PRIME + ($fromDate == null ? 43 : $fromDate.hashCode());
        final Object $toDate = this.getToDate();
        result = result * PRIME + ($toDate == null ? 43 : $toDate.hashCode());
        result = result * PRIME + this.getMpin();
        final Object $mobileNo = this.getMobileNo();
        result = result * PRIME + ($mobileNo == null ? 43 : $mobileNo.hashCode());
        final Object $mobileNoKey = this.getMobileNoKey();
        result = result * PRIME + ($mobileNoKey == null ? 43 : $mobileNoKey.hashCode());
        final Object $userKey = this.getUserKey();
        result = result * PRIME + ($userKey == null ? 43 : $userKey.hashCode());
        return result;
    }

    public String toString() {
        return "Agency(agency=" + this.getAgency() + ", agencyName=" + this.getAgencyName() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", dateOfBirth=" + this.getDateOfBirth() + ", fromDate=" + this.getFromDate() + ", toDate=" + this.getToDate() + ", mpin=" + this.getMpin() + ", mobileNo=" + this.getMobileNo() + ", mobileNoKey=" + this.getMobileNoKey() + ", userKey=" + this.getUserKey() + ")";
    }
}
