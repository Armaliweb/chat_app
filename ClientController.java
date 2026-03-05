package com.chatui.Client;

import javafx.application.Platform;
import java.io.IOException;

public class ClientController {
    private ClientModel model;
    private ClientView view;

    public ClientController(ClientModel model, ClientView view, String host, int port) {
        this.model = model;
        this.view = view;

        view.connectButton.setOnAction(e -> {
            try {
                String user = view.usernameField.getText();
                model.connect(host, port, user);
                view.setOnline(true);
                startListening();
            } catch (IOException ex) {
                view.appendChat("Error: Could not connect to server.");
            }
        });

        view.sendButton.setOnAction(e -> sendMessage());
    }

    private void sendMessage() {
        String text = view.messageInput.getText();
        if (!text.isEmpty()) {
            try {
                model.sendMessage(text);
                view.clearInput();
                if (text.equalsIgnoreCase("end") || text.equalsIgnoreCase("bye")) {
                    System.exit(0);
                }
            } catch (IOException e) {
                view.appendChat("Error: Message failed to send.");
            }
        }
    }

    private void startListening() {
        Thread listener = new Thread(() -> {
            try {
                while (true) {
                    String msg = model.receiveMessage();
                    if (msg != null) {
                        Platform.runLater(() -> view.appendChat(msg));
                    }
                    Thread.sleep(50);
                }
            } catch (Exception e) {
                Platform.runLater(() -> view.setOnline(false));
            }
        });
        listener.setDaemon(true);
        listener.start();
    }
}