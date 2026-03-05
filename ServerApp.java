package com.chatui.Server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerApp extends Application {
    @Override
    public void start(Stage stage) {
        ServerView view = new ServerView();
        ServerModel model = new ServerModel();
        
        // Initialize the controller
        new ServerController(model, view);

        Scene scene = new Scene(view, 900, 600);
        stage.setTitle("TCP Server - Monitoring");
        scene.getStylesheets().add(getClass().getResource("/server.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}