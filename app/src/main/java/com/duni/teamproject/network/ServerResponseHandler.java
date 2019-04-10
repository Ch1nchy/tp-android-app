package com.duni.teamproject.network;

import android.util.Log;

import com.duni.teamproject.network.client.Client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ServerResponseHandler {

    final static String TAG = ServerResponseHandler.class.toString();

    Socket responseSocket;
    Client client;

    public ServerResponseHandler(Socket socket, Client client) {
        responseSocket = socket;
        this.client = client;
    }

    public void handleResponse(String response) {
        // handle responses here...
        HashMap<String, String> data = seperateElements(response);
        HashMap<String, String> serverData = new HashMap<String, String>();

        // Initial setup:
        if (data.containsKey("status")) {
            String status = data.get("status");
            boolean b = false;
            try {
                b = Boolean.parseBoolean(status);
            } catch (Exception e) {
                Log.e(TAG, "handleResponse(): Could not parse boolean");
            } finally {
                client.setCurrentStatus(b);
            }
        }
        if (data.containsKey("name")) {
            serverData.put("name", data.get("name"));
        }

        if (!(serverData.isEmpty())) {
            // send the server data to the main activity file...
            client.setServerData(serverData);
        }
    }

    private HashMap<String, String> seperateElements(String r) {
        HashMap<String, String> details = new HashMap<>();

        String s = r.substring(1, r.length() - 1);
        String[] elements = s.split(Pattern.quote(";"));

        int size = elements.length;
        for (int i = 0; i < size; i++) {
            String[] vals = elements[i].split(Pattern.quote(":"));
            details.put(vals[0], vals[1]);
        }

        return details;
    }
}
