package com.micro.pd;

import com.micro.pd.controller.AgentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PdApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
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