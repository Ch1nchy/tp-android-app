package com.duni.teamproject.network.client;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.PopupWindow;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReader implements Runnable {

    private static final String TAG = "ClientReader";

    private Socket serverSocket;

    public ClientReader(Socket socket) {
        serverSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Create I/O streams:
            //DataOutputStream dOut = new DataOutputStream(serverSocket.getOutputStream());
            DataInputStream dIn = new DataInputStream(serverSocket.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            String data = "";
            boolean reading = true;

            while (reading) {
                if (reader.ready()) {
                    Log.d(TAG, "About to read...");
                    data = reader.readLine();

                    Log.d(TAG, "run() : data = " + data);
                    //Log.d(TAG, "New message: " + data);
                    // if data.startsWith("string here")...
                }
            }
            Log.d(TAG, "Data was : " + data);
            //dIn.close();
        } catch (IOException e) {
            Log.e(TAG, "run() " + e.toString());
        }


    }
}
