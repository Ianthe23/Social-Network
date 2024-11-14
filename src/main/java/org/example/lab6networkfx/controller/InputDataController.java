package org.example.lab6networkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.service.NetworkService;

import java.awt.*;
import java.awt.event.ActionEvent;

public class InputDataController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnExit;

    NetworkService service;
    Stage inputStage;
    User user;

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        usernameField.setText("");
    }

    public void setService(NetworkService service, Stage stage, User user) {
        this.service = service;
        this.inputStage = stage;
        this.user = user;

        if (user != null) {
            setFields(user);
        }
    }

    private void setFields(User user) {
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        usernameField.setText(user.getUsername());
    }

    @FXML
    private void handleAdd() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();

        User user = new User(firstName, lastName, username);
        saveUser(user);

        clearFields();
    }

    private void handleDelete() {
        String username = usernameField.getText();

        try {
            service.removeUser(username);
            inputStage.close();
        } catch (Exception e) {
            AlertMessages.showMessage(null, Alert.AlertType.CONFIRMATION, "Delete User", "User deleted successfully!");
        }

        inputStage.close();
    }

    @FXML
    public void handleExit() {
        inputStage.close();
    }

    private void saveUser(User user) {
        try {
            service.addUser(user.getFirstName(), user.getLastName(), user.getUsername());
            inputStage.close();
        } catch (Exception e) {
            AlertMessages.showMessage(null, Alert.AlertType.CONFIRMATION, "Add User", "User added successfully!");
        }

        inputStage.close();
    }

    public void handleClicks(ActionEvent event) {
        if (event.getSource() == btnAdd) {
            handleAdd();
        } else if (event.getSource() == btnDelete) {
            handleDelete();
        } else if (event.getSource() == btnExit) {
            handleExit();
        }
    }



}
