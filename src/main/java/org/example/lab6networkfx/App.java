package org.example.lab6networkfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.domain.validators.Validator;
import org.example.lab6networkfx.domain.validators.ValidatorFactory;
import org.example.lab6networkfx.domain.validators.ValidatorStrategy;
import org.example.lab6networkfx.repository.database.factory.DataBaseRepoFactory;
import org.example.lab6networkfx.repository.database.factory.DataBaseStrategy;
import org.example.lab6networkfx.repository.database.utils.AbstractDataBaseRepo;
import org.example.lab6networkfx.repository.database.utils.DataBaseAcces;
import org.example.lab6networkfx.service.NetworkService;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {
    private DataBaseAcces data;
    private AbstractDataBaseRepo<Integer, User> userRepo;
    private AbstractDataBaseRepo<Integer, User> friendshipRepo;
    public NetworkService service;

    @Override
    public void start(Stage stage) throws Exception {
        NetworkService service;
        ValidatorFactory factory = ValidatorFactory.getInstance();
        Validator userValidator = factory.createValidator(ValidatorStrategy.User);
        Validator friendValidator = factory.createValidator(ValidatorStrategy.Friendship);

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "ivona2004";

        this.data = new DataBaseAcces(url, username, password);
        try {
            data.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DataBaseRepoFactory repoFactory = new DataBaseRepoFactory(data);
        this.userRepo = repoFactory.createRepo(DataBaseStrategy.User, userValidator);
        this.friendshipRepo = repoFactory.createRepo(DataBaseStrategy.Friendship, friendValidator);
        this.service = new NetworkService(userRepo, friendshipRepo, "database");
        initView(stage);
        stage.setMinHeight(400);
        stage.setMinWidth(700);
        stage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("mainView.fxml"));
        Scene scene = new Scene(stageLoader.load());
        scene.getStylesheets().add(getClass().getResource("/org/example/lab6networkfx/styles/mainView.css").toExternalForm());
        primaryStage.setTitle("Network Application");
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
