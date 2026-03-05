package com.chatui.Client;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {
    @Override
    public void start(Stage stage) {
        Parameters params = getParameters();
        List<String> args = params.getRaw();

        String host = args.size() > 0 ? args.get(0) : "localhost";
        int port    = args.size() > 1 ? Integer.parseInt(args.get(1)) : 4422;

        ClientView view = new ClientView();
        ClientModel model = new ClientModel();

        new ClientController(model, view, host, port);

        Scene scene = new Scene(view, 1020, 660);
        scene.getStylesheets().add(
            getClass().getResource("/client.css").toExternalForm()
        );

        stage.setTitle("Group Chat");
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}