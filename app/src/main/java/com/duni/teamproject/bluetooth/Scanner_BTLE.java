package com.duni.teamproject.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

public class Scanner_BTLE {

    private ConnectedDevices cd;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private long scanPeriod;
    private int signalStrength;

    public Scanner_BTLE(ConnectedDevices connectedDevices, long scanPeriod, int signalStrength) {
        cd = connectedDevices;
        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;

        mHandler = new Handler();

        final BluetoothManager bluetoothManager = (BluetoothManager) cd.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    public boolean isScanning(){
        return mScanning;
    }

    public void start() {
        if (Utils.checkBluetooth(mBluetoothAdapter)) {
            Utils.requestUserBluetooth(cd);
            cd.stopScan();
        } else {
            scanLeDevice(true);
        }
    }

    public void stop() {
        scanLeDevice(false);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable && !mScanning) {
            Utils.toast(cd.getApplication(), "Starting BLE Scan...");

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.toast(cd.getApplicationContext(), "Stopping BLE Scan...");

                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    cd.stopScan();
                }
            }, scanPeriod);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    final int new_rssi = rssi;
                    if (rssi> signalStrength) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                cd.addDevice(device, new_rssi);
                            }
                        });
                    }
                }
            };
}
