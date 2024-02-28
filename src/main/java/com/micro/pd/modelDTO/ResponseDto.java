package com.micro.pd.modelDTO;


import org.json.JSONObject;

public class ResponseDto {
    String responseString;
    String detailsMessage;
    Boolean isError;

    JSONObject logInResponseJson;

    JSONObject validateOtpResponseJson;

    JSONObject downloadPolicyResponseJson;

    JSONObject updateMaskedPolicyNumberResponseJson;


    public ResponseDto() {
    }

    public JSONObject getValidateOtpResponseJson() {
        return validateOtpResponseJson;
    }

    public void setValidateOtpResponseJson(JSONObject validateOtpResponseJson) {
        this.validateOtpResponseJson = validateOtpResponseJson;
    }

    public String getResponseString() {
        return this.responseString;
    }

    public String getDetailsMessage() {
        return this.detailsMessage;
    }

    public Boolean getIsError() {
        return this.isError;
    }

    public JSONObject getLogInResponseJson() {
        return this.logInResponseJson;
    }

    public JSONObject getDownloadPolicyResponseJson() {
        return this.downloadPolicyResponseJson;
    }

    public JSONObject getUpdateMaskedPolicyNumberResponseJson() {
        return this.updateMaskedPolicyNumberResponseJson;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public void setDetailsMessage(String detailsMessage) {
        this.detailsMessage = detailsMessage;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    public void setLogInResponseJson(JSONObject logInResponseJson) {
        this.logInResponseJson = logInResponseJson;
    }

    public void setDownloadPolicyResponseJson(JSONObject downloadPolicyResponseJson) {
        this.downloadPolicyResponseJson = downloadPolicyResponseJson;
    }

    public void setUpdateMaskedPolicyNumberResponseJson(JSONObject updateMaskedPolicyNumberResponseJson) {
        this.updateMaskedPolicyNumberResponseJson = updateMaskedPolicyNumberResponseJson;
    }
}
