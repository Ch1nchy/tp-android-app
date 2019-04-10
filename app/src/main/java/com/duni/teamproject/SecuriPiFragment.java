package com.duni.teamproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

public class SecuriPiFragment extends Fragment {
    private static final String TAG = "SecuriPiFragment";

    private static final String ARG_PARAM1 = "status";
    private static final String ARG_PARAM2 = "data";

    private boolean serverStatus;
    private HashMap<String, String> serverData;

    private OnFragmentInteractionListener mListener;

    private TextView txtName;
    private TextView txtMac;
    private TextView txtStatus;

    public SecuriPiFragment() {
        // Required empty public constructor
    }

    public static SecuriPiFragment newInstance(boolean serverStatus, HashMap<String, String> serverData) {
        SecuriPiFragment fragment = new SecuriPiFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, serverStatus);
        args.putSerializable(ARG_PARAM2, serverData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serverStatus = getArguments().getBoolean(ARG_PARAM1);
            serverData = (HashMap<String, String>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_securi_pi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        txtName = (TextView) getView().findViewById(R.id.sp_f_name);
        txtMac = (TextView) getView().findViewById(R.id.sp_f_mac);
        txtStatus = (TextView) getView().findViewById(R.id.sp_f_status);

        String nameText = txtName.getText().toString();
        String macAddText = txtMac.getText().toString();
        String statusText = txtStatus.getText().toString();

        // If arguments have been passed...
        if (getArguments() != null) {
            if (serverStatus) {
                statusText += "Online";
            } else {
                statusText += "Offline";
            }

            if (serverData.containsKey("name")) {
                nameText += serverData.get("name");
            } else {
                nameText += "Unknown";
            }
        }

        Log.d(TAG, "onViewCreated(): " + statusText + "\n" + nameText + "\n" + macAddText);

        txtName.setText(nameText);
        txtMac.setText(macAddText);
        txtStatus.setText(statusText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }
}
