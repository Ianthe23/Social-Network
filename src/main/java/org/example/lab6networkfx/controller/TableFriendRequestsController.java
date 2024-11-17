package org.example.lab6networkfx.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.domain.friendships.FriendshipRequest;
import org.example.lab6networkfx.service.NetworkService;
import org.example.lab6networkfx.utils.events.NetworkEvent;

import java.util.stream.StreamSupport;
import java.util.List;

import java.util.stream.Collectors;

public class TableFriendRequestsController {

    @FXML
    private TableView<FriendshipRequest> friendRequestTableView;

    @FXML
    private TableColumn<FriendshipRequest, String> tableFriendRequestUser1;

    @FXML
    private TableColumn<FriendshipRequest, String> tableFriendRequestUser2;

    @FXML
    private TableColumn<FriendshipRequest, String> tableFriendRequestStatus;

    @FXML
    private TableColumn<FriendshipRequest, String> tableFriendRequestDate;

    @FXML
    private ObservableList<FriendshipRequest> items = FXCollections.observableArrayList();

    NetworkService service;
    Stage inputStage;
    User user;

    public void setService(NetworkService service, Stage stage, User user) {
        this.service = service;
        this.inputStage = stage;
        this.user = user;

        initItems();
        initializeTable();
    }

    @FXML
    public void initialize() {
        initializeTable();
    }

    private void initializeTable() {
        tableFriendRequestUser1.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getUser1().getUsername()));
        tableFriendRequestUser2.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getUser2().getUsername()));
        tableFriendRequestStatus.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getStatus().toString()));
        tableFriendRequestDate.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getDate().toString()));

        friendRequestTableView.setItems(items);
    }

    private void initItems() {
        items.clear();

        if (user==null) {
            Iterable<FriendshipRequest> requests = service.getAllFriendshipRequests();
            List<FriendshipRequest> userRequests = StreamSupport.stream(requests.spliterator(), false).collect(Collectors.toList());
            items.setAll(userRequests);
        }
        else {
            List<FriendshipRequest> userRequests = service.getPendingFriendshipRequests(user.getUsername());
            items.setAll(userRequests);
        }


    }
}
