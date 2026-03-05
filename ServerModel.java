package com.chatui.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.function.Consumer;

public class ServerModel {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private final Map<SocketChannel, String> clients = new HashMap<>();
    private final Set<SocketChannel> pendingName = new HashSet<>();
    private volatile boolean running = false;

    private Consumer<String> logListener;
    private Consumer<List<String>> userUpdateListener;

    public void setOnLog(Consumer<String> listener) { this.logListener = listener; }
    public void setOnUserUpdate(Consumer<List<String>> listener) { this.userUpdateListener = listener; }

    public void startServer(int port) throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        running = true;

        notifyLog("Server started on port " + port);

        while (running) {
            if (selector.select(500) == 0) continue;

            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (!key.isValid()) continue;
                if (key.isAcceptable()) handleAccept();
                else if (key.isReadable()) handleRead(key);
            }
        }

        for (SocketChannel ch : clients.keySet()) {
            try { ch.close(); } catch (IOException ignored) {}
        }
        clients.clear();
        pendingName.clear();
        serverChannel.close();
        selector.close();
        notifyLog("Server stopped.");
        notifyUserUpdate();
    }

    public void stopServer() {
        running = false;
        if (selector != null && selector.isOpen()) selector.wakeup();
    }

    private void handleAccept() throws IOException {
        SocketChannel client = serverChannel.accept();
        if (client == null) return;
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        pendingName.add(client);
        notifyLog("New connection from: " + client.getRemoteAddress());
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead;

        try {
            bytesRead = client.read(buffer);
        } catch (IOException e) {
            disconnectClient(client, key, "lost connection");
            return;
        }

        if (bytesRead == -1) {
            disconnectClient(client, key, "disconnected");
            return;
        }

        String msg = new String(buffer.array(), 0, bytesRead).trim();
        if (msg.isEmpty()) return;

        if (pendingName.remove(client)) {
            String username = msg.isEmpty() ? "Anonymous" : msg;
            clients.put(client, username);
            notifyLog(username + " joined.");
            broadcast(username + " has joined the server!", client);
            notifyUserUpdate();
            return;
        }

        String username = clients.get(client);

        if (msg.equalsIgnoreCase("bye") || msg.equalsIgnoreCase("end")) {
            broadcast(username + " has left the server.", client);
            disconnectClient(client, key, "left");
            return;
        }

        if (msg.equalsIgnoreCase("allUsers")) {
            String list = "System: Online users: " + String.join(", ", clients.values());
            try { client.write(ByteBuffer.wrap(list.getBytes())); } catch (IOException ignored) {}
            return;
        }

        broadcast(username + ": " + msg, null);
    }

    private void disconnectClient(SocketChannel client, SelectionKey key, String reason) {
        String name = clients.getOrDefault(client, "Unknown");
        pendingName.remove(client);
        clients.remove(client);
        key.cancel();
        try { client.close(); } catch (IOException ignored) {}
        notifyLog(name + " " + reason + ".");
        notifyUserUpdate();
    }


    private void broadcast(String msg, SocketChannel exclude) {
        byte[] data = msg.getBytes();
        for (SocketChannel target : new ArrayList<>(clients.keySet())) {
            if (target == exclude) continue;
            try {
                target.write(ByteBuffer.wrap(data));
            } catch (IOException e) {
                notifyLog(clients.getOrDefault(target, "Unknown") + " lost connection during broadcast.");
                clients.remove(target);
                pendingName.remove(target);
                try { target.close(); } catch (IOException ignored) {}
                notifyUserUpdate();
            }
        }
        notifyLog("Broadcast: " + msg);
    }

    private void notifyLog(String msg) {
        if (logListener != null) logListener.accept(msg);
    }

    private void notifyUserUpdate() {
        if (userUpdateListener != null) userUpdateListener.accept(new ArrayList<>(clients.values()));
    }
}