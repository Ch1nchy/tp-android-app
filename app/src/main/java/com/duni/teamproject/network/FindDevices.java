package com.duni.teamproject.network;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.duni.teamproject.R;
import com.duni.teamproject.network.client.Client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class FindDevices extends AppCompatActivity {

    final static String TAG = "FindDevices";

    LinearLayout linearLayout;
    PopupWindow connectPopup;
    View popupView;

    //NetworkHandler nh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_devices);

        Toolbar toolbar = findViewById(R.id.toolbar_fd);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.find_devices);

        // Popup Window stuff...
        linearLayout = (LinearLayout) findViewById(R.id.ll_find_devices);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add items to the action bar...
        getMenuInflater().inflate(R.menu.find_devices_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.scan_network:
                scanNetwork();
                break;
            case R.id.connect_to:
                openPopupConnect();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void scanNetwork() {
        // Start scanning the network...
    }

    private void openPopupConnect() {
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().
                getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.connect_popup, null);

        connectPopup = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btnClose = (Button) popupView.findViewById(R.id.float_button);
        Button btnConnect = (Button) popupView.findViewById(R.id.connect);

        final EditText txtIpAddress = (EditText) popupView.findViewById(R.id.con_ip_address);
        final EditText txtPort = (EditText) popupView.findViewById(R.id.con_port);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectPopup.dismiss();
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close connection (if there is any):
                /*
                if (nh != null) {
                    nh.close();
                }
                */

                // Get text from the EditText forms:
                String ipAddress = txtIpAddress.getText().toString();
                String port = txtPort.getText().toString();

                boolean valid = NetworkUtils.verifyAddress(ipAddress, port);

                // Validate the IP Address supplied by user:
                if (valid) {
                    // Connect and then close popup...
                    try {
                        InetAddress add = InetAddress.getByAddress(NetworkUtils.convertAddressToByteArray(ipAddress));
                        // Create client object and attempt to connect to the device...
                        Client c = new Client(add.getHostAddress(), 20101);
                        c.execute();

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }

                    connectPopup.dismiss();
                } else if (!valid) {
                    // Not a valid IP Address:
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindDevices.this);
                    builder.setMessage("The IP Address is either not valid or could not be found")
                            .setTitle("Invalid IP Address");

                    AlertDialog dialog = builder.create();
                }
            }
        });

        connectPopup.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
        connectPopup.setFocusable(true);
        connectPopup.update();
    }
}
