package com.duni.teamproject.bluetooth;

import java.util.HashMap;

public class GattAttributes {
    private static HashMap<String, String> attributes = new HashMap<>();
    public static String HEART_RATE_MEASUREMENT =               "00002a37-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG =         "00002902-0000-1000-8000-00805f9b34fb";
    public static String HEART_RATE_SERVICE =                   "0000180d-0000-1000-8000-00805f9b34fb";
    public static String DEVICE_INFORMATION_SERVICE =           "0000180a-0000-1000-8000-00805f9b34fb";
    public static String MANUFACTURER_NAME_STRING =             "00002a29-0000-1000-8000-00805f9b34fb";

    // Two custom UUIDs:
    public static String SWITCH_DATA =                          "F64A5671-345C-BC21-DB31-33DCFF312111";
    public static String SWITCH_TOGGLE =                        "F64A5671-345C-BC21-DB31-33DCFF312112";

    static {
        // Sample Services
        attributes.put(HEART_RATE_SERVICE, "Heart Rate Service");
        attributes.put(DEVICE_INFORMATION_SERVICE, "Device Information Service");
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        attributes.put(MANUFACTURER_NAME_STRING, "Manufacturer Name String");
        attributes.put(CLIENT_CHARACTERISTIC_CONFIG, "Client Characteristic Config");
        attributes.put(SWITCH_DATA, "SecuriPi Switch Data");
        attributes.put(SWITCH_TOGGLE, "SecuriPi Switch Toggle");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
