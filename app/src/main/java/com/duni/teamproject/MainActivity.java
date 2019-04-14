package com.duni.teamproject;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.duni.teamproject.network.FindDevices;
import com.duni.teamproject.network.client.Client;
import com.duni.teamproject.network.services.ClientService;
import com.duni.teamproject.session.Session;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SecuriPiFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;

    private FindDevices findDevices;
    private Session session;
    private Client client;

    private ClientService clientService;
    private boolean shouldUnbindService;

    private boolean SERVER_STATUS;
    private HashMap<String, String> SERVER_DATA;
    private String SERVER_ADDRESS;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_ACCESS_WIFI_STATE = 2;
    private static final int PERMISSION_REQUEST_CHANGE_WIFI_STATE = 3;
    private static final int PERMISSION_REQUEST_INTERNET = 4;

    private static final int REQUEST_CODE_LAUNCH_FD = 101;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            clientService = ((ClientService.MyLocalBinder)service).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName className) {
            clientService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        findDevices = new FindDevices();

        session = new Session(getBaseContext());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.captured_images:
                                Intent openCapIma = new Intent(MainActivity.this, CapturedImages.class);
                                startActivity(openCapIma);
                                break;
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                }
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request Location Permissions
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Location Access is needed to scan for devices on a network");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
            if (this.checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                // Request Access Wifi State Permissions
                requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE}, PERMISSION_REQUEST_ACCESS_WIFI_STATE);
            }
            if (this.checkSelfPermission(Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                // Request Change Wifi State Permissions
                requestPermissions(new String[]{Manifest.permission.CHANGE_WIFI_STATE}, PERMISSION_REQUEST_CHANGE_WIFI_STATE);
            }
            if (this.checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                // Request Internet Permissions
                requestPermissions(new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_INTERNET);
            }
        }

        /*
        if (validSession) {
            // Retrieve data from Session manager...
            SERVER_STATUS = session.getServerState();
            SERVER_DATA = session.getServerData();
        } else {
            session.clear();
        }*/

        //SERVER_ADDRESS = session.getServerAddress();
        /*
        if (SERVER_ADDRESS != "") {
            // validate the session server address, make sure it is still online
            SERVER_STATUS = session.getServerState();
            SERVER_DATA = session.getServerData();
        } else {
            session.clear();
            Log.e(TAG, "onCreate(): Session Server Address returned null.");
        }
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");

        // If FindDevices has sent the data to the MainActivity...
        if (SERVER_DATA != null) {
            loadFragment(SecuriPiFragment.newInstance(SERVER_STATUS, SERVER_DATA));
            Log.d(TAG, "Creating SecuriPiFragment");
        } else {
            Log.e(TAG, "Could not load Fragment, SERVER_DATA = null");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Coarse Location permission has been granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality Limited");
                    builder.setMessage("Location Access has not been granted");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    builder.show();
                }
                return;
            }
            case PERMISSION_REQUEST_ACCESS_WIFI_STATE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Access Wifi State has been granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality Limited");
                    builder.setMessage("You will not be able to create Peer to Peer connections to SecuriPi devices.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    builder.show();
                }
                return;
            }
            case PERMISSION_REQUEST_CHANGE_WIFI_STATE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Change Wifi State permission has been granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality Limited");
                    builder.setMessage("You will not be able to create Peer to Peer connections to SecuriPi devices.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    builder.show();
                }
                return;
            }
            case PERMISSION_REQUEST_INTERNET: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Internet permission has been granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality Limited");
                    builder.setMessage("You will not be able to create Peer to Peer connections to SecuriPi devices.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unbind service when activity is destroyed
        doUnbindService();
        //session.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.launch_find_devices:
                // launch find devices:
                Intent intent = new Intent(MainActivity.this, FindDevices.class);
                startActivityForResult(intent, REQUEST_CODE_LAUNCH_FD);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE_LAUNCH_FD):
                if (resultCode == Activity.RESULT_OK) {
                    // Extract data and launch client...
                    String serverAddress = data.getStringExtra("server_address");
                    int serverPort = data.getIntExtra("server_port", -1);
                    Log.d(TAG, "server address : " + serverAddress + "\nserver port : " + serverPort);

                    // start the service...
                    Intent serviceIntent = new Intent(this, ClientService.class);
                    serviceIntent.putExtra("serverIP", serverAddress);
                    serviceIntent.putExtra("serverPort", serverPort);
                    startService(serviceIntent);
                    doBindServuce();

                    // launch the client...
                    /*
                    client = new Client(serverAddress, serverPort);

                    // retrieve variables from client...
                    client.setCompleteListener(new Client.onCompleteListener() {
                        @Override
                        public void onComplete(boolean status, HashMap<String, String> serverData, String addr) {
                            SERVER_STATUS = status;
                            SERVER_DATA = serverData;
                        }
                    });
                    client.execute();
                    */
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.d(TAG, "resultCode == RESULT_CANCELED");
                }
                break;
        }
    }

    private void doBindServuce() {
        if (bindService(new Intent(MainActivity.this, ClientService.class),
                mConnection, Context.BIND_AUTO_CREATE)) {
            shouldUnbindService = true;
        } else {
            Log.e(TAG, "Error: the requested service doesn't exist, or this client isn't" +
                    "allowed to access it.");
        }
    }

    private void doUnbindService() {
        if (shouldUnbindService) {
            unbindService(mConnection);
            shouldUnbindService = false;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.commit();
    }
}
