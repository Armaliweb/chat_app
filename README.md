src/main/java/com/chatui/
├── Server/
│   ├── ServerApp.java          # JavaFX entry point
│   ├── ServerController.java   # Wires UI events to model
│   ├── ServerModel.java        # NIO selector loop, client management
│   └── ServerView.java         # Server monitor UI
└── Client/
    ├── ClientApp.java          # JavaFX entry point
    ├── ClientController.java   # Wires UI events to model
    ├── ClientModel.java        # Socket connection, send/receive
    └── ClientView.java         # Discord-style chat UI

src/main/resources/
├── server.css
└── client.css
