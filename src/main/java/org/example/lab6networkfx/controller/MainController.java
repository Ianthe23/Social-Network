package org.example.lab6networkfx.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


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
    private Button btnSendRequest;

    @FXML
    private Button btnFriendAdd;

    @FXML
    private Button btnDeleteFriend;

    @FXML
    private Button btnFriendList;

    @FXML
    private Button btnFriendRequestsList;

    @FXML
    private VBox tablePanel;

    @FXML
    private Label noTableLabel;

    @FXML
    private HBox tableHeader;

    @FXML
    private ObservableList<User> modelUsers = FXCollections.observableArrayList();
    @FXML
    private ObservableList<Friendship> modelFriendships = FXCollections.observableArrayList();

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

    public void setNetworkService(NetworkService service) {
        this.service = service;
        service.addObserver(this);
        initModelUsers();
        initModelFriendships();
    }

    @FXML
    public void initialize() {
        initializeUsers();
        initializeFriendships();

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

                btnAdd.setVisible(true);
                btnAdd.setManaged(true);

                btnSendRequest.setVisible(true);
                btnSendRequest.setManaged(true);

                btnFriendAdd.setVisible(true);
                btnFriendAdd.setManaged(true);

                btnDeleteFriend.setVisible(true);
                btnDeleteFriend.setManaged(true);

                btnFriendList.setVisible(true);
                btnFriendList.setManaged(true);

                btnFriendRequestsList.setVisible(true);
                btnFriendRequestsList.setManaged(true);

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

                btnAdd.setVisible(false);
                btnAdd.setManaged(false);

                btnDeleteFriend.setVisible(false);
                btnDeleteFriend.setManaged(false);

                btnFriendAdd.setVisible(false);
                btnFriendAdd.setManaged(false);

                btnFriendList.setVisible(false);
                btnFriendList.setManaged(false);

                btnSendRequest.setVisible(false);
                btnSendRequest.setManaged(false);

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
    public void handleUserRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // Se detectează un singur click
            User selectedItem = userTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                boolean alreadySelected = userTableView.getSelectionModel().isSelected(
                        userTableView.getSelectionModel().getSelectedIndex());
                if (alreadySelected) {
                    // Deselectăm linia dacă este deja selectată
                    userTableView.getSelectionModel().clearSelection();
                }
            }
        }
    }

    public void handleFriendshipRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // Se detectează un singur click
            Friendship selectedItem = friendshipTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                boolean alreadySelected = friendshipTableView.getSelectionModel().isSelected(
                        friendshipTableView.getSelectionModel().getSelectedIndex());
                if (alreadySelected) {
                    // Deselectăm linia dacă este deja selectată
                    friendshipTableView.getSelectionModel().clearSelection();
                }
            }
        }
    }

    @FXML
    private void handleUserPanelClick(ActionEvent event) {
        if (usersRadioButton.isSelected()) {
            if (event.getSource() == btnAdd) {
                handleSceneInputData(null);
            } else if (event.getSource() == btnDelete) {
                handleDeleteUser();
            } else if (event.getSource() == btnSendRequest) {
                if (getSelectedUser() != null) {
                    handleSceneInputFriendRequest(getSelectedUser());
                } else {
                    AlertMessages.showMessage(null, Alert.AlertType.ERROR, "Error", "No user selected!");
                }
            } else if (event.getSource() == btnFriendAdd) {
                if (getSelectedUser() != null) {
                    handleSceneInputFriendship(getSelectedUser());
                } else {
                    AlertMessages.showMessage(null, Alert.AlertType.ERROR, "Error", "No user selected!");
                }
            } else if (event.getSource() == btnDeleteFriend) {
                if (getSelectedUser() != null) {
                    handleSceneInputDeleteFriend(getSelectedUser());
                } else {
                    AlertMessages.showMessage(null, Alert.AlertType.ERROR, "Error", "No user selected!");
                }
            } else if (event.getSource() == btnFriendList) {
                if (getSelectedUser() != null) {
                    handleSceneTableFriendList(getSelectedUser());
                } else {
                    AlertMessages.showMessage(null, Alert.AlertType.ERROR, "Error", "No user selected!");
                }
            } else if (event.getSource() == btnFriendRequestsList) {
                handleSceneInputRequests(getSelectedUser());
            }

        } else if (friendshipsRadioButton.isSelected()) {
            if (event.getSource() == btnDelete) {
                handleDeleteFriendship();
            } else if (event.getSource() == btnFriendRequestsList) {
                handleSceneInputRequests(null);
            }
        }
    }

    public void handleSceneInputData(User user) {
        try {
            URL resourceUrl = getClass().getResource("/org/example/lab6networkfx/views/input-user-view.fxml");
            System.out.println("Resource URL: " + resourceUrl);

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setLocation(resourceUrl);

            AnchorPane root = (AnchorPane) loader.load();

            Stage inputDataStage = new Stage();
            inputDataStage.setTitle("Input Data User");
            inputDataStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/main-view.css").toExternalForm());
            inputDataStage.setScene(scene);

            InputUserController inputUserController = loader.getController();
            inputUserController.setService(service, inputDataStage, user);
            inputDataStage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void handleSceneInputRequests(User user) {
        try {
            URL resourceUrl = getClass().getResource("/org/example/lab6networkfx/views/table-friend-requests-view.fxml");
            System.out.println("Resource URL: " + resourceUrl);

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setLocation(resourceUrl);

            AnchorPane root = (AnchorPane) loader.load();

            Stage inputDataStage = new Stage();
            inputDataStage.setTitle("Table Friend Requests");
            inputDataStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/main-view.css").toExternalForm());
            inputDataStage.setScene(scene);

            TableFriendRequestsController tableFriendRequestsController = loader.getController();
            tableFriendRequestsController.setService(service, inputDataStage, user);
            inputDataStage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void handleSceneTableFriendList(User user) {
        try {
            URL resourceUrl = getClass().getResource("/org/example/lab6networkfx/views/table-friends-view.fxml");
            System.out.println("Resource URL: " + resourceUrl);

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setLocation(resourceUrl);

            AnchorPane root = (AnchorPane) loader.load();

            Stage inputDataStage = new Stage();
            inputDataStage.setTitle("Table Friends List");
            inputDataStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/main-view.css").toExternalForm());
            inputDataStage.setScene(scene);

            TableFriendListController tableFriendListController= loader.getController();
            tableFriendListController.setService(service, inputDataStage, user);
            inputDataStage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void handleSceneInputFriendship(User user) {
        try {
            URL resourceUrl = getClass().getResource("/org/example/lab6networkfx/views/input-friendship-view.fxml");
            System.out.println("Resource URL: " + resourceUrl);

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setLocation(resourceUrl);

            AnchorPane root = (AnchorPane) loader.load();

            Stage inputDataStage = new Stage();
            inputDataStage.setTitle("Input Data Friendship");
            inputDataStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/main-view.css").toExternalForm());
            inputDataStage.setScene(scene);

            InputFriendshipController inputFriendshipController = loader.getController();
            inputFriendshipController.setService(service, inputDataStage, user);
            inputDataStage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void handleSceneInputDeleteFriend(User user) {
        try {
            URL resourceUrl = getClass().getResource("/org/example/lab6networkfx/views/input-delete-friend-view.fxml");
            System.out.println("Resource URL: " + resourceUrl);

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setLocation(resourceUrl);

            AnchorPane root = (AnchorPane) loader.load();

            Stage inputDataStage = new Stage();
            inputDataStage.setTitle("Input Data Friendship");
            inputDataStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/main-view.css").toExternalForm());
            inputDataStage.setScene(scene);

            InputDeleteFriendController inputDeleteFriendController = loader.getController();
            inputDeleteFriendController.setService(service, inputDataStage, user);
            inputDataStage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void handleSceneInputFriendRequest(User user) {
        try {
            URL resourceUrl = getClass().getResource("/org/example/lab6networkfx/views/input-friend-request-view.fxml");
            System.out.println("Resource URL: " + resourceUrl);

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setLocation(resourceUrl);

            AnchorPane root = (AnchorPane) loader.load();

            Stage inputDataStage = new Stage();
            inputDataStage.setTitle("Input Delete Friend");
            inputDataStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/main-view.css").toExternalForm());
            inputDataStage.setScene(scene);

            InputFriendRequestController inputFriendRequestController = loader.getController();
            inputFriendRequestController.setService(service, inputDataStage, user);
            inputDataStage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteUser() {
        User selected = getSelectedUser();
        if (selected != null) {
            service.removeUser(selected.getUsername());
            AlertMessages.showMessage(null, Alert.AlertType.CONFIRMATION, "User deleted", "User " + selected.getUsername() + " was deleted successfully!");
        } else {
            AlertMessages.showMessage(null, Alert.AlertType.ERROR, "Error", "No user selected!");
        }
    }

    private void handleDeleteFriendship() {
        Friendship selected = friendshipTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.removeFriendship(selected.getUser1().getUsername(), selected.getUser2().getUsername());
            AlertMessages.showMessage(null, Alert.AlertType.CONFIRMATION, "Friendship deleted", "Friendship between " + selected.getUser1().getUsername() + " and " + selected.getUser2().getUsername() + " was deleted successfully!");
        } else {
            AlertMessages.showMessage(null, Alert.AlertType.ERROR, "Error", "No friendship selected!");
        }
    }

    private User getSelectedUser() {
        User selected = userTableView.getSelectionModel().getSelectedItem();
        return selected;
    }

    private void initModelUsers() {
        Iterable<User> users = service.getAllUsers();
        List<User> userList = StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList());
        modelUsers.setAll(userList);
    }

    private void initModelFriendships() {
        Iterable<Friendship> friendships = service.getAllFriendships();
        List<Friendship> friendshipList = StreamSupport.stream(friendships.spliterator(), false).collect(Collectors.toList());
        modelFriendships.setAll(friendshipList);
    }

    @Override
    public void update(NetworkEvent networkEvent) {
        initModelUsers();
        initModelFriendships();
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
        configureFadeTransition(btnFriendAdd);
        configureFadeTransition(btnFriendList);
        configureFadeTransition(btnFriendRequestsList);
        configureFadeTransition(btnSendRequest);
        configureFadeTransition(btnDeleteFriend);
    }

    private void initializeUsers() {
        tableUserID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        userTableView.setItems(modelUsers);
    }

    private void initializeFriendships() {
        tableFriendshipUser1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser1().getUsername()));
        tableFriendshipUser2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser2().getUsername()));
        tableFriendshipDate.setCellValueFactory(new PropertyValueFactory<>("since"));
        friendshipTableView.setItems(modelFriendships);
    }

}
