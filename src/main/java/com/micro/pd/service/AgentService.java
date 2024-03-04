package com.micro.pd.service;

import com.micro.pd.modelDTO.Agency;
import com.micro.pd.modelDTO.ResponseDto;

import java.util.List;

public interface AgentService {
    ResponseDto logIn(Agency agencyDetails);

    ResponseDto validateOTP(String otpText, ResponseDto logInResponse);

    ResponseDto downloadPolicyListOrUpdateFUP(Agency selectedAgency, ResponseDto asyncResponse, boolean updateFUP);

    ResponseDto updateMaskedPolicyNumber(Agency selectedAgency, ResponseDto asyncResponse);

    List<Agency> getAgencyStoredData();

    ResponseDto addressAndNomineeUpdate(Agency agencyDetails, ResponseDto responseDto);

    ResponseDto updatePolicy(Agency agencyDetails, ResponseDto responseDto);

}
