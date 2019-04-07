package com.duni.teamproject.network.client;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AsyncTask<Void, Void, Void> {

    final static String TAG = "Client";

    String destAddress;
    int destPort;
    String response = "";

    public Client(String addr, int port) {
        destAddress = addr;
        destPort = port;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        Socket socket = null;

        try {
            socket = new Socket(destAddress, destPort);

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
        // handle response...

        Log.d(TAG, "onPostExecute(): response = " + response);

        super.onPostExecute(result);
    }
}
