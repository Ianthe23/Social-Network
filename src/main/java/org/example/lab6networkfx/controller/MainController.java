package org.example.lab6networkfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.lab6networkfx.domain.Friendship;
import org.example.lab6networkfx.domain.friendships.FriendshipRequest;
import org.example.lab6networkfx.domain.messages.Message;
import org.example.lab6networkfx.service.MessageService;
import org.example.lab6networkfx.service.NetworkService;
import org.example.lab6networkfx.utils.events.EventType;
import org.example.lab6networkfx.utils.events.NetworkEvent;
import org.example.lab6networkfx.utils.observer.Observer;
import org.example.lab6networkfx.domain.User;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class MainController implements Observer<NetworkEvent> {
    private NetworkService service;
    private MessageService messageService;
    private User userLogged;
    private User selectedUser;

    private List<FriendshipRequest> friendRequests;

    Stage mainStage;

    @FXML
    private Label userLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label friendRequestsLabel;

    @FXML
    private RadioButton usersRadioButton;

    @FXML
    private RadioButton friendshipsRadioButton;

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
    private Button btnMessage;

    @FXML
    private Button btnSendMessage;

    @FXML
    private AnchorPane messagePane;

    @FXML
    private ListView<Message> chatListView;
    @FXML
    private TextField messageInput;
    @FXML
    private TextField searchField;
    @FXML
    private Label chatLabel;
    @FXML
    private Button btnDeleteMessage;
    @FXML
    private Button btnEditMessage;
    @FXML
    private Button btnReplyMessage;

    @FXML
    private VBox listPanel;

    @FXML
    private Label noTableLabel;

    @FXML
    private HBox tableHeader;

    @FXML
    private ObservableList<User> modelUsers = FXCollections.observableArrayList();
    @FXML
    private ObservableList<Friendship> modelFriendships = FXCollections.observableArrayList();
    @FXML
    private ObservableList<FriendshipRequest> modelFriendRequests = FXCollections.observableArrayList();
    @FXML
    private ObservableList<Message> modelMessages = FXCollections.observableArrayList();

    //USERS TABLE
    @FXML
    private ListView<User> userListView;

    @FXML
    private Label listLabel;

    @FXML
    private Button backButton;

//    @FXML
//    private TableColumn<User, Integer> tableUserID;
//
//    @FXML
//    private TableColumn<User, String> tableFirstName;
//
//    @FXML
//    private TableColumn<User, String> tableLastName;
//
//    @FXML
//    private TableColumn<User, String> tableUsername;
//
//    //FRIENDSHIPS TABLE
//    @FXML
//    private TableView<Friendship> friendshipTableView;
//
//    @FXML
//    private TableColumn<Friendship, String> tableFriendshipUser1;
//
//    @FXML
//    private TableColumn<Friendship, String> tableFriendshipUser2;
//
//    @FXML
//    private TableColumn<Friendship, String> tableFriendshipDate ;

    public void setNetworkService(NetworkService service, MessageService messageService, User user, Stage stage) {
        this.service = service;
        this.messageService = messageService;
        this.userLogged = user;
        this.mainStage = stage;
        userLabel.setText(userLogged.getUsername());
        fullNameLabel.setText(userLogged.getFirstName() + " " + userLogged.getLastName());
        service.addObserver(this);
        messageService.addObserver(this);
        initModelUsers();
        initModelFriendships();
        initModelFriendRequests();
        initModelMessages(null, null);
    }

    @FXML
    public void initialize() {
        initializeUsers();
//        initializeFriendships();
        backButton.setVisible(false);
        backButton.setManaged(false);

        // Initial visibility settings for the table
        listPanel.setVisible(false);
        listPanel.setManaged(false);

        tableHeader.setVisible(false);
        tableHeader.setManaged(false);

        // Initialize toggle group for radio buttons
        ToggleGroup toggleGroup = new ToggleGroup();
        usersRadioButton.setToggleGroup(toggleGroup);
        friendshipsRadioButton.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (usersRadioButton.isSelected()) {
                listPanel.setVisible(true);
                listPanel.setManaged(true);

                userListView.setVisible(true);
                userListView.setManaged(true);

                noTableLabel.setVisible(false);
                noTableLabel.setManaged(false);

                tableHeader.setVisible(true);
                tableHeader.setManaged(true);

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
                listPanel.setVisible(true);
                listPanel.setManaged(true);

                userListView.setVisible(false);
                userListView.setManaged(false);

                noTableLabel.setVisible(false);
                noTableLabel.setManaged(false);

                tableHeader.setVisible(true);
                tableHeader.setManaged(true);

                btnDeleteFriend.setVisible(false);
                btnDeleteFriend.setManaged(false);

                btnFriendAdd.setVisible(false);
                btnFriendAdd.setManaged(false);

                btnFriendList.setVisible(false);
                btnFriendList.setManaged(false);

                btnSendRequest.setVisible(false);
                btnSendRequest.setManaged(false);

            } else {
                listPanel.setVisible(false);
                listPanel.setManaged(false);

                tableHeader.setVisible(false);
                tableHeader.setManaged(false);

                noTableLabel.setVisible(true);
                noTableLabel.setManaged(true);
            }
        });

        configureButtons();
    }

    private void updateFriendRequestLabel() {
        //get the last friendship request from model that is not in friendRequest
        FriendshipRequest lastFriendshipRequest = null;
        for (FriendshipRequest friendshipRequest : modelFriendRequests) {
            if (!friendRequests.contains(friendshipRequest)) {
                lastFriendshipRequest = friendshipRequest;
                break;
            }
        }
        if (lastFriendshipRequest.getUser2().getUsername().equals(userLogged.getUsername())){
            AlertMessages.showMessage(mainStage, Alert.AlertType.INFORMATION, "New Notification", "You have a new friend request!");
            friendRequestsLabel.setText(lastFriendshipRequest.getUser1().getUsername() + " sent you a friend request!");
        }
    }

    public void handleUserItemClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // Detect single click
            User selectedItem = userListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                int selectedIndex = userListView.getSelectionModel().getSelectedIndex();
                boolean alreadySelected = userListView.getSelectionModel().isSelected(selectedIndex);
                if (alreadySelected) {
                    // Deselect the line if it is already selected
                    userListView.getSelectionModel().clearSelection();
                }
            }
        }
    }

    public void handleMessageItemClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // Detect single click
            Message selectedItem = chatListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                int selectedIndex = chatListView.getSelectionModel().getSelectedIndex();
                boolean alreadySelected = chatListView.getSelectionModel().isSelected(selectedIndex);
                if (alreadySelected) {
                    // Deselect the line if it is already selected
                    chatListView.getSelectionModel().clearSelection();
                }
            }
        }
    }

//    public void handleFriendshipRowClick(MouseEvent event) {
//        if (event.getClickCount() == 1) { // Se detectează un singur click
//            Friendship selectedItem = friendshipTableView.getSelectionModel().getSelectedItem();
//            if (selectedItem != null) {
//                boolean alreadySelected = friendshipTableView.getSelectionModel().isSelected(
//                        friendshipTableView.getSelectionModel().getSelectedIndex());
//                if (alreadySelected) {
//                    // Deselectăm linia dacă este deja selectată
//                    friendshipTableView.getSelectionModel().clearSelection();
//                }
//            }
//        }
//    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String searchText = searchField.getText();
            List<User> searchResults = new ArrayList<>();
            for (User user : modelUsers) {
                if (user.getUsername().contains(searchText) || user.getFirstName().contains(searchText) || user.getLastName().contains(searchText)) {
                    searchResults.add(user);
                }
            }
            userListView.setItems(FXCollections.observableArrayList(searchResults));
    }

    @FXML
    private void handleUserPanelClick(ActionEvent event) {
        if (usersRadioButton.isSelected()) {
            if (event.getSource() == btnSendRequest) {
                if (getSelectedUser() != null) {
                    handleSceneInputFriendRequest(getSelectedUser());
                } else {
                    AlertMessages.showMessage(mainStage, Alert.AlertType.ERROR, "Error", "No user selected!");
                }
            } else if (event.getSource() == btnFriendAdd) {
                    handleSceneInputFriendship();
            } else if (event.getSource() == btnDeleteFriend) {
                handleSceneInputDeleteFriend();
            } else if (event.getSource() == btnFriendList) {
                handleSceneTableFriendList();
            } else if (event.getSource() == btnFriendRequestsList) {
                handleSceneInputRequests(getSelectedUser());
            } else if (event.getSource() == backButton) {
                handleBackButton();
            } else if (event.getSource() == btnMessage) {
                if (getSelectedUser() != null) {
                    selectedUser = getSelectedUser();
                    initializeMessages(userLogged, selectedUser);
                } else {
                    AlertMessages.showMessage(mainStage, Alert.AlertType.ERROR, "Error", "No user selected!");
                }
            } else if (event.getSource() == btnSendMessage) {
                handleSendMessage(selectedUser);
            } else if (event.getSource() == btnDeleteMessage) {
                if (getSelectedMessage() != null) {
                    messageService.deleteMessage(getSelectedMessage());
                } else {
                    AlertMessages.showMessage(mainStage, Alert.AlertType.ERROR, "Error", "No message selected!");
                }
            } else if (event.getSource() == btnReplyMessage) {
                if (getSelectedMessage() == null || messageInput.getText().isEmpty()) {
                    AlertMessages.showMessage(mainStage, Alert.AlertType.ERROR, "Error", "No message selected or message is empty!");
                } else {
                    handleReplyMessage();
                }
            }

        } else if (friendshipsRadioButton.isSelected()) {
            if (event.getSource() == btnFriendRequestsList) {
                handleSceneInputRequests(null);
            }
        }
    }

    private void handleSendMessage(User user) {
        String message = messageInput.getText();
        if (message.isEmpty()) {
            AlertMessages.showMessage(mainStage, Alert.AlertType.ERROR, "Error", "Message cannot be empty!");
        } else {
            messageService.sendMessage(userLogged, user, message, -1);
            //sort the messages by date
            modelMessages.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
//            modelMessages.add(new Message(message, LocalDateTime.now(), user, userLogged)); // Update the model
            messageInput.clear();
        }
    }

    private void handleReplyMessage() {
        Message selectedMessage = getSelectedMessage();
        String message = messageInput.getText();
        if (message.isEmpty()) {
            AlertMessages.showMessage(mainStage, Alert.AlertType.ERROR, "Error", "Message cannot be empty!");
        } else {
            messageService.sendMessage(userLogged, selectedMessage.getFrom(), message, selectedMessage.getId());
            //sort the messages by date
            modelMessages.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        }
    }

    private void handleBackButton() {
        userListView.setItems(modelUsers);
        backButton.setVisible(false);
        backButton.setManaged(false);
        listLabel.setText("All users");
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

    public void handleSceneTableFriendList() {
        //modify the listView to show the friends of the user
        userListView.setItems(null);
        //iterate through the users and if the username is equal to the userLogged, set the items to the friends of the user
        for (User user : modelUsers) {
            if (user.getUsername().equals(userLogged.getUsername())) {
                userListView.setItems(FXCollections.observableArrayList(user.getFriendships()));
            }
        }
        backButton.setVisible(true);
        backButton.setManaged(true);
        listLabel.setText("Friends List");
    }

    public void handleSceneInputFriendship() {
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
            inputFriendshipController.setService(service, inputDataStage, userLogged);
            inputDataStage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void handleSceneInputDeleteFriend() {
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
            inputDeleteFriendController.setService(service, inputDataStage, userLogged);
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
            inputFriendRequestController.setService(service, inputDataStage, userLogged, user);
            inputDataStage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

//    private void handleDeleteFriendship() {
//        Friendship selected = friendshipTableView.getSelectionModel().getSelectedItem();
//        if (selected != null) {
//            service.removeFriendship(selected.getUser1().getUsername(), selected.getUser2().getUsername());
//            AlertMessages.showMessage(mainStage, Alert.AlertType.CONFIRMATION, "Friendship deleted", "Friendship between " + selected.getUser1().getUsername() + " and " + selected.getUser2().getUsername() + " was deleted successfully!");
//        } else {
//            AlertMessages.showMessage(mainStage, Alert.AlertType.ERROR, "Error", "No friendship selected!");
//        }
//    }

    private User getSelectedUser() {
        User selected = userListView.getSelectionModel().getSelectedItem();
        return selected;
    }

    private Message getSelectedMessage() {
        Message selected = chatListView.getSelectionModel().getSelectedItem();
        return selected;
    }

    private void initModelMessages(User user1, User user2) {
        Iterable<Message> messages;
        if (user1 == null || user2 == null) {
            messages = messageService.getAllMessages();
        }
        else {
            messages = messageService.getMessagesBetweenUsers(user1, user2);
        }
        List<Message> messageList = StreamSupport.stream(messages.spliterator(), false).collect(Collectors.toList());
        modelMessages.setAll(messageList);
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

    private void initModelFriendRequests() {
        friendRequests = new ArrayList<>(modelFriendRequests);
        Iterable<FriendshipRequest> friendshipRequests = service.getAllFriendshipRequests();
        List<FriendshipRequest> friendshipRequestList = StreamSupport.stream(friendshipRequests.spliterator(), false).collect(Collectors.toList());
        modelFriendRequests.setAll(friendshipRequestList);

    }

    @Override
    public void update(NetworkEvent networkEvent) {
        initModelUsers();
        initModelFriendships();
        initModelFriendRequests();
        initModelMessages(userLogged, selectedUser);

        if (networkEvent.getType() == EventType.PEND) {
            System.out.println("Pending request");
            updateFriendRequestLabel();
        }

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
        configureFadeTransition(btnFriendAdd);
        configureFadeTransition(btnFriendList);
        configureFadeTransition(btnFriendRequestsList);
        configureFadeTransition(btnSendRequest);
        configureFadeTransition(btnDeleteFriend);
        configureFadeTransition(backButton);
        configureFadeTransition(btnMessage);
        configureFadeTransition(btnSendMessage);
        configureFadeTransition(btnDeleteMessage);
        configureFadeTransition(btnEditMessage);
        configureFadeTransition(btnReplyMessage);
    }

    public void initializeUsers() {
        userListView.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label nameLabel = new Label(user.getFirstName() + " " + user.getLastName());
                    Label usernameLabel = new Label(user.getUsername());

                    nameLabel.setStyle("-fx-text-fill: #ffffff;");
                    usernameLabel.setStyle("-fx-text-fill: #ffffff;");

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    HBox hbox = new HBox(nameLabel, spacer, usernameLabel);
                    hbox.setSpacing(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(hbox);
                }
            }
        });

        userListView.setItems(modelUsers); // Populate with your user data
    }

    private void initializeMessages(User user1, User user2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        modelMessages.setAll(messageService.getMessagesBetweenUsers(user1, user2));
        chatLabel.setText("Chat with " + user2.getUsername());

        chatListView.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create components for the message
                    Label messageLabel = new Label(message.getMessage());
                    messageLabel.setWrapText(true);
                    messageLabel.setMaxWidth(170);

                    Label dateLabel = new Label(message.getDate().format(formatter));
                    dateLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 10px;");
                    dateLabel.setAlignment(Pos.CENTER_RIGHT);

                    VBox messageVBox = new VBox(messageLabel, dateLabel);
                    messageVBox.setSpacing(2);
                    messageVBox.setAlignment(Pos.CENTER_RIGHT);
                    messageVBox.setStyle("-fx-background-radius: 10; -fx-padding: 5 10 5 10;");

                    // Check if the message is a reply
                    if (message.getReplyingTo() != -1) {
                        // Retrieve the original message using the replyingTo ID
                        Message originalMessage = messageService.getAllMessages().stream()
                                .filter(x -> x.getId().equals(message.getReplyingTo()))
                                .findFirst()
                                .orElse(null);

                        if (originalMessage != null) {
                            Label originalMessageLabel = new Label(originalMessage.getMessage());
                            originalMessageLabel.setWrapText(true);
                            originalMessageLabel.setMaxWidth(150);

                            Label originalSenderLabel = new Label("Reply to " + originalMessage.getFrom().getUsername());
                            originalSenderLabel.setStyle("-fx-font-size: 10px;");

                            VBox replyVBox = new VBox(originalSenderLabel, originalMessageLabel);
                            if (message.getFrom().equals(userLogged)) {
                                replyVBox.setStyle("-fx-background-color: #959fee; -fx-background-radius: 8; -fx-padding: 5 10 5 10;");
                            } else {
                                replyVBox.setStyle("-fx-background-color: #8f8b8a; -fx-background-radius: 8; -fx-padding: 5 10 5 10;");
                            }
                            replyVBox.setSpacing(2);
                            replyVBox.setAlignment(Pos.CENTER_LEFT);

                            // Add a border to visually separate the reply box
                            replyVBox.setStyle(replyVBox.getStyle() + "-fx-border-color: #bbbbbb; -fx-border-width: 1; -fx-border-radius: 8;");

                            messageVBox.getChildren().add(0, replyVBox); // Prepend the reply VBox to the message VBox
                        }
                    }

                    if (message.getFrom().equals(userLogged)) {
                        messageVBox.setStyle(messageVBox.getStyle() + "-fx-background-color: #7c8bef; -fx-text-fill: #FFFFFF;");
                    } else {
                        messageVBox.setStyle(messageVBox.getStyle() + "-fx-background-color: #706d6c; -fx-text-fill: #FFFFFF;");
                    }

                    HBox hbox = new HBox(messageVBox);
                    hbox.setSpacing(10);
                    hbox.setAlignment(message.getFrom().equals(userLogged) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

                    setGraphic(hbox);
                }
            }
        });

        chatListView.setItems(modelMessages);
    }

//    private void initializeFriendships() {
//        tableFriendshipUser1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser1().getUsername()));
//        tableFriendshipUser2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser2().getUsername()));
//        tableFriendshipDate.setCellValueFactory(new PropertyValueFactory<>("since"));
//        friendshipTableView.setItems(modelFriendships);
//    }

}
