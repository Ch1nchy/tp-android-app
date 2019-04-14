package com.duni.teamproject.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.strictmode.ServiceConnectionLeakedViolation;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Session {

    private SharedPreferences prefs;

    private boolean reachable;

    private static final String TAG = "Session";

    public static final String SERVER_STATE = "serverState";
    public static final String SERVER_DATA = "serverData";
    public static final String SERVER_ADDRESS = "serverAddress";

    public Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clear() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(SERVER_STATE);
        editor.remove(SERVER_DATA);
        editor.remove(SERVER_ADDRESS);
        editor.commit();
    }

    public void setServerState(boolean serverState) {
        prefs.edit().putBoolean(SERVER_STATE, serverState).apply();
    }

    public void setServerData(HashMap<String, String> serverData) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(serverData);
        prefsEditor.putString(SERVER_DATA, json);
        prefsEditor.apply();
    }

    public void setServerAddress(String serverAddress) {
        prefs.edit().putString(SERVER_ADDRESS, serverAddress).apply();
    }

    public boolean getServerState() {
        return prefs.getBoolean(SERVER_STATE, false);
    }

    public HashMap<String, String> getServerData() {
        Gson gson = new Gson();
        String json = prefs.getString(SERVER_DATA, null);
        HashMap<String, String> map = new HashMap<String, String>();
        return gson.fromJson(json, map.getClass());
    }

    public String getServerAddress() {
        return prefs.getString(SERVER_ADDRESS, "");
    }
}
