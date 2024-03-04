package com.micro.pd.modelDTO;


import org.json.JSONObject;

public class ResponseDto {
    String responseString;
    String detailsMessage;

    String errorMessage;
    Boolean isError;

    JSONObject logInResponseJson;

    JSONObject validateOtpResponseJson;

    JSONObject downloadPolicyResponseJson;

    JSONObject updateMaskedPolicyNumberResponseJson;

    public ResponseDto() {
    }


    public String getResponseString() {
        return this.responseString;
    }

    public String getDetailsMessage() {
        return this.detailsMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Boolean getIsError() {
        return this.isError;
    }

    public JSONObject getLogInResponseJson() {
        return this.logInResponseJson;
    }

    public JSONObject getValidateOtpResponseJson() {
        return this.validateOtpResponseJson;
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

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    public void setLogInResponseJson(JSONObject logInResponseJson) {
        this.logInResponseJson = logInResponseJson;
    }

    public void setValidateOtpResponseJson(JSONObject validateOtpResponseJson) {
        this.validateOtpResponseJson = validateOtpResponseJson;
    }

    public void setDownloadPolicyResponseJson(JSONObject downloadPolicyResponseJson) {
        this.downloadPolicyResponseJson = downloadPolicyResponseJson;
    }

    public void setUpdateMaskedPolicyNumberResponseJson(JSONObject updateMaskedPolicyNumberResponseJson) {
        this.updateMaskedPolicyNumberResponseJson = updateMaskedPolicyNumberResponseJson;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ResponseDto)) return false;
        final ResponseDto other = (ResponseDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$responseString = this.getResponseString();
        final Object other$responseString = other.getResponseString();
        if (this$responseString == null ? other$responseString != null : !this$responseString.equals(other$responseString))
            return false;
        final Object this$detailsMessage = this.getDetailsMessage();
        final Object other$detailsMessage = other.getDetailsMessage();
        if (this$detailsMessage == null ? other$detailsMessage != null : !this$detailsMessage.equals(other$detailsMessage))
            return false;
        final Object this$errorMessage = this.getErrorMessage();
        final Object other$errorMessage = other.getErrorMessage();
        if (this$errorMessage == null ? other$errorMessage != null : !this$errorMessage.equals(other$errorMessage))
            return false;
        final Object this$isError = this.getIsError();
        final Object other$isError = other.getIsError();
        if (this$isError == null ? other$isError != null : !this$isError.equals(other$isError)) return false;
        final Object this$logInResponseJson = this.getLogInResponseJson();
        final Object other$logInResponseJson = other.getLogInResponseJson();
        if (this$logInResponseJson == null ? other$logInResponseJson != null : !this$logInResponseJson.equals(other$logInResponseJson))
            return false;
        final Object this$validateOtpResponseJson = this.getValidateOtpResponseJson();
        final Object other$validateOtpResponseJson = other.getValidateOtpResponseJson();
        if (this$validateOtpResponseJson == null ? other$validateOtpResponseJson != null : !this$validateOtpResponseJson.equals(other$validateOtpResponseJson))
            return false;
        final Object this$downloadPolicyResponseJson = this.getDownloadPolicyResponseJson();
        final Object other$downloadPolicyResponseJson = other.getDownloadPolicyResponseJson();
        if (this$downloadPolicyResponseJson == null ? other$downloadPolicyResponseJson != null : !this$downloadPolicyResponseJson.equals(other$downloadPolicyResponseJson))
            return false;
        final Object this$updateMaskedPolicyNumberResponseJson = this.getUpdateMaskedPolicyNumberResponseJson();
        final Object other$updateMaskedPolicyNumberResponseJson = other.getUpdateMaskedPolicyNumberResponseJson();
        if (this$updateMaskedPolicyNumberResponseJson == null ? other$updateMaskedPolicyNumberResponseJson != null : !this$updateMaskedPolicyNumberResponseJson.equals(other$updateMaskedPolicyNumberResponseJson))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ResponseDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $responseString = this.getResponseString();
        result = result * PRIME + ($responseString == null ? 43 : $responseString.hashCode());
        final Object $detailsMessage = this.getDetailsMessage();
        result = result * PRIME + ($detailsMessage == null ? 43 : $detailsMessage.hashCode());
        final Object $errorMessage = this.getErrorMessage();
        result = result * PRIME + ($errorMessage == null ? 43 : $errorMessage.hashCode());
        final Object $isError = this.getIsError();
        result = result * PRIME + ($isError == null ? 43 : $isError.hashCode());
        final Object $logInResponseJson = this.getLogInResponseJson();
        result = result * PRIME + ($logInResponseJson == null ? 43 : $logInResponseJson.hashCode());
        final Object $validateOtpResponseJson = this.getValidateOtpResponseJson();
        result = result * PRIME + ($validateOtpResponseJson == null ? 43 : $validateOtpResponseJson.hashCode());
        final Object $downloadPolicyResponseJson = this.getDownloadPolicyResponseJson();
        result = result * PRIME + ($downloadPolicyResponseJson == null ? 43 : $downloadPolicyResponseJson.hashCode());
        final Object $updateMaskedPolicyNumberResponseJson = this.getUpdateMaskedPolicyNumberResponseJson();
        result = result * PRIME + ($updateMaskedPolicyNumberResponseJson == null ? 43 : $updateMaskedPolicyNumberResponseJson.hashCode());
        return result;
    }

    public String toString() {
        return "ResponseDto(responseString=" + this.getResponseString() + ", detailsMessage=" + this.getDetailsMessage() + ", errorMessage=" + this.getErrorMessage() + ", isError=" + this.getIsError() + ", logInResponseJson=" + this.getLogInResponseJson() + ", validateOtpResponseJson=" + this.getValidateOtpResponseJson() + ", downloadPolicyResponseJson=" + this.getDownloadPolicyResponseJson() + ", updateMaskedPolicyNumberResponseJson=" + this.getUpdateMaskedPolicyNumberResponseJson() + ")";
    }
}
