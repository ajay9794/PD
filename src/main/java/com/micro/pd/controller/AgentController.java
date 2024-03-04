package com.micro.pd.controller;

import com.micro.pd.database.DatabaseHelper;
import com.micro.pd.modelDTO.Agency;
import com.micro.pd.modelDTO.ResponseDto;
import com.micro.pd.service.AgentService;
import com.micro.pd.service.serviceImpl.AgentServiceImpl;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AgentController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private Label requestMessage;
    @FXML
    private Label popupLabelMessage;

    @FXML
    private MFXComboBox<String> agency;
    @FXML
    private MFXTextField  agencyName;
    @FXML
    private MFXTextField  username;
    @FXML
    private MFXTextField password;
    @FXML
    private MFXTextField  otpText;


    @FXML
    private DatePicker strDOB;
    @FXML
    private DatePicker strFromDate;
    @FXML
    private DatePicker strToDate;



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


    @FXML
    private MFXButton nbDownloadButton;
    @FXML
    private MFXButton otpSubmitButton;
    @FXML
    private MFXButton registerMPINButton;
    @FXML
    private MFXButton updateAddressAndNomineeButton;
    @FXML
    private MFXButton updateFUPButton;
    @FXML
    private MFXButton updatePolicyButton;



    private AgentService agentService;

    private ResponseDto responseDto;

    private Agency selectedAgency;

    private List<Agency> storedAgencyList;

    public AgentController() {}

    @FXML
    void onClickRegisterMPINButton(ActionEvent event) {
        this.showAlert(Alert.AlertType.ERROR, "Error", "header", "This is the complete message");
    }

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
        this.enableOrDisabledAllFeilds(false);
    }
    @FXML
    void onOTPSubmitButtonClick(ActionEvent event) {
        if(this.otpText.getText().isEmpty()) {
            this.popupLabelMessage.setText("Empty text OR Invalid text !!!..");
        } else {
            this.welcomeText.setText("OTP Enter Successfully...");
            this.requestMessage.setText("OTP Validation in-progress...");
            this.middlePage.setVisible(true);
            this.popupPage.setVisible(false);
            this.spinner.setVisible(true);
            CompletableFuture.supplyAsync(this::validateOTP)
                    .thenApplyAsync(this::downloadPolicyList)
                    .thenApplyAsync(this::updateMaskedPolicyNumber)
                    .thenAcceptAsync(result -> {
                        Platform.runLater(() -> {
                            Platform.runLater(() -> {
                                this.requestMessage.setText("");
                                this.spinner.setVisible(false);
                                this.popupLabelMessage.setText("");
                                this.popupPage.setVisible(false);
                                this.welcomeText.setText("");
                                this.middlePage.setVisible(true);
                                this.enableOrDisabledAllFeilds(false);
                                if(BooleanUtils.isTrue(result.getIsError())) {
                                    this.showAlert(Alert.AlertType.ERROR, "Error", result.getErrorMessage(), result.getDetailsMessage());
                                } else {
                                    this.responseDto = result; /** at every async operation success need to mention this response */
                                    this.showAlert(Alert.AlertType.INFORMATION, "Success", result.getErrorMessage(), result.getDetailsMessage());
                                }
                            });
                        });
                    })
                    .exceptionally(throwable -> {
                        /** Handle exceptions */
                        Platform.runLater(() ->{
                                    this.enableOrDisabledAllFeilds(false);
                                    this.showAlert(Alert.AlertType.ERROR, "Error", null, throwable.getMessage());
                        });
                        return null;
                    });
        }
    }

    @FXML
    void onClickButtonAddressAndNomineeUpdate(ActionEvent event) {
        if(this.checkUserCredentials()) {
            Agency agencyDetails = new Agency(this.agency.getText(), this.agencyName.getText(), this.username.getText(), this.password.getText(),
                    this.strDOB.getValue().toString(), this.strFromDate.getValue().toString(),this.strToDate.getValue().toString());

            ResponseDto response = this.agentService.addressAndNomineeUpdate(agencyDetails, this.responseDto);
            if(BooleanUtils.isTrue(response.getIsError())) {
                this.showAlert(Alert.AlertType.ERROR,  "Address And Nominee update", response.getErrorMessage(), response.getDetailsMessage());
            } else {
                /** Success response */
                this.showAlert(Alert.AlertType.INFORMATION,  "Address And Nominee update", response.getErrorMessage(), response.getDetailsMessage());
            }
        }
    }

    @FXML
    void onClickButtonUpdatePolicy(ActionEvent event) {
        Agency agencyDetails = new Agency(this.agency.getText(), this.agencyName.getText(), this.username.getText(), this.password.getText(),
                this.strDOB.getValue().toString(), this.strFromDate.getValue().toString(),this.strToDate.getValue().toString());

        ResponseDto response = this.agentService.updatePolicy(agencyDetails, this.responseDto);
        if(BooleanUtils.isTrue(response.getIsError())) {
            this.showAlert(Alert.AlertType.ERROR,  "update *** policy", response.getErrorMessage(), response.getDetailsMessage());
        } else {
            /** Success response */
            this.showAlert(Alert.AlertType.INFORMATION,  "update *** policy", response.getErrorMessage(), response.getDetailsMessage());
        }
    }
    @FXML
    void onClickButtonUpdateFUP(ActionEvent event) {
        Agency agencyDetails = new Agency(this.agency.getText(), this.agencyName.getText(), this.username.getText(), this.password.getText(),
                this.strDOB.getValue().toString(), this.strFromDate.getValue().toString(),this.strToDate.getValue().toString());

        ResponseDto response = this.agentService.downloadPolicyListOrUpdateFUP(agencyDetails, this.responseDto, true);
    }

    public ResponseDto validateOTP() {
        sleep(1000);
        ResponseDto response = this.agentService.validateOTP(this.otpText.getText(), this.responseDto);
        if(BooleanUtils.isTrue(response.getIsError())) {
            Platform.runLater(() -> {
                this.welcomeText.setText(response.getDetailsMessage());
                this.requestMessage.setText("");
                this.spinner.setVisible(false);
                this.popupPage.setVisible(true);
                this.popupLabelMessage.setText(response.getDetailsMessage());
            });
            return  response;
        } else {
            Platform.runLater(() -> {
                this.responseDto = response;
                this.updateFUPButton.setDisable(false);
                this.updateAddressAndNomineeButton.setDisable(false);
                this.updatePolicyButton.setDisable(false);
                this.requestMessage.setText("Fetching policy detail's...");
                this.welcomeText.setText("OTP Validation Success...");
                this.popupLabelMessage.setText("");
                this.middlePage.setVisible(true);
                this.spinner.setVisible(true);
                this.popupPage.setVisible(false);
                System.out.println("validation is done");
            });
            return response;
        }
    }

    public ResponseDto downloadPolicyList(ResponseDto asyncResponse) {
        if(BooleanUtils.isFalse(asyncResponse.getIsError())) {
            sleep(1000);
            ResponseDto policyDownloadResponse = this.agentService.downloadPolicyListOrUpdateFUP(this.selectedAgency, asyncResponse, false);
            if(BooleanUtils.isTrue(policyDownloadResponse.getIsError())) {
                Platform.runLater(() -> {
                    this.requestMessage.setText("");
                    this.spinner.setVisible(false);
                    this.popupLabelMessage.setText("");
                    this.popupPage.setVisible(false);
                    this.welcomeText.setText(policyDownloadResponse.getDetailsMessage());
                    this.middlePage.setVisible(true);
                });
            } else {
                this.responseDto = policyDownloadResponse;
            }
            return policyDownloadResponse;
        } else {
            return asyncResponse;
        }
    }

    public ResponseDto updateMaskedPolicyNumber(ResponseDto asyncResponse) {
        if(BooleanUtils.isFalse(asyncResponse.getIsError())) {
            Platform.runLater(() -> {
                this.requestMessage.setText("updating masked policy number...");
                this.welcomeText.setText("policy detail's downloaded successfully...");
            });
            sleep(1000);
            ResponseDto response = this.agentService.updateMaskedPolicyNumber(this.selectedAgency , asyncResponse);
            if(BooleanUtils.isFalse(response.getIsError())) {
             this.responseDto = response;
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

    private void showAlert(Alert.AlertType alertType, String alertTitle, String alertHeader, String alertMessage) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertMessage);
        alert.showAndWait();
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

        /** initially disabled button's*/
        this.updateFUPButton.setDisable(true);
        this.updateAddressAndNomineeButton.setDisable(true);
        this.updatePolicyButton.setDisable(true);

        StringConverter<LocalDate> converter = new StringConverter<>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        this.strDOB.setConverter(converter);
        this.strFromDate.setConverter(converter);
        this.strToDate.setConverter(converter);

        Platform.runLater(()-> {
            List<Agency> agencyList = this.agentService.getAgencyStoredData();
            if(CollectionUtils.isNotEmpty(agencyList)) {
                this.storedAgencyList = agencyList;

                List<String> namesList = agencyList.stream()
                        .map(Agency::getAgency) /** Extract only the agency */
                        .collect(Collectors.toList());
                        agency.getItems().addAll(namesList);
            }
        });


        // Customize how the selected value is displayed in the ComboBox
        agency.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });

        agency.setOnAction(event -> {
            List<Agency> agencyListObject = this.storedAgencyList.stream().filter(agencyObj -> StringUtils.isNotEmpty(agencyObj.getAgency()) && agencyObj.getAgency().equalsIgnoreCase(agency.getValue())).collect(Collectors.toList());
            Agency agencyObject = agencyListObject.get(0);
            if(CollectionUtils.isNotEmpty(agencyListObject) && agencyObject != null) {
                this.agencyName.setText(agencyObject.getAgencyName());
                this.username.setText(agencyObject.getUsername());
                this.password.setText(agencyObject.getPassword());
                this.strDOB.setValue(LocalDate.parse(agencyObject.getDateOfBirth()));
            }
        });
    }
}
