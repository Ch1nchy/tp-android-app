package com.duni.teamproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Session {

    private SharedPreferences prefs;

    public static final String SERVER_STATE = "serverState";
    public static final String SERVER_DATA = "serverData";

    public Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
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

    public boolean getServerState() {
        return prefs.getBoolean(SERVER_STATE, false);
    }

    public HashMap<String, String> getServerData() {
        Gson gson = new Gson();
        String json = prefs.getString(SERVER_DATA, null);
        HashMap<String, String> map = new HashMap<String, String>();
        return gson.fromJson(json, map.getClass());
    }
}
