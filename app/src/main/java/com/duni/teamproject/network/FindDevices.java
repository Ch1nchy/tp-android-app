package com.duni.teamproject.network;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.duni.teamproject.R;
import com.duni.teamproject.session.Session;
import com.duni.teamproject.network.client.Client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class FindDevices extends AppCompatActivity {

    final static String TAG = "FindDevices";

    LinearLayout linearLayout;
    PopupWindow connectPopup;
    View popupView;

    private Session session;

    private static Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_devices);

        Toolbar toolbar = findViewById(R.id.toolbar_fd);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.find_devices);

        session = new Session(getBaseContext());

        // Popup Window stuff...
        linearLayout = (LinearLayout) findViewById(R.id.ll_find_devices);

        final EditText edtIpAddress = findViewById(R.id.fd_con_ip_address);
        EditText edtPort = findViewById(R.id.fd_con_port);

        Button btnConnect = findViewById(R.id.fd_connect);
        btnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Add intent data...
                String ipAddress = edtIpAddress.getText().toString();
                int port = 20101;

                Intent resultIntent = new Intent();
                resultIntent.putExtra("server_address", ipAddress);
                resultIntent.putExtra("server_port", port);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        Button btnCancel = findViewById(R.id.fd_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add canceled data info here...
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add items to the action bar...
        //getMenuInflater().inflate(R.menu.find_devices_menu, menu);
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
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get text from the EditText forms:
                String ipAddress = txtIpAddress.getText().toString();
                String port = txtPort.getText().toString();

                boolean valid = NetworkUtils.verifyAddress(ipAddress, port);

                if (valid) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("server_address", ipAddress);
                    resultIntent.putExtra("server_port", 20101);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                /*
                // Validate the IP Address supplied by user:
                if (valid) {
                    // Connect and then close popup...
                    try {
                        final InetAddress add = InetAddress.getByAddress(NetworkUtils.convertAddressToByteArray(ipAddress));
                        // Create client object and attempt to connect to the device...
                        client = new Client(add.getHostAddress(), 20101);
                        client.setCompleteListener(new Client.onCompleteListener() {
                            public void onComplete(boolean status, HashMap<String, String> data, String address) {
                                // Add variables to Intent
                                if (address != "") {
                                    session.setServerState(status);
                                    session.setServerData(data);
                                    session.setServerAddress(address);
                                } else {
                                    // The server could not be found, clear any data that might still be there.
                                    Log.e(TAG, "setOnClickListener(): address == null, session.clear() called");
                                    session.clear();
                                }


                                /*
                                if (valid) {
                                    session.setServerState(status);
                                    session.setServerData(data);
                                    //session.setServerSocket(true);
                                } else {
                                    session.clear();
                                }
                                */ /*
                            }
                        });
                        client.execute();

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
                */
            }
        });

        connectPopup.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
        connectPopup.setFocusable(true);
        connectPopup.update();
    }
}
