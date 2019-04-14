package com.duni.teamproject.network.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.duni.teamproject.network.client.ClientReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientService extends Service {

    private final static String TAG = "ClientService";
    private final IBinder myBinder = new MyLocalBinder();

    private String serverIP = "";
    private int serverPort = 0;

    private static Socket serverSocket;

    public ClientService() {
    }

    @Override
    public void onCreate() {
        // this is called first...
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Intent launchIntent = intent;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                serverIP = launchIntent.getStringExtra("serverIP");
                serverPort = launchIntent.getIntExtra("serverPort", -1);

                if (serverPort == -1) {
                    Log.e(TAG, "Error when retrieving server port from intent.");
                    //this.onDestroy();
                    // find a way to safely end a service?
                }
                else
                {
                    try {
                        serverSocket = new Socket(serverIP, serverPort);

                    } catch (IOException e) {
                        Log.e(TAG, "Error: " + e.toString());
                    }
                }

                // start the clientreader and clientwriter...
                //Thread clientReaderThread = new Thread(new ClientReader(serverSocket));
                //clientReaderThread.run();

                try {
                    startClient();
                } catch (IOException e) {
                    Log.e(TAG, "Error starting startClient() : " + e.toString());
                }

            }
        });
        thread.start();

        return Service.START_REDELIVER_INTENT;
    }

    private void startClient() throws IOException {
        PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(serverSocket.getInputStream()));
        String fromServer, fromClient;

        while ((fromServer = in.readLine()) != null) {
            Log.d(TAG, "From Server: " + fromServer);
            if (fromServer.equals("quit")) {
                Log.e(TAG, "Server closed connection");
                break;
            }

            // TODO: Create class to handle output to server based on server message...
            fromClient = "";

            if (fromClient != null && fromClient != "") {
                Log.d(TAG, "Client: " + fromClient);
                out.println(fromClient);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "SERVICE STOPPING");

        try {
            serverSocket.close();
        } catch(IOException e) {
            Log.e(TAG, "Error: " + e.toString());
        }
    }

    public class MyLocalBinder extends Binder {
        public ClientService getService() {
            return ClientService.this;
        }
    }
}
