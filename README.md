# chatApp – Class Project

A multi-client chat application developed using JavaFX for the user interface and Java NIO for the networking layer.  
The system supports multiple simultaneous clients, provides a modern chat interface, and follows the MVC architecture for clear separation of concerns.

---

## Overview

Server (Java NIO)
- Implements a non-blocking NIO Selector and SocketChannel loop.
- Accepts multiple client connections.
- Broadcasts messages to all connected clients.
- Tracks active users.
- Provides a JavaFX interface for server monitoring.

Client (JavaFX)
- Offers a modern chat interface.
- Connects to the server through a TCP socket.
- Sends and receives messages in real time.
- Supports built-in chat commands.
- Organized using the MVC pattern.

---

## Project Structure

src/
└── main/
    ├── java/
    │   └── com/chatui/
    │       ├── Server/
    │       │   ├── ServerApp.java          // JavaFX entry point for the server
    │       │   ├── ServerController.java   // Connects view and model
    │       │   ├── ServerModel.java        // NIO selector loop and client management
    │       │   └── ServerView.java         // Server UI
    │       └── Client/
    │           ├── ClientApp.java          // JavaFX entry point for the client
    │           ├── ClientController.java   // Handles UI actions
    │           ├── ClientModel.java        // TCP client networking logic
    │           └── ClientView.java         // Chat UI layout and styling
    │
    └── resources/
        ├── server.css
        └── client.css

---

## How to Run

Both applications are executed using Maven.  
Start the server before launching any client.

Start the server:

mvn -Pserver javafx:run

Start a client:

mvn -Pclient javafx:run

Multiple clients may be launched in separate terminals.

---

## Usage

1. Start the server and click Start.
2. Launch one or more client applications.
3. Enter a username and connect to the server.
4. Enter messages and send them through the chat interface.

### Built-in Commands

| Command    | Description                         |
|------------|-------------------------------------|
| allUsers   | Lists all currently connected users |
| bye        | Disconnects gracefully              |
| end        | Closes the client application       |

---

## Demo

A demonstration video is included as demo.mp4.

---

## Technologies Used

- Java 17 or later  
- JavaFX  
- Java NIO  
- Maven  
- MVC architectural pattern

---

## Notes

This project was created as part of an academic class assignment.
