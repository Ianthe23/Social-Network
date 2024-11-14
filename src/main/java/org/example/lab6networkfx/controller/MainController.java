package org.example.lab6networkfx.controller;

import javafx.fxml.FXML;
import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import org.example.lab6networkfx.service.NetworkService;
import org.example.lab6networkfx.utils.events.NetworkEvent;
import org.example.lab6networkfx.utils.observer.Observer;


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
    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();
        usersRadioButton.setToggleGroup(toggleGroup);
        friendshipsRadioButton.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (usersRadioButton.isSelected() || friendshipsRadioButton.isSelected()) {
                tablePanel.setVisible(true);
                tablePanel.setManaged(true);

                noTableLabel.setText("Table");
                noTableLabel.setTextFill(javafx.scene.paint.Color.web("#ffffff"));

//                noTableLabel.setVisible(false);
//                noTableLabel.setManaged(false);

                if (usersRadioButton.isSelected()) {
                    System.out.println("Users selected");
                } else {
                    System.out.println("Friendships selected");
                }
            } else {
                tablePanel.setVisible(false);
                tablePanel.setManaged(false);

                noTableLabel.setText("No table selected");
                noTableLabel.setTextFill(javafx.scene.paint.Color.web("#FF0000"));
                noTableLabel.setVisible(true);
                noTableLabel.setManaged(true);
            }
        });

        configureButtons();

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

}
