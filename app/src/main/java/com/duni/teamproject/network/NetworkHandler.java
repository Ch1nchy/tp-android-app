package com.duni.teamproject.network;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkHandler {

    private static final String TAG = "NetworkHandler";

    private Socket securiPi;

    private InetAddress localhost;

    public NetworkHandler() throws Exception {
        // Determine device's current IP address:
        localhost = InetAddress.getLocalHost();
        Log.d(TAG, "Device's IP Address: " + localhost.toString());

        // Determine network's netmask:
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localhost);
        int length = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();

        // Convert net mask to a address:
        InetAddress netMask = NetworkUtils.createNetMask(length);

        // NOT VERY OPTIMIZED FOR LARGE NETWORKS, REALLY NEEDS LOOKING INTO...
        ArrayList<InetAddress> networkDevices = NetworkUtils.scanNetwork(localhost, netMask);
    }

    public Socket getSocket() {
        return securiPi;
    }

    public boolean connect(InetAddress address) {
        /*
        SocketAddress localAdd = new InetSocketAddress(localhost, 20101);
        SocketAddress remoteAdd = new InetSocketAddress(address, 20101);

        try {
            securiPi.bind(localAdd);
            securiPi.connect(remoteAdd, 1000);
        } catch (Exception e) {
            Log.e(TAG, "connect(): Error creating socket: " + e.toString());
            return false;
        }
        return true;
        */

        Socket s = null;

        return true;
    }

    public void close() {
        try {
            securiPi.close();
        } catch (Exception e) {
            Log.e(TAG, "close(): Error closing socket: " + e.toString());
        }
    }
}
