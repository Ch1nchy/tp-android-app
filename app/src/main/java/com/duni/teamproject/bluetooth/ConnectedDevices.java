package com.duni.teamproject.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import com.duni.teamproject.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectedDevices extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final static String TAG = ConnectedDevices.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;

    private HashMap<String, BTLE_Device> mBTDeviceHashMap;
    private ArrayList<BTLE_Device> mBTDevicesArrayList;
    private BTLE_ListAdapter adapter;

    private Button btn_scan;

    private BTStateManager mBTStateManager;
    private Scanner_BTLE mBTLEScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.duni.teamproject.R.layout.activity_connected_devices);

        Toolbar toolbar = findViewById(R.id.toolbar_cd);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.connected_devices);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.toast(getApplicationContext(), "BLE not supported");
            finish();
        }

        mBTStateManager = new BTStateManager(getApplicationContext());
        mBTLEScanner = new Scanner_BTLE(this, 7500, -75);

        mBTDeviceHashMap = new HashMap<>();
        mBTDevicesArrayList = new ArrayList<>();

        adapter = new BTLE_ListAdapter(this, R.layout.bluetooth_device_format, mBTDevicesArrayList);

        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);;
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);

        btn_scan = (Button) findViewById(R.id.btn_scan);
        findViewById(R.id.btn_scan).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(mBTStateManager, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(mBTStateManager);
        stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {
                Utils.toast(getApplicationContext(), "Please enable Bluetooth");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // This is called when an item in the listview is clicked
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                Utils.toast(getApplicationContext(), "Scan Button Pressed");

                if (!mBTLEScanner.isScanning()) {
                    startScan();
                } else {
                    stopScan();
                }
                break;
            default:
                break;
        }
    }

    public void addDevice(BluetoothDevice device, int new_rssi) {
        String address = device.getAddress();

        if (mBTDeviceHashMap.containsKey(address)) {
            BTLE_Device btle_device = new BTLE_Device(device);
            btle_device.setRSSI(new_rssi);

            mBTDeviceHashMap.put(address, btle_device);
            mBTDevicesArrayList.add(btle_device);
        } else {
            mBTDeviceHashMap.get(address).setRSSI(new_rssi);
        }

        adapter.notifyDataSetChanged();
    }

    public void startScan() {
        btn_scan.setText("Scanning...");

        mBTDevicesArrayList.clear();
        mBTDeviceHashMap.clear();

        adapter.notifyDataSetChanged();

        mBTLEScanner.start();
    }

    public void stopScan() {
        btn_scan.setText("Scan Again");

        mBTLEScanner.stop();
    }


}