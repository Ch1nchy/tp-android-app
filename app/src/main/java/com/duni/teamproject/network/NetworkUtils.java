package com.duni.teamproject.network;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    public NetworkUtils() {

    }

    public static InetAddress createNetMask(int length) {
        InetAddress netmask = null;
        int count = length;
        byte[] net  = new byte[4];

        for (int i = 0; i < 4; i++) {
            if (count >= 8) {
                net[i] = (byte)255;
                count -= 8;
            } else if (count < 8 && count != 0) {
                byte t = 0;
                do {
                    t += (byte)(Math.pow(2, 8 - count));
                    count--;
                } while (count != 0);
                net[i] = t;
                count = 0;
            }
        }

        try {
            netmask = InetAddress.getByAddress(net);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return netmask;
    }

    public static ArrayList<InetAddress> scanNetwork(InetAddress localhostAdd, InetAddress netmaskAdd) {
        ArrayList<InetAddress> ips = new ArrayList<>();

        // Conversions of string to byte array for easier bit comparisons...
        String localhostStr = localhostAdd.getHostAddress();
        byte[] localhost = convertAddressToByteArray(localhostStr);

        String netmaskStr = netmaskAdd.getHostAddress();
        byte[] netmask = convertAddressToByteArray(netmaskStr);

        // find minimum and max subnet values:
        byte[] minAddress = getMinimumAddress(localhost, netmask);
        System.out.println("Minimum Address: " + printByteAddress(minAddress));

        // Find max address....
        byte[] maxAddress = getMaximumAddress(minAddress, netmask);
        System.out.println("Maximum Address: " + printByteAddress(maxAddress));

        ArrayList<byte[]> availableIPs = getAvailableIPs(minAddress, maxAddress);

        int ipListSize = availableIPs.size();
        for (int i = 0; i < ipListSize; i++) {
            InetAddress device = convertByteArrayToAddress(availableIPs.get(i));
            try {
                if (device != null && device.isReachable(1000)); {
                    ips.add(device);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ips;
    }

    private static ArrayList<byte[]> getAvailableIPs(byte[] minAddress, byte[] maxAddress) {
        ArrayList<byte[]> availableIPs = new ArrayList<>();

        // Keeps adding 1 to the min address until it is equal to maxAddress...

        do {
            System.out.println("Adding address: " + minAddress.toString());
            availableIPs.add(minAddress);
            addOneToByteArray(minAddress);
        } while (!(Arrays.equals(minAddress, maxAddress)));

        System.out.println("Amount of available IPs: " + availableIPs.size());

        return availableIPs;
    }

    private static byte[] addOneToByteArray(byte[] address) {
        byte[] nAddress = address;

        for (int i = 3; i >= 0; i--) {
            if (!(nAddress[i] == (byte)255)) {
                nAddress[i] += (byte)1;
                break;
            } else {
                nAddress[i] = (byte)0;
            }
        }

        return nAddress;
    }

    private static byte[] takeOneFromByteArray(byte[] address) {
        byte[] nAddress = address;

        for (int i = 3; i >= 0; i--) {
            if (!(nAddress[i] == 0x00)) {
                nAddress[i] -= (byte)1;
                break;
            }
        }

        return nAddress;
    }

    private static byte[] convertAddressToByteArray(String address) {
        byte[] bAddress = new byte[4];
        String[] byteComponents = address.split(Pattern.quote("."));

        int length = byteComponents.length;

        for (int i = 0; i < length; i++) {
            int b = 0;
            try {
                b = Integer.parseInt(byteComponents[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            bAddress[i] = (byte) b;
        }
        return bAddress;
    }

    private static InetAddress convertByteArrayToAddress(byte[] address) {
        InetAddress device;

        try {
            device = InetAddress.getByAddress(address);
            return device;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] getMinimumAddress(byte[] localhost, byte[] netmask) {
        byte[] minimumAddress = new byte[4];

        for (int i = 0; i < 4; i++) {
            minimumAddress[i] = (byte)(localhost[i] & netmask[i]);
        }

        addOneToByteArray(minimumAddress);
        return minimumAddress;
    }

    private static byte[] getMaximumAddress(byte[] minAddress, byte[] netmask) {
        byte[] maximumAddress = new byte[4];

        for (int i = 0; i < 4; i++) {
            maximumAddress[i] = (byte)(minAddress[i] | ~netmask[i]);
        }

        takeOneFromByteArray(maximumAddress);
        return maximumAddress;
    }

    private static String printByteAddress(byte[] address) {
        return (address[0] & 0xFF) + "." + (address[1] & 0xFF)
                + "." + (address[2] & 0xFF) + "." + (address[3] & 0xFF);
    }
}
