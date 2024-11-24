package org.example.lab6networkfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.example.lab6networkfx.domain.Friendship;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.service.NetworkService;

import java.io.IOException;

public class LoginController {
    NetworkService service;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;

    @FXML
    private Button btnCancel;

    @FXML
    public ObservableList<User> modelUsers = FXCollections.observableArrayList();
    public ObservableList<Friendship> modelFriends = FXCollections.observableArrayList();

    Stage primaryStage;

    public void setLoginController(NetworkService service, Stage primaryStage) {
        this.service = service;
        this.primaryStage = primaryStage;
    }

    private void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        User userFound = service.findUsername(username);
        if (userFound == null) {
            AlertMessages.showMessage(primaryStage, Alert.AlertType.ERROR, "Login Error", "User not found!");
        } else if (userFound.getPassword().equals(password)) {
            try {
                clearFields();
                // Load the main view
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/org/example/lab6networkfx/main-view.fxml"));

                AnchorPane root = loader.load();

                // Create a new stage for the main view
                Stage mainStage = new Stage();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/main-view.css").toExternalForm());
                mainStage.setTitle("Home");
                mainStage.setScene(scene);

                // Set up the controller for the main view
                MainController controller = loader.getController();
                controller.setNetworkService(service, userFound, mainStage);

                // Show the main view stage
                mainStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AlertMessages.showMessage(primaryStage, Alert.AlertType.ERROR, "Login Error", "Incorrect password!");
        }
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/lab6networkfx/views/signup-view.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/main-view.css").toExternalForm());
            stage.setTitle("Sign Up");
            stage.setScene(scene);

            SignupController controller = loader.getController();
            controller.setSignup(stage, service);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        primaryStage.close();
    }
}
