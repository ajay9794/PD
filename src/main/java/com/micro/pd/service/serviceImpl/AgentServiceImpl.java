package com.micro.pd.service.serviceImpl;

import com.micro.pd.constants.Constants;
import com.micro.pd.database.DatabaseHelper;
import com.micro.pd.modelDTO.Agency;
import com.micro.pd.modelDTO.ResponseDto;
import com.micro.pd.service.AgentService;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AgentServiceImpl implements AgentService {
    @Override
    public ResponseDto logIn(Agency agencyDetails) {
        ResponseDto responseObject = new ResponseDto();
        try {
            JSONObject localJSONObject1 = new JSONObject();

            localJSONObject1.put("userId", agencyDetails.getUsername());
            localJSONObject1.put("isMobileRequired", true);
            localJSONObject1.put("captcha", "");
            localJSONObject1.put("dob", agencyDetails.getDateOfBirth() + "T00:00:00");

//            localJSONObject1.put("userId", "0145393A");
//            localJSONObject1.put("isMobileRequired", true);
//            localJSONObject1.put("captcha", "");
//            localJSONObject1.put("dob", "1989-05-01T00:00:00");

//            localJSONObject1.put("dob", Constants.getYYYMMDD(agencyDetails.get)+"T00:00:00")
//            JSONObject localJSONObject1 = new JSONObject();
//            localJSONObject1.put("userId", agencyCode);
//            localJSONObject1.put("isMobileRequired", true);
//            localJSONObject1.put("captcha", "");
//            StringBuilder localStringBuilder = new StringBuilder();
//            localStringBuilder.append(Constants.getYYYMMDD(strDOB)+"T00:00:00");

//            localJSONObject1.put("dob", localStringBuilder.toString());
            String encoder= Constants.encodeResponse(localJSONObject1.toString());

            String getURL =  "https://mbiz.licindia.in/BFFService/prelogin/forgot/generate";
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            RequestBody body = RequestBody.create(JSON, encoder);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(getURL)
                    .post(body)

                    .addHeader("accept-encryption", "EDB")
                    .addHeader("LOCALE", "IN_En")
                    .addHeader("Referer", "https://mbiz.licindia.in/AgentPortal/")
                    .addHeader("Channel", "TEST")
                    .addHeader("Host", "mbiz.licindia.in")
                    //Notice this request has header if you don't need to send a header just erase this part
                    .build();
            String responseStringJson="";
            try {
                Response response = client.newCall(request).execute();
                String token = response.body().string().replaceAll("[\\n\\r]", "");
                responseStringJson=Constants.decodeResponse(token);
                System.out.println(responseStringJson);
                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
            }


//            String strOtpReference="";


            if (responseStringJson.contains(""))

                if ((responseStringJson.contains("Invalid DOB") | responseStringJson.contains("Invalid Date Of Birth"))) {
                    responseObject.setDetailsMessage("Invalid DOB");
                    responseObject.setIsError(true);
                    return responseObject;
                }

            if (responseStringJson.contains("Active LIC Agents are required to submit written request")) {
                responseObject.setDetailsMessage("Active LIC Agents are required to submit written request\"");
                responseObject.setIsError(true);
                return responseObject;
            }
            if (responseStringJson.contains("The entered Password is invalid")) {
                responseObject.setDetailsMessage("Invalid Password");
                responseObject.setIsError(true);
                return responseObject;
            }
            if ((responseStringJson.contains("Unable to connect to the network") )) {
                responseObject.setDetailsMessage("Network Problem");
                responseObject.setIsError(true);
                return responseObject;
            }
            if ((responseStringJson.contains("UserID Disabled"))) {
                responseObject.setDetailsMessage("UserID Disabled");
                responseObject.setIsError(true);
                return responseObject;
            }
            if ((responseStringJson.contains("Invalid UserID/Password"))) {
                responseObject.setDetailsMessage("Invalid UserID/Password");
                responseObject.setIsError(true);
                return responseObject;
            }
            if ((responseStringJson.contains("Invalid UserID"))) {
                responseObject.setDetailsMessage("Invalid UserID");
                responseObject.setIsError(true);
                return responseObject;
            }
            responseObject.setLogInResponseJson(new JSONObject(responseStringJson));
            responseObject.setResponseString(responseStringJson);
            this.saveAgencyData(agencyDetails);
//            String[] arrayOfString1 = str1.replace("{", "").replace("}", "").replace("\"", "").split(",");
//
//            for (int k = 0; k < arrayOfString1.length; k++) {
//                String[] arrayOfString2 = arrayOfString1[k].split(":");
//                if (arrayOfString2[0].contains("idamId"))
//                    NBDownloadActivity.this.strAdamId = arrayOfString2[1];
//
//                else if (arrayOfString2[0].contains("mobileOTPReference"))
//                    NBDownloadActivity.this.strOtpReference = arrayOfString2[1];
//                else if (arrayOfString2[0].contains("mobileNo"))
//                    NBDownloadActivity.this.mobileno = arrayOfString2[1];
//                else if (arrayOfString2[0].contains("emailId"))
//                    NBDownloadActivity.this.emailid = arrayOfString2[1];
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseObject;
    }

    @Override
    public ResponseDto validateOTP(String otpText,  ResponseDto logInResponse) {
        try{
            JSONObject localJSONObject = new JSONObject();
            localJSONObject.put("idamId", logInResponse.getLogInResponseJson().get("idamId"));
            localJSONObject.put("otp", otpText); //user enter otp
            localJSONObject.put("otpReference", logInResponse.getLogInResponseJson().get("mobileOTPReference"));
            String encoder=Constants.encodeResponse(localJSONObject.toString());
            String getURL = "https://mbiz.licindia.in//BFFService/login/validateotp";
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, encoder);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(getURL)
                    .post(body)
                    .addHeader("accept-encryption", "EDB")
                    .addHeader("LOCALE", "IN_En")
                    .addHeader("Referer", "https://mbiz.licindia.in/AgentPortal/")
                    .addHeader("Channel", "TEST")
                    .addHeader("Host", "mbiz.licindia.in")
                    //Notice this request has header if you don't need to send a header just erase this part
                    .build();

            Response response = client.newCall(request).execute();
            String token = response.body().string().replaceAll("[\\n\\r]", "");

            String str= Constants.decodeResponse(token);

            if (str.contains("Invalid OTP")) {
                logInResponse.setDetailsMessage("Invalid OTP");
                logInResponse.setIsError(true);
                return logInResponse;
            }
            if (str.contains("Unable to connect to the network")) {
                logInResponse.setDetailsMessage("Problem with lic site, try later..");
                logInResponse.setIsError(true);
                return logInResponse;
            }
            /** success validation response */
            logInResponse.setValidateOtpResponseJson(new JSONObject(str));
            logInResponse.setIsError(false);
        } catch(Exception e) {
            if ((e.getMessage().contains("Proxy Error") | e.getMessage().contains("Request Time-out"))){
                logInResponse.setDetailsMessage("Proxy Error");
                logInResponse.setIsError(true);
            } else {
                logInResponse.setDetailsMessage(e.getMessage().toString());
                logInResponse.setIsError(true);
            }
            e.printStackTrace();
        }
        return logInResponse;
    }

    @Override
    public ResponseDto downloadPolicyList(Agency selectedAgency, ResponseDto asyncResponse) {
        try{
            String str1 = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
            String str2 = URLEncoder.encode(Constants.Encrypt("doc", Constants.key), "utf-8");

            String str3 = URLEncoder.encode(Constants.Encrypt(selectedAgency.getFromDate(),  Constants.key), "utf-8");
            String str4 = URLEncoder.encode(Constants.Encrypt(selectedAgency.getToDate(),  Constants.key), "utf-8");
            StringBuilder localStringBuilder = new StringBuilder();
            //  localStringBuilder.append("https://ebiz.licindia.in/BFFService/policyenquiry/policysearch?_dc=");
            localStringBuilder.append("https://mbiz.licindia.in/AgentBFFService/agents/policysearch?_dc=");
            localStringBuilder.append(str1);
            localStringBuilder.append("&context=");
            localStringBuilder.append(str2);
            localStringBuilder.append("&startDoc=");
            localStringBuilder.append(str3);
            localStringBuilder.append("&endDoc=");
            localStringBuilder.append(str4);
            localStringBuilder.append("&count=0&page=1&start=0&limit=0");


            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(localStringBuilder.toString())
                    .addHeader("accept-encryption", "EDB")
                    .addHeader("LOCALE", "IN_En")
                    .addHeader("Referer", "https://mbiz.licindia.in/AgentPortal/")
                    .addHeader("Channel", "TEST")
                    .addHeader("Host", "mbiz.licindia.in")
                    .addHeader("Authorization", asyncResponse.getValidateOtpResponseJson().get("accesstoken").toString())
                    //Notice this request has header if you don't need to send a header just erase this part
                    .build();
            Response response = client.newCall(request).execute();
            String token = response.body().string().replaceAll("[\\n\\r]", "");
            String resultString =  Constants.decodeResponse(token);
            /** success download policy details **/
            asyncResponse.setDownloadPolicyResponseJson(new JSONObject(resultString));
            asyncResponse.setIsError(false);
            JSONArray policyList = new JSONObject(resultString).optJSONArray("policySearch");
            this.savePolicyData(policyList);
        } catch (Exception e) {
            asyncResponse.setIsError(true);
            if(e.getMessage().toString().equalsIgnoreCase("timeout")){
                asyncResponse.setDetailsMessage("socket");
            } else {
                asyncResponse.setDetailsMessage("error");
            }
        }
        return asyncResponse;
    }


    @Override
    public ResponseDto updateMaskedPolicyNumber(ResponseDto asyncResponse) {
        try {

            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("https://mbiz.licindia.in/AgentBFFService/agents/allpolicies");
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(localStringBuilder.toString())
                    .addHeader("accept-encryption", "EDB")
                    .addHeader("LOCALE", "IN_En")
                    .addHeader("Referer", "https://mbiz.licindia.in/AgentPortal/")
                    .addHeader("Channel", "TEST")
                    .addHeader("Host", "mbiz.licindia.in")
                    .addHeader("Authorization",asyncResponse.getValidateOtpResponseJson().get("accesstoken").toString()) //token  // validation --> next oart
                    //Notice this request has header if you don't need to send a header just erase this part
                    .build();
            Response response = client.newCall(request).execute();
            String token = response.body().string().replaceAll("[\\n\\r]", "");
            String resultString =  Constants.decodeResponse(token);

            boolean bool1 = resultString.contains("200");
            if(bool1) {
                asyncResponse.setUpdateMaskedPolicyNumberResponseJson(new JSONObject(resultString));
                asyncResponse.setIsError(false);
                JSONArray policyList = new JSONObject(resultString).optJSONArray("policies");
                this.updatePolicyData(policyList);
            }
        } catch(Exception e) {
            asyncResponse.setIsError(false);
            asyncResponse.setDetailsMessage("error");
        }
        return asyncResponse;
    }

    @Override
    public List<Agency> getAgencyStoredData() {
        return DatabaseHelper.getStoredAgency();
    }

    private void savePolicyData(JSONArray policyList) {
        DatabaseHelper.savePolicyData(policyList);
    }
    private void updatePolicyData(JSONArray policyList) {
        DatabaseHelper.updatePolicyData(policyList);
    }
    public void saveAgencyData(Agency agencyDetails) {
        DatabaseHelper.insertAgencyData(agencyDetails);
    }
}
