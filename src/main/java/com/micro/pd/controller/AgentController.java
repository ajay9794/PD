package com.micro.pd.controller;

import com.micro.pd.database.DatabaseHelper;
import com.micro.pd.modelDTO.Agency;
import com.micro.pd.modelDTO.ResponseDto;
import com.micro.pd.service.AgentService;
import com.micro.pd.service.serviceImpl.AgentServiceImpl;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
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
    private MFXPasswordField password;
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
    private MFXButton updateAddressAndNomineeButton;
    @FXML
    private MFXButton updateFUPButton;
    @FXML
    private MFXButton updatePolicyButton;
    @FXML
    private MFXButton exportDataButton;



    private AgentService agentService;

    private ResponseDto responseDto;

    private Agency selectedAgency;

    private List<Agency> storedAgencyList;

    private String navigateToButtonFunctionality = "";

    public AgentController() {}


    @FXML
    void onClickButtonExportData(ActionEvent actionEvent) {
        if(this.username.getText().isEmpty()) {
            this.showAlert(Alert.AlertType.WARNING, "", null, "Fill in the agency information...");
        } else {
            this.requestMessage.setText("Currently, we are in the process of exporting policy details");
            this.spinnerActive(true);
            new Thread(() -> {
                agentService.exportPolicyDataIntoJsonFile(this.username.getText());

                // Once the export operation is complete, update UI on the JavaFX Application Thread
                Platform.runLater(() -> {

                    // Introduce a delay of 2 seconds (adjust as needed)
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> {
                        this.requestMessage.setText("");
                        this.spinnerActive(false);
                    });
                    pause.play();
                });
            }).start();
        }
    }

    @FXML
    protected void onButtonClick() {
        if(this.checkUserCredentials()) {
            Agency agencyDetails = new Agency(this.agency.getText(), this.agencyName.getText(), this.username.getText(), this.password.getText(),
                    this.strDOB.getValue().toString(), this.strFromDate.getValue().toString(),this.strToDate.getValue().toString());

            ResponseDto response = this.agentService.logIn(agencyDetails);
            if(BooleanUtils.isTrue(response.getIsError())) {
                this.showAlert(Alert.AlertType.ERROR, "", response.getErrorMessage(), response.getDetailsMessage());
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
    void onOTPSubmitButtonClick() {
        if(this.otpText.getText().isEmpty()) {
            this.popupLabelMessage.setText("Empty text OR Invalid text !!!..");
        } else {
            Platform.runLater(()-> {
                this.requestMessage.setText("OTP Validation in-progress...");
                this.middlePage.setVisible(true);
                this.popupPage.setVisible(false);
                this.spinnerActive(true);
            });
            if(this.navigateToButtonFunctionality.equalsIgnoreCase("onClickButtonAddressAndNomineeUpdate")) {
                this.navigateToButtonFunctionality = "";
                this.enableOrDisabledAllFeilds(false);
                ResponseDto response = this.agentService.validateOTP(this.otpText.getText(), this.responseDto);
                if(BooleanUtils.isFalse(response.getIsError())) {
                    this.onClickButtonAddressAndNomineeUpdate();
                } else {
                    this.requestMessage.setText("");
                    this.spinnerActive(false);
                    this.showAlert(Alert.AlertType.ERROR, "", null, response.getDetailsMessage());
                }
            } else if(this.navigateToButtonFunctionality.equalsIgnoreCase("onClickButtonUpdateFUP")) {
                this.navigateToButtonFunctionality = "";
                this.enableOrDisabledAllFeilds(false);
                ResponseDto response = this.agentService.validateOTP(this.otpText.getText(), this.responseDto);
                if(BooleanUtils.isFalse(response.getIsError())) {
                    this.onClickButtonUpdateFUP();
                } else {
                    this.requestMessage.setText("");
                    this.spinnerActive(false);
                    this.showAlert(Alert.AlertType.ERROR, "", null, response.getDetailsMessage());
                }
            } else if(this.navigateToButtonFunctionality.equalsIgnoreCase("onClickButtonUpdatePolicy")) {
                this.navigateToButtonFunctionality = "";
                this.enableOrDisabledAllFeilds(false);
                ResponseDto response = this.agentService.validateOTP(this.otpText.getText(), this.responseDto);
                if(BooleanUtils.isFalse(response.getIsError())) {
                    this.onClickButtonUpdatePolicy();
                } else {
                    this.requestMessage.setText("");
                    this.spinnerActive(false);
                    this.showAlert(Alert.AlertType.ERROR, "", null, response.getDetailsMessage());
                }
            } else {
                CompletableFuture.supplyAsync(this::validateOTP)
                        .thenApplyAsync(this::downloadPolicyList)
                        .thenApplyAsync(this::updateMaskedPolicyNumber)
                        .thenAcceptAsync(result -> {
                            Platform.runLater(() -> {
                                Platform.runLater(() -> {
                                    this.requestMessage.setText("");
                                    this.spinnerActive(false);
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
                            Platform.runLater(() -> {
                                this.middlePage.setVisible(true);
                                this.popupPage.setVisible(false);
                                this.spinnerActive(false);
                                this.enableOrDisabledAllFeilds(false);
                                this.showAlert(Alert.AlertType.ERROR, "Error", null, throwable.getMessage());
                            });
                            return null;
                        });
            }

        }
    }

    @FXML
    void onClickButtonAddressAndNomineeUpdate() {
        if(this.checkUserCredentials()) {
            if(this.responseDto != null && this.responseDto.getValidateOtpResponseJson() != null && StringUtils.isNotEmpty(this.responseDto.getValidateOtpResponseJson().getString("accesstoken"))) {
                Agency agencyDetails = new Agency(this.agency.getText(), this.agencyName.getText(), this.username.getText(), this.password.getText(),
                        this.strDOB.getValue().toString(), this.strFromDate.getValue().toString(),this.strToDate.getValue().toString());
                String accessTokan = this.responseDto.getValidateOtpResponseJson().getString("accesstoken");

                List<JSONObject> policyList = DatabaseHelper.getPolicyForAddressAndNomineeUpdate(agencyDetails);
                if (policyList != null && !policyList.isEmpty()) {
                    Platform.runLater(() -> {
                        this.spinnerActive(true);
                        this.requestMessage.setText("Updating policy details...");
                    });
                    String finalAccessTokan = accessTokan;
                    Task<ResponseDto> task = new Task<>() {
                        @Override
                        protected ResponseDto call() {
                            ResponseDto responseObject = responseDto;
                            try {
                                Connection connection = DatabaseHelper.getConnection();
                                int counter = 1;
                                for (JSONObject policyObject : policyList) {
                                    if (counter >= 2) {
                                        counter = 1;
                                        Thread.sleep(20000);
                                    } else {
                                        counter++;
                                    }
                                    agentService.addressAndNomineeUpdate(agencyDetails, finalAccessTokan, policyObject,  connection);
                                    Platform.runLater(() -> {
                                        requestMessage.setText(policyObject.getString("PolicyNo") + "policy updated...");
                                    });
                                }
                                responseObject.setIsError(false);
                                responseObject.setErrorMessage(null);
                                responseObject.setDetailsMessage("SuccessFully update all policy address details...");
                                agentService.exportPolicyDataIntoJsonFile(agencyDetails.getUsername());
                                return responseObject;
                            } catch (Exception e) {
                                responseObject.setIsError(true);
                                responseObject.setErrorMessage("Getting error while updating address details...");
                                responseObject.setDetailsMessage(e.getMessage());
                                return responseObject;
                            }
                        }
                    };

                    // Set callbacks for success and failure
                    task.setOnSucceeded(event -> {
                        ResponseDto result = task.getValue(); // Retrieve the response
                        // Handle the result as needed
                        // Note: This block is executed on the JavaFX Application Thread
                        if (result != null && !result.getIsError()) {
                            // Update UI for successful response
                            this.showAlert(Alert.AlertType.INFORMATION, "Address and Nominee Update", result.getErrorMessage(), result.getDetailsMessage());
                        } else {
                            // Handle error case
                            this.showAlert(Alert.AlertType.ERROR, "Address and Nominee Update", result.getErrorMessage(), result.getDetailsMessage());
                        }
                        Platform.runLater(() -> {
                            this.spinnerActive(false);
                        });
                    });

                    task.setOnFailed(event -> {
                        // Handle failure or exception
                        ResponseDto result = task.getValue(); // Retrieve the response (may be null)
                        // Handle the result or exception as needed
                        // Note: This block is executed on the JavaFX Application Thread
                        if (result != null && !result.getIsError()) {
                            // Update UI for successful response
                            this.showAlert(Alert.AlertType.INFORMATION, "Address and Nominee Update", result.getErrorMessage(), result.getDetailsMessage());
                        } else {
                            // Handle error case
                            this.showAlert(Alert.AlertType.ERROR, "Address and Nominee Update", result.getErrorMessage(), result.getDetailsMessage());
                        }
                        Platform.runLater(() -> {
                            this.spinnerActive(false);
                        });
                    });

                    // Start the task in a new thread
                    new Thread(task).start();
                } else {
                    this.showAlert(Alert.AlertType.INFORMATION,  "", null, "policy detail's are missing...");
                }
            } else {
                this.navigateToButtonFunctionality = "onClickButtonAddressAndNomineeUpdate";
                onButtonClick();
            }

        }
    }

    @FXML
    void onClickButtonUpdatePolicy() {
        if(checkUserCredentials()) {
            if(this.responseDto != null && this.responseDto.getValidateOtpResponseJson() != null && StringUtils.isNotEmpty(this.responseDto.getValidateOtpResponseJson().getString("accesstoken"))) {
                Agency agencyDetails = new Agency(this.agency.getText(), this.agencyName.getText(), this.username.getText(), this.password.getText(),
                        this.strDOB.getValue().toString(), this.strFromDate.getValue().toString(),this.strToDate.getValue().toString());
                Platform.runLater(() -> {
                    this.spinnerActive(true);
                    this.requestMessage.setText("Update policy details...");
                });
                ResponseDto response = this.agentService.updatePolicy(agencyDetails, this.responseDto);
                Platform.runLater(() ->{
                    if(BooleanUtils.isTrue(response.getIsError())) {
                        this.showAlert(Alert.AlertType.ERROR,  "update *** policy", response.getErrorMessage(), response.getDetailsMessage());
                    } else {
                        /** Success response */
                        this.showAlert(Alert.AlertType.INFORMATION,  "update *** policy", response.getErrorMessage(), response.getDetailsMessage());
                    }
                    this.spinnerActive(false);
                    this.requestMessage.setText("");
                });
            } else {
                this.navigateToButtonFunctionality = "onClickButtonUpdatePolicy";
                onButtonClick();
            }
        }
    }

    @FXML
    void onClickButtonUpdateFUP() {
        if(checkUserCredentials()) {
            if(this.responseDto != null && this.responseDto.getValidateOtpResponseJson() != null && StringUtils.isNotEmpty(this.responseDto.getValidateOtpResponseJson().getString("accesstoken"))) {
                Agency agencyDetails = new Agency(this.agency.getText(), this.agencyName.getText(), this.username.getText(), this.password.getText(),
                        this.strDOB.getValue().toString(), this.strFromDate.getValue().toString(),this.strToDate.getValue().toString());

                Platform.runLater(() -> {
                    this.spinnerActive(true);
                    this.requestMessage.setText("Update policy details...");
                });
                ResponseDto response = this.agentService.downloadPolicyListOrUpdateFUP(agencyDetails, this.responseDto, true);
                if(BooleanUtils.isTrue(response.getIsError())) {
                    this.showAlert(Alert.AlertType.ERROR,  "Update FUP", response.getErrorMessage(), response.getDetailsMessage());
                } else {
                    /** Success response */
                    this.showAlert(Alert.AlertType.INFORMATION,  "Update FUP", response.getErrorMessage(), response.getDetailsMessage());
                }
                Platform.runLater(() -> {
                    this.spinnerActive(false);
                    this.requestMessage.setText("");
                });
            } else {
                this.navigateToButtonFunctionality = "onClickButtonUpdateFUP";
                onButtonClick();
            }
        }
    }

    public ResponseDto validateOTP() {
        sleep(1000);
        ResponseDto response = this.agentService.validateOTP(this.otpText.getText(), this.responseDto);
        if(BooleanUtils.isTrue(response.getIsError())) {
            Platform.runLater(() -> {
                this.welcomeText.setText(response.getDetailsMessage());
                this.requestMessage.setText("");
                this.spinnerActive(false);
                this.popupPage.setVisible(true);
                this.popupLabelMessage.setText(response.getDetailsMessage());
            });
            return  response;
        } else {
            Platform.runLater(() -> {
                this.responseDto = response;
                this.requestMessage.setText("Fetching policy detail's...");
                this.welcomeText.setText("OTP Validation Success...");
                this.popupLabelMessage.setText("");
                this.middlePage.setVisible(true);
                this.spinnerActive(true);
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
                    this.spinnerActive(false);
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
            this.showAlert(Alert.AlertType.WARNING, "Incomplete Form", null, "Please fill in all required fields.");
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

    public void spinnerActive(boolean value) {
        this.spinner.setVisible(value);
        this.middlePage.setDisable(value);
        this.popupPage.setDisable(value);
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
        this.spinnerActive(false);

        /**  set fromDate and toDate as default while opening application*/
        this.strFromDate.setValue(LocalDate.of(1956, 1, 1));
        this.strToDate.setValue(LocalDate.now());


        StringConverter<LocalDate> converter = new StringConverter<>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
            if(agencyList != null && !agencyList.isEmpty()) {
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
            if(agencyListObject != null && !agencyListObject.isEmpty() && agencyObject != null) {
                this.agencyName.setText(agencyObject.getAgencyName());
                this.username.setText(agencyObject.getUsername());
                this.password.setText(agencyObject.getPassword());
                this.strDOB.setValue(LocalDate.parse(agencyObject.getDateOfBirth()));
                /** System.out.println("select agency "+ agencyObject.toString()); */
                this.responseDto = null;
            }
        });
    }
}
