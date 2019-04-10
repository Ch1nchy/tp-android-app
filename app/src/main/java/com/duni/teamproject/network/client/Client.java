package com.duni.teamproject.network.client;

import android.os.AsyncTask;
import android.util.Log;

import com.duni.teamproject.network.ServerResponseHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client extends AsyncTask<Void, Void, Void> {

    public interface onCompleteListener {
        public void onComplete(boolean status, HashMap<String, String> serverData);
    }

    public void setCompleteListener(onCompleteListener listener) {
        this.listener = listener;
    }

    onCompleteListener listener;

    final static String TAG = "Client";

    String destAddress;
    int destPort;
    String response = "";
    Socket serverSocker;

    private static boolean status;
    private static HashMap<String, String> serverData;

    ServerResponseHandler responseHandler;

    public Client(String addr, int port) {
        destAddress = addr;
        destPort = port;
        status = false;
    }

    public boolean getCurrentStatus() {
        return status;
    }

    public void setCurrentStatus(boolean status) {
        this.status = status;
    }

    public HashMap<String, String> getServerData() {
        return serverData;
    }

    public void setServerData(HashMap<String, String> serverData) {
        this.serverData = serverData;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        Socket socket = null;

        try {
            socket = new Socket(destAddress, destPort);
            serverSocker = socket;
            responseHandler = new ServerResponseHandler(serverSocker, this);

            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream( 1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }

        } catch (UnknownHostException e) {
            Log.e(TAG, "doInBackground(): UnknownHostException catch: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground(): IOException catch: " + e.toString());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground(): " + e.toString());
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // handle response, this is where the response can be used to
        // update the screen...
        Log.d(TAG, "onPostExecute(): response = " + response);

        // handle the response...
        responseHandler.handleResponse(response);
        listener.onComplete(status, serverData);

        super.onPostExecute(result);
    }
}
