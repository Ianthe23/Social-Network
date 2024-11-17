package org.example.lab6networkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.lab6networkfx.domain.Friendship;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.service.NetworkService;

import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

public class InputFriendshipController {
    @FXML
    private TextField user1Field;

    @FXML
    private ComboBox<String> user2Field;

    @FXML
    private Button btnAccept;

    @FXML
    private Button btnReject;

    @FXML
    private Button btnExit;

    NetworkService service;
    Stage inputStage;
    User user;

    private void clearFields() {
        user1Field.setText("");
        user2Field.setValue(null);
    }

    public void setService(NetworkService service, Stage stage, User user) {
        this.service = service;
        this.inputStage = stage;
        this.user = user;

        setFields(user);
    }

    private void setFields(User user) {
        //add the users to the combo boxes
        List<String> users = new ArrayList<>();
        service.getAllUsers().forEach(currentUser -> {
            if (((User) currentUser).getUsername().equals(user.getUsername())) {
                ((User) currentUser).getPendingFriendships().forEach(friend ->
                        users.add(friend.getUsername())
                );
            }
        });

        user1Field.setText(user.getUsername());
        user2Field.getItems().addAll(users);
    }

    private void handleAdd() {
        try {
            String user1 = user1Field.getText().toString();
            String user2 = user2Field.getValue().toString();
            System.out.println(user1 + " " + user2);
            service.acceptFriendshipRequest(user1, user2);
            System.out.println("Friendship added");
            AlertMessages.showMessage(null, Alert.AlertType.CONFIRMATION, "Friendship added", "Friendship added successfully");
            clearFields();
            inputStage.close();
        } catch (Exception e) {
            AlertMessages.showMessage(null, Alert.AlertType.ERROR, "Add Friendship", e.getMessage());
        }
        inputStage.close();
    }

    public void handleReject() {
        try {
            String user1 = user1Field.getText().toString();
            String user2 = user2Field.getValue().toString();
            service.rejectFriendshipRequest(user1, user2);
            AlertMessages.showMessage(null, Alert.AlertType.CONFIRMATION, "Friendship rejected", "Friendship rejected successfully");
            clearFields();
            inputStage.close();
        } catch (Exception e) {
            AlertMessages.showMessage(null, Alert.AlertType.ERROR, "Reject Friendship", e.getMessage());
        }
        inputStage.close();
    }

    public void handleExit() {
        inputStage.close();
    }

    @FXML
    public void handleClicks(ActionEvent event) {
        if (event.getSource() == btnAccept) {
            handleAdd();
        } else if (event.getSource() == btnReject){
            handleReject();
        }
        else if (event.getSource() == btnExit) {
            handleExit();
        }
    }


}
