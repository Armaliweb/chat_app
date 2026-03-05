package com.chatui.Server;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ServerView extends GridPane {

    public TextArea serverLogArea;
    public ListView<String> connectedUsersListView;
    public Button startServerButton;
    public Button stopServerButton;
    public Label serverStatusLabel;

    private final Map<String, Color> userColors = new HashMap<>();
    private final Random rng = new Random();

    public ServerView() {
        buildUI();
        setupColoredUsersList();
        setServerRunning(false);
    }

    private void buildUI() {
        getStyleClass().add("root");

        setPadding(new Insets(16));
        setHgap(12);
        setVgap(12);

        ColumnConstraints left = new ColumnConstraints();
        left.setHgrow(Priority.ALWAYS);

        ColumnConstraints right = new ColumnConstraints();
        right.setMinWidth(280);
        right.setMaxWidth(340);

        getColumnConstraints().addAll(left, right);

        GridPane topBar = new GridPane();
        topBar.getStyleClass().add("card");
        topBar.setPadding(new Insets(14));
        topBar.setHgap(10);
        topBar.setVgap(10);

        ColumnConstraints t0 = new ColumnConstraints();
        t0.setHgrow(Priority.ALWAYS);
        ColumnConstraints t1 = new ColumnConstraints(120);
        ColumnConstraints t2 = new ColumnConstraints(120);
        ColumnConstraints t3 = new ColumnConstraints(200);
        topBar.getColumnConstraints().addAll(t0, t1, t2, t3);

        Label title = new Label("Group Chat (Server)");
        title.getStyleClass().add("title");

        startServerButton = new Button("Start");
        startServerButton.getStyleClass().add("primary-button");

        stopServerButton = new Button("Stop");
        stopServerButton.getStyleClass().add("danger-button");

        serverStatusLabel = new Label("Server: Stopped");
        serverStatusLabel.getStyleClass().add("status-label");

        topBar.add(title, 0, 0, 4, 1);
        topBar.add(startServerButton, 1, 1);
        topBar.add(stopServerButton, 2, 1);
        topBar.add(serverStatusLabel, 3, 1);

        add(topBar, 0, 0, 2, 1);

        serverLogArea = new TextArea();
        serverLogArea.setEditable(false);
        serverLogArea.setWrapText(true);
        serverLogArea.setPromptText("Server logs will appear here...");
        serverLogArea.getStyleClass().add("log-area");

        VBox logCard = new VBox(serverLogArea);
        logCard.getStyleClass().add("card");
        logCard.setPadding(new Insets(14));
        VBox.setVgrow(serverLogArea, Priority.ALWAYS);

        add(logCard, 0, 1);
        GridPane.setVgrow(logCard, Priority.ALWAYS);

        Label usersTitle = new Label("Connected Users");
        usersTitle.getStyleClass().add("panel-title");

        connectedUsersListView = new ListView<>();
        connectedUsersListView.setItems(FXCollections.observableArrayList());

        VBox usersCard = new VBox(10, usersTitle, connectedUsersListView);
        usersCard.getStyleClass().add("card");
        usersCard.setPadding(new Insets(14));
        VBox.setVgrow(connectedUsersListView, Priority.ALWAYS);

        add(usersCard, 1, 1);
        GridPane.setVgrow(usersCard, Priority.ALWAYS);
    }


    private void setupColoredUsersList() {
        connectedUsersListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(String username, boolean empty) {
                super.updateItem(username, empty);

                if (empty || username == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(username);

                Color c = userColors.computeIfAbsent(username, u -> randomPastel());
                String rgba = String.format("rgba(%d,%d,%d,0.35)",
                        (int) (c.getRed() * 255),
                        (int) (c.getGreen() * 255),
                        (int) (c.getBlue() * 255));

                setStyle("-fx-background-color: " + rgba + ";" +
                        "-fx-background-insets: 4;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8;");
            }
        });
    }

    private Color randomPastel() {
        double r = 0.55 + rng.nextDouble() * 0.40;
        double g = 0.55 + rng.nextDouble() * 0.40;
        double b = 0.55 + rng.nextDouble() * 0.40;
        return new Color(r, g, b, 1.0);
    }

    public void appendLog(String line) {
        serverLogArea.appendText(line + "\n");
    }

    public void setServerRunning(boolean running) {
        if (running) {
            serverStatusLabel.setText("Server: Running");
            startServerButton.setDisable(true);
            stopServerButton.setDisable(false);
        } else {
            serverStatusLabel.setText("Server: Stopped");
            startServerButton.setDisable(false);
            stopServerButton.setDisable(true);
        }
    }
}