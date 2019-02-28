package com.duni.teamproject.bluetooth;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.duni.teamproject.ListAdapter;
import com.duni.teamproject.R;

public class ConnectedDevices extends AppCompatActivity {

    int[] images = {R.drawable.ic_bluetooth, R.drawable.ic_bluetooth, R.drawable.ic_bluetooth};

    String[] devices = {"Bluetooth Device 1", "Bluetooth Device 2", "Bluetooth Device 3"};

    String[] descriptions = {"Bluetooth Data", "Bluetooth Data", "Bluetooth Data"};

    ListView lView;

    ListAdapter lAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.duni.teamproject.R.layout.activity_connected_devices);

        Toolbar toolbar = findViewById(R.id.toolbar_cd);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.connected_devices);

        scanForDevices();
        showDummyDevices();
    }

    private void scanForDevices() {
        // Start of scanning for Bluetooth devices...
    }

    private void showDummyDevices() {
        lView = (ListView) findViewById(R.id.device_list);

        lAdapter = new ListAdapter(ConnectedDevices.this, devices, descriptions, images);

        lView.setAdapter(lAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ConnectedDevices.this, devices[i] + " " + descriptions[i], Toast.LENGTH_SHORT).show();
            }
        });
    }
}
