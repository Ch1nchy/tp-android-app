package com.duni.teamproject.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendIdentifier {

    private static DatagramSocket socket = null;
    private static String identifyMessage =
            "<ID>prog:SecuriPi device:androidapp req:server_identify <ID>";

    public SendIdentifier(InetAddress recipientAddress) {
        try {
            send(identifyMessage, recipientAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(String data, InetAddress address) throws IOException {
        // CHANGE TO USE TCP/IP INSTEAD....
        socket = new DatagramSocket();

        byte[] buffer = data.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 4455);
        socket.send(packet);
        socket.close();
    }
}
