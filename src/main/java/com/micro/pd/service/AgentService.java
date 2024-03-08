package com.micro.pd.service;

import com.micro.pd.modelDTO.Agency;
import com.micro.pd.modelDTO.ResponseDto;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.List;

public interface AgentService {
    ResponseDto logIn(Agency agencyDetails);

    ResponseDto validateOTP(String otpText, ResponseDto logInResponse);

    ResponseDto downloadPolicyListOrUpdateFUP(Agency selectedAgency, ResponseDto asyncResponse, boolean updateFUP);

    ResponseDto updateMaskedPolicyNumber(Agency selectedAgency, ResponseDto asyncResponse);

    List<Agency> getAgencyStoredData();

    void addressAndNomineeUpdate(Agency agencyDetails, String accessTokan, JSONObject policyObject, Connection connection) throws Exception;

    ResponseDto updatePolicy(Agency agencyDetails, ResponseDto responseDto);

    void exportPolicyDataIntoJsonFile(String username);
}
