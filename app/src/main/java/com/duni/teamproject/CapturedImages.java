package com.duni.teamproject;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CapturedImages extends AppCompatActivity {

    String[] listViewTitle = new String[] {
            "ListView Title 1", "ListView Title 2", "ListView Title 3"
    };

    int[] listViewImage = new int[] {
            R.drawable.one, R.drawable.two, R.drawable.three
    };

    String[] listViewDesc = new String[] {
            "ListView Description 1", "ListView Description 2", "ListView Description 3"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_images);

        Toolbar toolbar = findViewById(R.id.toolbar_ci);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.captured_images);

        displayImages();
    }

    private void displayImages() {
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 3; i++)
        {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_image", Integer.toString(listViewImage[i]));
            hm.put("listview_title", listViewTitle[i]);
            hm.put("listview_description", listViewDesc[i]);
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_description"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_desc};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_activty, from, to);
        ListView androidListView = (ListView) findViewById(R.id.listview_ci);
        androidListView.setAdapter(simpleAdapter);
    }
}
