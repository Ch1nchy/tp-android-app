package com.duni.teamproject.network.client;

import java.net.Socket;

public class ClientWriter {
    private Socket serverSocket;

    public ClientWriter(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }


}
