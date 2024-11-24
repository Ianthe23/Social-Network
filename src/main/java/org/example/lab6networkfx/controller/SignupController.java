package org.example.lab6networkfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.domain.messages.Message;
import org.example.lab6networkfx.service.MessageService;
import org.example.lab6networkfx.service.NetworkService;

import java.io.IOException;

public class SignupController {
    NetworkService service;
    MessageService messageService;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Button btnSignup;

    @FXML
    private Button btnCancel;

    Stage inputStage;

    public void setSignup(Stage stage, NetworkService service, MessageService messageService) {
        this.service = service;
        this.messageService = messageService;
        this.inputStage = stage;
    }

    private void clearFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }

    @FXML
    void handleSignUp(ActionEvent event) throws IOException {
        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String confirmPassword = txtConfirmPassword.getText().toString();

        if (!password.equals(confirmPassword)) {
            AlertMessages.showMessage(inputStage, Alert.AlertType.ERROR,"Password Error","Passwords don't match!");
        }

        try {
            service.addUser(firstName, lastName, username, password);
            clearFields();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/lab6networkfx/views/login-view.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/main-view.css").toExternalForm());
            stage.setTitle("Log In");
            stage.setScene(scene);
            LoginController controller = loader.getController();
            controller.setLoginController(service, messageService, stage);
            AlertMessages.showMessage(inputStage, Alert.AlertType.INFORMATION,"Success","User added successfully!");
        } catch (Exception e) {
            AlertMessages.showMessage(inputStage, Alert.AlertType.ERROR,"Error",e.getMessage());
        }

    }

    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/org/example/lab6networkfx/views/login-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        AnchorPane root = stageLoader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/main-view.css").toExternalForm());
        stage.setTitle("Log In");
        stage.setScene(scene);
        LoginController controller = stageLoader.getController();
        controller.setLoginController(service, messageService, stage);
    }


}
