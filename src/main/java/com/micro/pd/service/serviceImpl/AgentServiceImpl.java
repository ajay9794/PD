package com.micro.pd.service.serviceImpl;

import com.micro.pd.constants.Constants;
import com.micro.pd.database.DatabaseHelper;
import com.micro.pd.modelDTO.Agency;
import com.micro.pd.modelDTO.ResponseDto;
import com.micro.pd.service.AgentService;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
            System.out.println(str);
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
            } else {
                logInResponse.setDetailsMessage(e.getMessage().toString());
            }
            logInResponse.setErrorMessage("OTP validation error");
            logInResponse.setIsError(true);
            e.printStackTrace();
        }
        return logInResponse;
    }

    @Override
    public ResponseDto downloadPolicyListOrUpdateFUP(Agency selectedAgency, ResponseDto asyncResponse, boolean updateFUP) {
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
                    .addHeader("Authorization", asyncResponse.getValidateOtpResponseJson().getString("accesstoken"))
                    //Notice this request has header if you don't need to send a header just erase this part
                    .build();
            Response response = client.newCall(request).execute();
            String token = response.body().string().replaceAll("[\\n\\r]", "");
            String resultString =  Constants.decodeResponse(token);
            System.out.println(resultString);
            /** success download policy details **/
            asyncResponse.setDownloadPolicyResponseJson(new JSONObject(resultString));
            asyncResponse.setIsError(false);
            JSONArray policyList = new JSONObject(resultString).optJSONArray("policySearch");
            this.savePolicyOrUpdateFupData(policyList, updateFUP);
        } catch (Exception e) {
            e.printStackTrace();
            asyncResponse.setIsError(true);
            asyncResponse.setErrorMessage(null);
            if(e.getMessage().toString().equalsIgnoreCase("timeout")){
                asyncResponse.setDetailsMessage("socket");
            } else {
                asyncResponse.setDetailsMessage("error");
            }
        }
        return asyncResponse;
    }


    @Override
    public ResponseDto updateMaskedPolicyNumber(Agency selectedAgency, ResponseDto asyncResponse) {
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
                this.exportPolicyDataIntoJsonFile(selectedAgency.getUsername());
            }
        } catch(Exception e) {
            asyncResponse.setIsError(false);
            asyncResponse.setErrorMessage(null);
            asyncResponse.setDetailsMessage("error");
            e.printStackTrace();
        }
        return asyncResponse;
    }

    @Override
    public void addressAndNomineeUpdate(Agency agencyDetails, String accesstoken, JSONObject policyObject, Connection connection) throws Exception {
        String str1 = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        String strPolicyNo = policyObject.getString("PolicyNo");

        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("https://mbiz.licindia.in/AgentBFFService/agentpolicy/policyInfo?policyNumber=");
        localStringBuilder.append(Constants.encode(strPolicyNo.replace("\n", "")));
        localStringBuilder.append("&dc=");
        localStringBuilder.append(str1);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(localStringBuilder.toString())
                .addHeader("accept-encryption", "EDB")
                .addHeader("LOCALE", "IN_En")
                .addHeader("Referer", "https://mbiz.licindia.in/AgentPortal/")
                .addHeader("Channel", "TEST")
                .addHeader("Host", "mbiz.licindia.in")
                .addHeader("Authorization", accesstoken)
                .build();
        Response response = client.newCall(request).execute();
        String token = response.body().string().replaceAll("[\\n\\r]", "");
        String str5 = Constants.decodeResponse(token);


        boolean bool1 = str5.contains(strPolicyNo);

        if (bool1) {
            /** preparing object to values to update in policyCalender */
            JSONObject values = new JSONObject(); /** This are the values that we need to update in policyCalender table */
            JSONObject jObject = new JSONObject(str5);
            try {
                JSONObject geoObject = jObject.getJSONObject("address");

                values.put("Address", geoObject.getString("address1").replace(",", " ").replace("'", ""));
                values.put("Address1", geoObject.getString("address2").replace(",", " ").replace("'", ""));
                values.put("Address2", geoObject.getString("address3").replace(",", " ").replace("'", ""));
                values.put("Pin", String.valueOf(geoObject.get("pin")));
            } catch (Exception e) {
                System.out.println("Error getting while reading address JSONObject");
            }
            try {
                JSONObject geoObject = jObject.getJSONObject("person");

                String fullname = geoObject.getString("fullName");
                if (fullname != null) {
                    fullname = fullname.replace(",", "");

                    fullname = fullname.trim();
                } else {
                    fullname = "";
                }

                values.put("Name", fullname);

                values.put("Gender", geoObject.getString("sex"));
            } catch (Exception e) {
                System.out.println("Error getting while reading person JSONObject");
            }
            try {
                JSONObject geoObject = jObject.getJSONObject("branch");
                values.put("branch", String.valueOf(geoObject.get("branchCode")).trim());
            } catch (Exception e) {
                System.out.println("Error getting while reading branch JSONObject");
            }
            try {
                JSONObject geoObject = jObject.getJSONObject("policy");

                values.put("PremEndDate", String.valueOf(geoObject.get("dateOfLastPayment")));
                values.put("Age", String.valueOf(geoObject.get("ageAtEntry")));
                values.put("MaturityDate", String.valueOf(geoObject.get("dateOfMaturity")));
                values.put("DOB", String.valueOf(geoObject.get("dob")));
                values.put("ppt", String.valueOf(geoObject.get("premiumPayingTerm")));

                values.put("Term", String.valueOf(geoObject.get("policyTerm")).replace(".00", ""));
                values.put("PolicyStatus", String.valueOf(geoObject.get("statusShortDesc")));
                values.put("FUPDate", String.valueOf(geoObject.get("firstUnpaidPremiumDate")));

            } catch (Exception e) {
                System.out.println("Error getting and setting while reading policy JSONObject");
            }

            values.put("updateAddress", true);
            /** updating values */
            DatabaseHelper.updatePolicyDataDynamicallyQueryConstruct(connection, values, policyObject.getString("PolicyNo"));
        }
    }

    @Override
    public ResponseDto updatePolicy(Agency agencyDetails, ResponseDto oldResponse) {
        ResponseDto responseObject = new ResponseDto();
        try {
            /** select PolicyNoMask,Premium  */
            List<JSONObject> policyList = DatabaseHelper.getPolicyForUpdatePolicy(agencyDetails);
            if (policyList != null && !policyList.isEmpty()) {

                for(JSONObject policyObject : policyList ) {
                    String str1 = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    String str2 = policyObject.getString("PolicyNoMask");

                    StringBuilder localStringBuilder = new StringBuilder();
                    localStringBuilder.append("https://mbiz.licindia.in/AgentBFFService/agentpolicy/policyMaskedInfo?policyNo=");
                    localStringBuilder.append(str2.replace("\n",""));

                    localStringBuilder.append("&instalmentPremium=");
                    localStringBuilder.append(policyObject.getString("Premium"));

                    localStringBuilder.append("&agencyCode=");
                    localStringBuilder.append(agencyDetails.getUsername());
                    localStringBuilder.append("&dc=");
                    localStringBuilder.append(str1);
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(localStringBuilder.toString())
                            .addHeader("accept-encryption", "EDB")
                            .addHeader("LOCALE", "IN_En")
                            .addHeader("Referer", "https://mbiz.licindia.in/AgentPortal/")
                            .addHeader("Channel", "TEST")
                            .addHeader("Host", "mbiz.licindia.in")
                            .addHeader("Authorization", oldResponse.getValidateOtpResponseJson().getString("accesstoken"))
                            .build();

                    Response response = client.newCall(request).execute();
                    String token = response.body().string();
                    String str5= Constants.decodeResponse(token);

                    JSONObject jObject = new JSONObject(str5);
                    String policyno = jObject.getString("policyNo").replace("\n","");

                    System.out.println("Policy "+policyno);
                    if (StringUtils.isNotEmpty(policyno)) {

                        JSONObject values = new JSONObject();
                        try {
                            values.put("PolicyNo", policyno);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        /** updating values */
                        try (Connection connection = DatabaseHelper.getConnection();
                             Statement ignored = connection.createStatement()) {
                            DatabaseHelper.updateMaskPolicyNo(connection, values, policyObject.getString("PolicyNo"), policyObject.getString("Premium"));
                        } catch (SQLException e) {
                            System.out.println("Error getting while connecting database");
                        }
                    }
                };
                responseObject.setIsError(false);
                responseObject.setDetailsMessage("SuccessFully update policy details...");
            } else {
                responseObject.setIsError(false);
                responseObject.setDetailsMessage("policy details are missing...");
            }
        } catch (Exception e) {
            responseObject.setIsError(true);
            responseObject.setErrorMessage(null);
            responseObject.setDetailsMessage("Getting error while updating details...");
        }
        return responseObject;
    }


    @Override
    public List<Agency> getAgencyStoredData() {
        return DatabaseHelper.getStoredAgency();
    }

    @Override
    public void exportPolicyDataIntoJsonFile(String username) {
        if (StringUtils.isNotEmpty(username)) {
            List<JSONObject> policyList = new ArrayList<>();
            try {
                policyList = DatabaseHelper.getAllPolicies();
            } catch (Exception e) {
                System.out.println("policy fetching error");
            }
            if (policyList != null && !policyList.isEmpty()) {
                String fileName = username + "nbdata.json";

                // Create a directory named "json_files" in the user's home directory
                Path directoryPath = Paths.get(System.getProperty("user.home"));
//                if (!Files.exists(directoryPath)) {
//                    try {
//                        Files.createDirectories(directoryPath);
//                    } catch (IOException e) {
//                        /** fall back solution */
//                        directoryPath = Paths.get("C:", "json_files");
//                        if (!Files.exists(directoryPath)) {
//                            try {
//                                Files.createDirectories(directoryPath);
//                            } catch (IOException exception) {
//                                exception.printStackTrace();
//                            }
//                        }
//                    }
//                }

                // Construct the file path inside the "json_files" directory
                Path filePath = directoryPath.resolve(fileName);

                try {
                    writeJsonToFile(policyList, filePath);
                    System.out.println("JSON file created successfully at: " + filePath.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeJsonToFile(List<JSONObject> listJsonData, Path filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
            fileWriter.write("["); // Start of the JSON array

            for (int i = 0; i < listJsonData.size(); i++) {
                fileWriter.write(listJsonData.get(i).toString());

                if (i < listJsonData.size() - 1) {
                    fileWriter.write(",");
                }
            }
            fileWriter.write("]"); // End of the JSON array
        }
    }
    private void savePolicyOrUpdateFupData(JSONArray policyList, boolean updateFUP) {
        DatabaseHelper.savePolicyOrUpdateFupData(policyList, updateFUP);
    }
    private void updatePolicyData(JSONArray policyList) {
        DatabaseHelper.updatePolicyData(policyList);
    }
    public void saveAgencyData(Agency agencyDetails) {
        DatabaseHelper.insertAgencyData(agencyDetails);
    }
}
