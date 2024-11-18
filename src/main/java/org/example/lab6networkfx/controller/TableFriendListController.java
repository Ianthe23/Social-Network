package org.example.lab6networkfx.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.lab6networkfx.domain.Friendship;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.domain.friendships.FriendshipRequest;
import org.example.lab6networkfx.service.NetworkService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TableFriendListController {
    @FXML
    private TableView<Friendship> friendListTableView;

    @FXML
    private TableColumn<Friendship, String> tableFriendListUser2;

    @FXML
    private TableColumn<Friendship, String> tableFriendListDate;

    @FXML
    private ObservableList<Friendship> items = FXCollections.observableArrayList();

    NetworkService  service;
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
        tableFriendListUser2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser2().getUsername()));
        tableFriendListDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSince().toString()));

        friendListTableView.setItems(items);
    }

    private void initItems() {
        items.clear();

        service.getAllFriendships().forEach(friendship -> {
            if (((Friendship) friendship).getUser1().getUsername().equals(user.getUsername())) {
                items.add(((Friendship)friendship));
            }
        });

        service.getAllFriendships().forEach(friendship -> {
            if (((Friendship) friendship).getUser2().getUsername().equals(user.getUsername())) {
                items.add(((Friendship)friendship));
            }
        });

    }
}
