package com.chatui.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientModel {
    private SocketChannel socketChannel;
    private boolean readOnlyMode = false;

    public void connect(String host, int port, String username) throws IOException {
        if (username == null || username.trim().isEmpty()) {
            this.readOnlyMode = true;
        }

        socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
        socketChannel.configureBlocking(false);
        
        String nameToSend = readOnlyMode ? "Anonymous" : username;
        socketChannel.write(ByteBuffer.wrap(nameToSend.getBytes()));
    }

    public void sendMessage(String msg) throws IOException {
        if (readOnlyMode) return;
        
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        socketChannel.write(buffer);
    }

    public String receiveMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = socketChannel.read(buffer);
        if (bytesRead > 0) {
            return new String(buffer.array(), 0, bytesRead).trim();
        }
        return null;
    }

    public boolean isReadOnly() {
        return readOnlyMode;
    }
}