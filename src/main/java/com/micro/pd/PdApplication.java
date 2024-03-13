package com.micro.pd;

import com.micro.pd.controller.AgentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class PdApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            File databaseFile = new File(System.getProperty("user.home"), "pd.db");

            if (!databaseFile.exists()) {
                try {
                    Files.copy(Objects.requireNonNull(PdApplication.class.getResource("/pd.db")).openStream(),
                            databaseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            FXMLLoader fxmlLoader = new FXMLLoader(PdApplication.class.getResource("pd-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 600);
            primaryStage.setTitle("PD VIEW!");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            Throwable cause = e.getCause();
            if (cause != null) {
                cause.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}