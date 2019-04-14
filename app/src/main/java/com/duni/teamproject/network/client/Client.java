package com.duni.teamproject.network.client;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.duni.teamproject.network.ServerResponseHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client extends AsyncTask<Void, Void, Void> {

    public interface onCompleteListener {
        public void onComplete(boolean status, HashMap<String, String> serverData, String addr);
    }

    public void setCompleteListener(onCompleteListener listener) {
        this.listener = listener;
    }

    onCompleteListener listener;

    final static String TAG = "Client";

    final static int CLIENT_READER_MSG = 1;
    final static int CLIENT_WRITER_MSG = 2;

    String destAddress;
    int destPort;
    String response = "";

    private static Socket serverSocket;
    private static boolean status;
    private static HashMap<String, String> serverData;

    ServerResponseHandler responseHandler;

    private Handler mainThreadHandler;

    public Client(String addr, int port) {
        destAddress = addr;
        destPort = port;
        status = false;
    }

    public void setCurrentStatus(boolean status) {
        this.status = status;
    }

    public void setServerData(HashMap<String, String> serverData) {
        this.serverData = serverData;
    }

    public void sendResponse(String response) {
        // Do something else here
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "handleMessage(): Received message from worker thread");
                if (msg.what == CLIENT_READER_MSG) {
                    // handle message...
                    Log.d(TAG, "msg = " + msg.toString());
                } else if (msg.what == CLIENT_WRITER_MSG) {
                    // handle message...
                }
            }
        };

        try {
            Log.d(TAG, "doInBackground(): Creating Socket...");
            serverSocket = new Socket(destAddress, destPort);
            Log.d(TAG, "serverSocket : " + serverSocket.toString());
            responseHandler = new ServerResponseHandler(serverSocket, Client.this);

            ClientReader reader = new ClientReader(serverSocket);
            reader.run();
            ClientWriter writer = new ClientWriter(serverSocket);
            // writer.run();

            /* OLD STUFF
            responseHandler = new ServerResponseHandler(serverSocket, this);

            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream( 1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = serverSocket.getInputStream();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }

            Log.d(TAG, "doInBackground(): response = " + response);
            */

        } catch (UnknownHostException e) {
            Log.e(TAG, "doInBackground(): UnknownHostException catch: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground(): IOException catch: " + e.toString());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground(): " + e.toString());
                }
            }
        }
        return null;
    }



    @Override
    protected void onPostExecute(Void result) {
        //super.onPostExecute(result);

        // handle response, this is where the response can be used to
        // update the screen...
        //Log.d(TAG, "onPostExecute(): response = " + response);

        // handle the response...
        //responseHandler.handleResponse(response);
        //String fullAddress = destAddress + ":" + destPort;
        //Log.d(TAG, "onPostExecute(): address = " + fullAddress);
        //listener.onComplete(status, serverData, fullAddress);


    }

    /*
    private class MessageHandler extends Handler {
        public MessageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

        }
    }
    */

    /*
    private class ServerListener extends Thread {
        @Override
        public void run() {
            do {
                try {
                    serverSocket = new Socket(destAddress, destPort);
                    responseHandler = new ServerResponseHandler(serverSocket, Client.this);

                    ByteArrayOutputStream byteArrayOutputStream =
                            new ByteArrayOutputStream(1024);
                    byte[] buffer = new byte[1024];

                    int bytesRead;
                    InputStream inputStream = serverSocket.getInputStream();

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                        response += byteArrayOutputStream.toString("UTF-8");
                    }

                    responseHandler.handleResponse(response);
                    listener.onComplete(status, serverData);
                } catch (IOException e) {
                    Log.e(TAG, "ServerListener: " + e.toString());
                }
            } while (!serverSocket.isClosed());
        }
    }

    private class ServerResponse extends Thread {
        private String resp = "";

        public ServerResponse(String response) {
            resp = response;
        }

        @Override
        public void run() {
            while (!serverSocket.isClosed()) {
                try {
                    int length = resp.length();
                    int index = 0;

                    while (length >= 255) {
                        DataOutputStream dOut = new DataOutputStream(serverSocket.getOutputStream());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(255);

                        byte[] msg = new byte[255];
                        byteArrayOutputStream.write(msg, index, 255);

                        index += 255;
                        length -= 255;

                        dOut.writeByte(255);
                        dOut.write(msg);
                        dOut.flush();
                        dOut.close();
                    }
                    if (length < 255 && length != 0) {
                        DataOutputStream dOut = new DataOutputStream(serverSocket.getOutputStream());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(length);

                        byte[] msg = new byte[length];
                        byteArrayOutputStream.write(msg, index, length);

                        dOut.writeByte(length);
                        dOut.write(msg);
                        dOut.flush();
                        dOut.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "ServerResponse: " + e.toString());
                }
            }
        }
    }
    */
}
