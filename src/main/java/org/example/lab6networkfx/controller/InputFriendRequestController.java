package org.example.lab6networkfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.service.NetworkService;

import java.util.ArrayList;
import java.util.List;

public class InputFriendRequestController {
    @FXML
    private ComboBox<String> user1Field;

    @FXML
    private ComboBox<String> user2Field;

    @FXML
    private Button btnSendRequest;

    @FXML
    private Button btnExit;

    NetworkService service;
    Stage inputStage;
    User user1;
    User user2;

    private void clearFields() {
        user1Field.setValue(null);
        user2Field.setValue(null);
    }

    public void setService(NetworkService service, Stage stage, User user1, User user2) {
        this.service = service;
        this.inputStage = stage;
        this.user1 = user1;
        this.user2 = user2;

        setFields(user1, user2);
    }

    private void setFields(User user1, User user2) {
        //add the users to the combo boxes
        List<String> users = new ArrayList<>();
        service.getAllUsers().forEach(user3 -> users.add(((User) user3).getUsername()));

        if (user1 == null) {
            user1Field.setValue(null);
        } else {
            user1Field.setValue(user1.getUsername());
        }

        if (user2 == null) {
            user2Field.setValue(null);
        } else {
            user2Field.setValue(user2.getUsername());
        }
        user1Field.getItems().addAll(users);
        user2Field.getItems().addAll(users);
    }

    private void handleRequest() {
        try {
            String user1 = user1Field.getValue().toString();
            String user2 = user2Field.getValue().toString();
            service.pendingFriendshipRequest(user1, user2);
            AlertMessages.showMessage(inputStage, Alert.AlertType.CONFIRMATION, "Friend Request sent", "Friend Request sent successfully");
            clearFields();
            inputStage.close();
        } catch (Exception e) {
            AlertMessages.showMessage(inputStage, Alert.AlertType.ERROR, "Add Friendship", e.getMessage());
        }
        inputStage.close();
    }

    public void handleExit() {
        inputStage.close();
    }

    @FXML
    public void handleClicks(ActionEvent event) {
        if (event.getSource() == btnSendRequest) {
            handleRequest();
        } else if (event.getSource() == btnExit) {
            handleExit();
        }
    }
}
