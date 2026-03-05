package com.chatui.Client;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class ClientView extends HBox {

    public TextField usernameField;
    public Button connectButton;
    public TextArea chatArea;
    public TextField messageInput;
    public Button sendButton;
    public Label statusLabel;
    public Circle statusIndicatorCircle;
    public Label modeLabel;

    private Label avatarLabel;
    private Label userNameDisplay;

    public ClientView() {
        buildUI();
        applyReadOnlyModeRule();
        enableSendOnEnter();
        setOnline(false);
    }

    private void buildUI() {
        getStyleClass().add("root");
        setSpacing(0);
        HBox.setHgrow(this, Priority.ALWAYS);

        VBox rail = buildServerRail();

        VBox sidebar = buildChannelSidebar();

        VBox mainPanel = buildMainPanel();
        HBox.setHgrow(mainPanel, Priority.ALWAYS);

        getChildren().addAll(rail, sidebar, mainPanel);
    }

    private VBox buildServerRail() {
        VBox rail = new VBox(8);
        rail.getStyleClass().add("server-rail");
        rail.setAlignment(Pos.TOP_CENTER);

        Button gcIcon = new Button("GC");
        gcIcon.getStyleClass().addAll("server-icon", "server-icon-active");

        Region divider = new Region();
        divider.getStyleClass().add("rail-divider");

        Button icon2 = new Button("+");
        icon2.getStyleClass().add("server-icon");

        rail.getChildren().addAll(gcIcon, divider, icon2);
        return rail;
    }

    private VBox buildChannelSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.getStyleClass().add("channel-sidebar");

        HBox header = new HBox();
        header.getStyleClass().add("server-name-header");
        header.setAlignment(Pos.CENTER_LEFT);
        Label serverName = new Label("Group Chat Server");
        serverName.getStyleClass().add("server-name-label");
        header.getChildren().add(serverName);

        VBox channelList = new VBox(2);
        channelList.setPadding(new Insets(8, 8, 8, 8));

        Label categoryLabel = new Label("TEXT CHANNELS");
        categoryLabel.getStyleClass().add("category-label");

        HBox ch1 = makeChannelItem("general", true);


        channelList.getChildren().addAll(categoryLabel, ch1);

        VBox.setVgrow(channelList, Priority.ALWAYS);

        VBox connectSection = buildConnectSection();

        HBox userPanel = buildUserPanel();

        sidebar.getChildren().addAll(header, channelList, connectSection, userPanel);
        return sidebar;
    }

    private HBox makeChannelItem(String name, boolean active) {
        Label hash = new Label("#");
        hash.getStyleClass().add(active ? "hash-label-active" : "hash-label");
        hash.setMinWidth(20);

        Label lbl = new Label(name);
        lbl.getStyleClass().add(active ? "channel-label-active" : "channel-label");

        HBox row = new HBox(6, hash, lbl);
        row.getStyleClass().add(active ? "channel-item-active" : "channel-item");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 8, 6, 8));
        return row;
    }

    private VBox buildConnectSection() {
        VBox section = new VBox(8);
        section.getStyleClass().add("connect-section");

        Label lbl = new Label("Username");
        lbl.setStyle(
            "-fx-text-fill: #949ba4;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 700;"
        );

        usernameField = new TextField();
        usernameField.setPromptText("Enter username…");
        usernameField.setPrefWidth(Double.MAX_VALUE);

        connectButton = new Button("Connect to Server");
        connectButton.getStyleClass().add("primary-button");
        connectButton.setMaxWidth(Double.MAX_VALUE);

        statusIndicatorCircle = new Circle(5, Color.web("#87898c"));
        statusLabel = new Label("Not connected");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #87898c;");
        HBox statusRow = new HBox(6, statusIndicatorCircle, statusLabel);
        statusRow.setAlignment(Pos.CENTER_LEFT);

        modeLabel = new Label("⚠  Read-only — enter a username to chat");
        modeLabel.getStyleClass().add("mode-label");
        modeLabel.setWrapText(true);
        modeLabel.setMaxWidth(Double.MAX_VALUE);

        section.getChildren().addAll(lbl, usernameField, connectButton, statusRow, modeLabel);
        return section;
    }

    private HBox buildUserPanel() {
        HBox panel = new HBox(8);
        panel.getStyleClass().add("user-panel");
        panel.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = new StackPane();
        avatar.setStyle(
            "-fx-background-color: #5865f2;" +
            "-fx-background-radius: 50%;" +
            "-fx-min-width: 32px; -fx-pref-width: 32px; -fx-max-width: 32px;" +
            "-fx-min-height: 32px; -fx-pref-height: 32px; -fx-max-height: 32px;"
        );
        avatarLabel = new Label("?");
        avatarLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: 700;");
        avatar.getChildren().add(avatarLabel);

        userNameDisplay = new Label("Not connected");
        userNameDisplay.getStyleClass().add("user-name-label");

        Label statusHint = new Label("● Online");
        statusHint.getStyleClass().add("user-status-label");

        VBox nameStack = new VBox(1, userNameDisplay, statusHint);
        nameStack.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(nameStack, Priority.ALWAYS);

        panel.getChildren().addAll(avatar, nameStack);
        return panel;
    }

    private VBox buildMainPanel() {
        VBox panel = new VBox(0);
        panel.getStyleClass().add("chat-bg");

        HBox channelHeader = buildChannelHeader();

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPromptText("No messages yet — be the first!");
        chatArea.getStyleClass().add("text-area");
        VBox.setVgrow(chatArea, Priority.ALWAYS);

        HBox composer = buildComposer();

        panel.getChildren().addAll(channelHeader, chatArea, composer);
        return panel;
    }

    private HBox buildChannelHeader() {
        HBox header = new HBox(8);
        header.getStyleClass().add("channel-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label hash = new Label("#");
        hash.setStyle(
            "-fx-text-fill: #80848e;" +
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 400;" +
            "-fx-padding: 0 2 0 0;"
        );

        Label channelName = new Label("general");
        channelName.getStyleClass().add("title");

        // Separator
        Region sep = new Region();
        sep.setStyle("-fx-background-color: #3f4147;");
        sep.setPrefWidth(1);
        sep.setPrefHeight(20);
        HBox.setMargin(sep, new Insets(0, 4, 0, 4));

        Label topic = new Label("Group TCP Chat · Use allUsers to list connected users");
        topic.setStyle("-fx-text-fill: #87898c; -fx-font-size: 14px;");

        header.getChildren().addAll(hash, channelName, sep, topic);
        return header;
    }

    private HBox buildComposer() {
        HBox wrap = new HBox(0);
        wrap.getStyleClass().add("composer-wrap");
        wrap.setAlignment(Pos.CENTER);

        HBox inner = new HBox(0);
        inner.getStyleClass().add("composer-inner");
        inner.setAlignment(Pos.CENTER);
        inner.setStyle(
            "-fx-background-color: #383a40;" +
            "-fx-background-radius: 8;"
        );
        HBox.setHgrow(inner, Priority.ALWAYS);

        Label plusIcon = new Label("⊕");
        plusIcon.setStyle(
            "-fx-text-fill: #b5bac1;" +
            "-fx-font-size: 22px;" +
            "-fx-padding: 0 4 0 12;" +
            "-fx-cursor: hand;"
        );

        messageInput = new TextField();
        messageInput.setPromptText("Message #general");
        messageInput.getStyleClass().add("input");
        messageInput.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 0;" +
            "-fx-border-width: 0;" +
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #dbdee1;" +
            "-fx-prompt-text-fill: #87898c;" +
            "-fx-padding: 11 8 11 4;"
        );
        HBox.setHgrow(messageInput, Priority.ALWAYS);

        

        sendButton = new Button("➤");
        sendButton.getStyleClass().add("send-button");

        inner.getChildren().addAll(plusIcon, messageInput, sendButton);
        wrap.getChildren().add(inner);
        return wrap;
    }


    private void applyReadOnlyModeRule() {
        var usernameEmpty = Bindings.createBooleanBinding(
            () -> usernameField.getText() == null || usernameField.getText().trim().isEmpty(),
            usernameField.textProperty()
        );
        messageInput.disableProperty().bind(usernameEmpty);
        sendButton.disableProperty().bind(usernameEmpty);
        modeLabel.visibleProperty().bind(usernameEmpty);
        modeLabel.managedProperty().bind(usernameEmpty);
    }

    private void enableSendOnEnter() {
        messageInput.setOnAction(e -> sendButton.fire());
    }

    public void setOnline(boolean online) {
        if (online) {
            statusIndicatorCircle.setFill(Color.web("#23a55a"));
            statusLabel.setText("Connected");
            statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #23a55a;");

            // Update avatar + name from username field
            String uname = usernameField.getText().trim();
            if (!uname.isEmpty()) {
                avatarLabel.setText(String.valueOf(uname.charAt(0)).toUpperCase());
                userNameDisplay.setText(uname);
            }
        } else {
            statusIndicatorCircle.setFill(Color.web("#87898c"));
            statusLabel.setText("Not connected");
            statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #87898c;");
            userNameDisplay.setText("Not connected");
            avatarLabel.setText("?");
        }
    }

    public void appendChat(String line) {
        chatArea.appendText(line + "\n");
    }

    public void clearInput() {
        messageInput.clear();
    }
}