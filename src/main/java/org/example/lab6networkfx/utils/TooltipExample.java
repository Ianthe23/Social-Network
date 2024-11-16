package org.example.lab6networkfx.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TooltipExample extends Application {

    @Override
    public void start(Stage stage) {
        Button button = new Button("Hover me");

        // Create a Tooltip and set its text
        Tooltip tooltip = new Tooltip("This is the button name");
        button.setTooltip(tooltip);

        VBox vbox = new VBox(button);
        Scene scene = new Scene(vbox, 300, 200);

        stage.setScene(scene);
        stage.setTitle("Tooltip Example");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

