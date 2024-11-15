package org.example.lab6networkfx.controller;

import javafx.fxml.FXML;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.lab6networkfx.domain.Friendship;
import org.example.lab6networkfx.service.NetworkService;
import org.example.lab6networkfx.utils.events.EventType;
import org.example.lab6networkfx.utils.events.NetworkEvent;
import org.example.lab6networkfx.utils.observer.Observer;
import org.example.lab6networkfx.domain.User;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;


public class MainController implements Observer<NetworkEvent> {
    NetworkService service;

    @FXML
    private RadioButton usersRadioButton;

    @FXML
    private RadioButton friendshipsRadioButton;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private VBox tablePanel;

    @FXML
    private Label noTableLabel;

    @FXML
    private HBox tableHeader;

    //USERS TABLE
    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, Integer> tableUserID;

    @FXML
    private TableColumn<User, String> tableFirstName;

    @FXML
    private TableColumn<User, String> tableLastName;

    @FXML
    private TableColumn<User, String> tableUsername;

    //FRIENDSHIPS TABLE
    @FXML
    private TableView<Friendship> friendshipTableView;

    @FXML
    private TableColumn<Friendship, String> tableFriendshipUser1;

    @FXML
    private TableColumn<Friendship, String> tableFriendshipUser2;

    @FXML
    private TableColumn<Friendship, String> tableFriendshipDate ;



    @FXML
    public void initialize() {
        // Initial visibility settings for the table
        tablePanel.setVisible(false);
        tablePanel.setManaged(false);

        tableHeader.setVisible(false);
        tableHeader.setManaged(false);

        // Initialize toggle group for radio buttons
        ToggleGroup toggleGroup = new ToggleGroup();
        usersRadioButton.setToggleGroup(toggleGroup);
        friendshipsRadioButton.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (usersRadioButton.isSelected()) {
                tablePanel.setVisible(true);
                tablePanel.setManaged(true);

                userTableView.setVisible(true);
                userTableView.setManaged(true);

                friendshipTableView.setVisible(false);
                friendshipTableView.setManaged(false);

                noTableLabel.setVisible(false);
                noTableLabel.setManaged(false);

                tableHeader.setVisible(true);
                tableHeader.setManaged(true);

                initializeUsers();
            } else if (friendshipsRadioButton.isSelected()) {
                tablePanel.setVisible(true);
                tablePanel.setManaged(true);

                friendshipTableView.setVisible(true);
                friendshipTableView.setManaged(true);

                userTableView.setVisible(false);
                userTableView.setManaged(false);

                noTableLabel.setVisible(false);
                noTableLabel.setManaged(false);

                tableHeader.setVisible(true);
                tableHeader.setManaged(true);

                initializeFriendships();
            } else {
                tablePanel.setVisible(false);
                tablePanel.setManaged(false);

                tableHeader.setVisible(false);
                tableHeader.setManaged(false);

                noTableLabel.setVisible(true);
                noTableLabel.setManaged(true);
            }
        });

        configureButtons();
    }

    @FXML
    private void handleUserPanelClick(ActionEvent event) {
        if (event.getSource() == btnAdd) {
            handleSceneInputData(null);
        }
    }

    public void handleSceneInputData(User user) {
        try {
            URL resourceUrl = getClass().getResource("/org/example/lab6networkfx/inputDataView.fxml");
            System.out.println("Resource URL: " + resourceUrl);

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setLocation(resourceUrl);

            AnchorPane root = (AnchorPane) loader.load();

            Stage inputDataStage = new Stage();
            inputDataStage.setTitle("Input Data");
            inputDataStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            inputDataStage.setScene(scene);

            InputDataController inputDataController = loader.getController();
            inputDataController.setService(service, inputDataStage, user);
            inputDataStage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private User getSelectedUser() {
        User selected = userTableView.getSelectionModel().getSelectedItem();
        return selected;
    }

    @Override
    public void update(NetworkEvent networkEvent) {
        System.out.println("MainController: " + networkEvent);
    }

    private void configureFadeTransition(Button button) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), button);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.5);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);
        button.setOnMouseEntered(event -> fadeTransition.playFromStart());
    }

    private void configureButtons() {
        configureFadeTransition(btnAdd);
        configureFadeTransition(btnDelete);
    }

    private void initializeUsers() {
        tableUserID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    private void initializeFriendships() {
        tableFriendshipUser1.setCellValueFactory(new PropertyValueFactory<>("user1"));
        tableFriendshipUser2.setCellValueFactory(new PropertyValueFactory<>("user2"));
        tableFriendshipDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

}
