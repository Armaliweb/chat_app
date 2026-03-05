chatApp - class project
A JavaFX chat application built on Java NIO. Supports multiple simultaneous clients with a Discord-style UI.

Server — NIO selector-based TCP server that manages connections and broadcasts messages
Client — JavaFX UI that connects to the server and sends/receives messages in real time
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
Running
Both apps are run via Maven. Start the server first, then one or more clients.

Start the server:

mvn -Pserver javafx:run
Start a client

mvn -Pclient javafx:run
Usage
Launch the server and click Start
Launch one or more clients, enter a username, and click Connect to Server
Type in the message box and press Enter or click the send button
Built-in commands:

Command	Description
allUsers	Lists all currently connected users
bye	Disconnects gracefully
end	Closes the client application
Demo
Uploading demo.mp4…
