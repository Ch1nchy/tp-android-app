package com.duni.teamproject.network;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;

public class NetworkHandler {

    private static final String TAG = "NetworkHandler";

    public NetworkHandler() throws Exception {
        // Determine device's current IP address:
        InetAddress localhost = InetAddress.getLocalHost();
        Log.d(TAG, "Device's IP Address: " + localhost.toString());

        // Determine network's netmask:
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localhost);
        int length = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();

        // Convert net mask to a address:
        InetAddress netMask = NetworkUtils.createNetMask(length);

        // NOT VERY OPTIMIZED FOR LARGE NETWORKS, REALLY NEEDS LOOKING INTO...
        ArrayList<InetAddress> networkDevices = NetworkUtils.scanNetwork(localhost, netMask);
    }

}
