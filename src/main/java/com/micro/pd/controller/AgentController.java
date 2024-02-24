package com.micro.pd.controller;

import com.micro.pd.database.DatabaseHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AgentController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField agency;
    @FXML
    private TextField agency_name;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    private DatabaseHelper databaseHelper;

    @FXML
    protected void onButtonClick() throws SQLException {
        if(this.checkUserCredentials()) {
            this.databaseHelper.getConnection(); // just to check (test success)
            welcomeText.setText("Welcome to JavaFX Application!");
        }
    }

    private boolean checkUserCredentials() {
        if (this.agency.getText().isEmpty() || this.agency_name.getText().isEmpty() ||
        this.username.getText().isEmpty() || this.password.getText().isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
