package com.chatui.Server;

import javafx.application.Platform;

public class ServerController {

  private ServerModel model;
  private ServerView view;

  public ServerController(ServerModel model, ServerView view) {
    this.model = model;
    this.view = view;

    setupModelListeners();

    view.startServerButton.setOnAction(e -> {
      view.setServerRunning(true);
      view.appendLog("Server starting...");

      new Thread(() -> {
        try {
          model.startServer(4422);
        } catch (Exception ex) {
          Platform.runLater(() ->
            view.appendLog("Server Error: " + ex.getMessage())
          );
        }
        Platform.runLater(() -> view.setServerRunning(false));
      })
        .start();
    });

    view.stopServerButton.setOnAction(e -> {
      view.appendLog("Stopping server...");
      model.stopServer();
    });
  }

  private void setupModelListeners() {
    model.setOnLog(msg -> Platform.runLater(() -> view.appendLog(msg)));

    model.setOnUserUpdate(users ->
      Platform.runLater(() -> {
        view.connectedUsersListView.getItems().clear();
        view.connectedUsersListView.getItems().addAll(users);
      })
    );
  }
}
