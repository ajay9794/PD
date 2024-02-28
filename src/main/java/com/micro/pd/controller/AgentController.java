package com.micro.pd.controller;

import com.micro.pd.database.DatabaseHelper;
import com.micro.pd.modelDTO.Agency;
import com.micro.pd.modelDTO.ResponseDto;
import com.micro.pd.service.AgentService;
import com.micro.pd.service.serviceImpl.AgentServiceImpl;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.BooleanUtils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class AgentController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private Label requestMessage;
    @FXML
    private Label popupLabelMessage;
    @FXML
    private MFXTextField  agency;
    @FXML
    private MFXTextField  agencyName;
    @FXML
    private MFXTextField  username;
    @FXML
    private MFXTextField password;

    @FXML
    private DatePicker strDOB;

    @FXML
    private DatePicker strFromDate;

    @FXML
    private DatePicker strToDate;

    @FXML
    private MFXTextField  otpText;

    @FXML
    private VBox middleVBox;

    @FXML
    private VBox leftSideVBox;

    @FXML
    private Pane middlePage;

    @FXML
    private Pane popupPage;

    @FXML
    private HBox root;

    @FXML
    private StackPane spinner;

    private DatabaseHelper databaseHelper;

    private AgentService agentService;

    private ResponseDto responseDto;

    private Agency selectedAgency;

    private int otpFailedCounter = 0;

    public AgentController() {}

    @FXML
    protected void onButtonClick() {
        if(this.checkUserCredentials()) {
            Agency agencyDetails = new Agency(this.agency.getText(), this.agencyName.getText(), this.username.getText(), this.password.getText(),
                    this.strDOB.getValue().toString(), this.strFromDate.getValue().toString(),this.strToDate.getValue().toString());

            ResponseDto response = this.agentService.logIn(agencyDetails);
            if(BooleanUtils.isTrue(response.getIsError())) {
                this.welcomeText.setText(response.getDetailsMessage());
                this.responseDto = null;
            } else {
                this.enableOrDisabledAllFeilds(true);
                this.selectedAgency = agencyDetails;
                this.responseDto = response;
                this.popupPage.setVisible(true);
                this.middlePage.setVisible(false);
                this.welcomeText.setText("Welcome to PD Application!");
            }
        }
    }

    @FXML
    void onClickClosePopup(ActionEvent event) {
        this.otpText.setText("");
        this.popupPage.setVisible(false);
        this.middlePage.setVisible(true);
    }
    @FXML
    void onOTPSubmitButtonClick(ActionEvent event) {
        if(this.otpText.getText().isEmpty()) {
            this.popupLabelMessage.setText("Empty text OR Invalid text !!!..");
        } else {
            CompletableFuture.supplyAsync(this::validateOTP)
                    .thenApplyAsync(this::downloadPolicyList)
                    .thenApplyAsync(this::updateMaskedPolicyNumber)
                    .thenAcceptAsync(result -> {
                        if(BooleanUtils.isFalse(result.getIsError())) {
                           /** Everything is success */

                        } else {

                        }
                        // Update UI with the final result
//                        Platform.runLater(() -> updateMessage("All operations completed: " + result));
                    })
                    .exceptionally(throwable -> {
                        // Handle exceptions
                        Platform.runLater(() -> this.welcomeText.setText("Error during operations: " + throwable.getMessage()));
                        return null;
                    });
//            ResponseDto response = this.agentService.validateOTP(this.otpText.getText(), this.responseDto);
//            if(BooleanUtils.isTrue(response.getIsError())) {
//                this.welcomeText.setText(response.getDetailsMessage());
//                this.responseDto = null;
//            } else {
//                this.requestMessage.setText("Fetching policy detail's !!!...");
//                this.welcomeText.setText("OTP Validation Successful!");
//                System.out.println("validation is done");
//                ResponseDto policyDownloadResponse = this.agentService.downloadPolicyList(this.selectedAgency, response.getResponseJson());
//
//                if(BooleanUtils.isTrue(policyDownloadResponse.getIsError())) {
//
//                } else {
//
//                }
//            }

        }
    }

    public ResponseDto validateOTP() {
        this.welcomeText.setText("OTP Enter Successfully...");
        this.requestMessage.setText("OTP Validation in-progress...");
        this.middlePage.setVisible(true);
        this.popupPage.setVisible(false);
        this.spinner.setVisible(true);
        sleep(1000);
        ResponseDto response = this.agentService.validateOTP(this.otpText.getText(), this.responseDto);
        if(BooleanUtils.isTrue(response.getIsError())) {
            this.welcomeText.setText(response.getDetailsMessage());
            this.requestMessage.setText("");
            this.spinner.setVisible(false);
            this.popupPage.setVisible(true);
            this.popupLabelMessage.setText(response.getDetailsMessage());


//            if(this.otpFailedCounter > 3) {
//                /** user has 3 attempts to submit otp after that data cleat  */
//                this.otpText.setText("");
//                this.popupLabelMessage.setText(response.getDetailsMessage());
//                this.enableOrDisabledAllFeilds(false);
//                this.popupPage.setVisible(true);
//            } else {
//
//            }
            return  response;
        } else {
            return response;
        }
    }

    public ResponseDto downloadPolicyList(ResponseDto asyncResponse) {
        if(BooleanUtils.isFalse(asyncResponse.getIsError())) {
            this.requestMessage.setText("Fetching policy detail's...");
            this.welcomeText.setText("OTP Validation Success...");
            this.popupLabelMessage.setText("");
            this.middlePage.setVisible(true);
            this.spinner.setVisible(true);
            this.popupPage.setVisible(false);
            System.out.println("validation is done");
            sleep(1000);
            ResponseDto policyDownloadResponse = this.agentService.downloadPolicyList(this.selectedAgency, asyncResponse);
            if(BooleanUtils.isTrue(policyDownloadResponse.getIsError())) {
                this.requestMessage.setText("");
                this.spinner.setVisible(false);
                this.popupLabelMessage.setText("");
                this.popupPage.setVisible(false);
                this.welcomeText.setText(policyDownloadResponse.getDetailsMessage());
                this.middlePage.setVisible(true);
            }
            return policyDownloadResponse;
        } else {
            return asyncResponse;
        }
    }

    public ResponseDto updateMaskedPolicyNumber(ResponseDto asyncResponse) {
        if(BooleanUtils.isFalse(asyncResponse.getIsError())) {
            this.requestMessage.setText("updating masked policy number...");
            this.welcomeText.setText("policy detail's downloaded successfully...");
            sleep(1000);
            ResponseDto response = this.agentService.updateMaskedPolicyNumber(asyncResponse);
            if(BooleanUtils.isTrue(response.getIsError())) {
                this.requestMessage.setText("");
                this.spinner.setVisible(false);
                this.popupLabelMessage.setText("");
                this.popupPage.setVisible(false);
                this.welcomeText.setText(response.getDetailsMessage());
                this.middlePage.setVisible(true);
            }
            return response;
        } else {
            return asyncResponse;
        }
    }
    private boolean checkUserCredentials() {
        if (this.agency.getText().isEmpty() || this.agencyName.getText().isEmpty() ||
                this.username.getText().isEmpty() || this.password.getText().isEmpty() ||
                this.strDOB.getValue() == null || this.strToDate.getValue() == null ||
                this.strFromDate.getValue() == null) {
            return false;
        } else {
            return true;
        }

    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enableOrDisabledAllFeilds(boolean value) {
        this.agency.setDisable(value);
        this.agencyName.setDisable(value);
        this.username.setDisable(value);
        this.password.setDisable(value);
        this.strDOB.setDisable(value);
        this.strFromDate.setDisable(value);
        this.strToDate.setDisable(value);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /** Initialize service  */
        AgentService agentService = new AgentServiceImpl();
        this.agentService = agentService;

        /** end Here */
        this.middleVBox.setVisible(true);
        this.leftSideVBox.setVisible(true);
        this.middlePage.setVisible(true);
        this.popupPage.setVisible(false);
        this.spinner.setVisible(false);

    }
}
