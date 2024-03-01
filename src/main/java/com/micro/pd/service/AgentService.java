package com.micro.pd.service;

import com.micro.pd.modelDTO.Agency;
import com.micro.pd.modelDTO.ResponseDto;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

public interface AgentService {
    ResponseDto logIn(Agency agencyDetails);

    ResponseDto validateOTP(String otpText, ResponseDto logInResponse);

    ResponseDto downloadPolicyList(Agency selectedAgency, ResponseDto asyncResponse);

    ResponseDto updateMaskedPolicyNumber(ResponseDto asyncResponse);

    List<Agency> getAgencyStoredData();
}
