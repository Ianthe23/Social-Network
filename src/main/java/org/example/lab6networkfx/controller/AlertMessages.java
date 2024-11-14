package org.example.lab6networkfx.controller;

import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class AlertMessages {
    public static void showMessage(Stage stage, Alert.AlertType type, String header, String message) {
        Alert alert = new Alert(type);
        alert.initOwner(stage);
        alert.setTitle(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
