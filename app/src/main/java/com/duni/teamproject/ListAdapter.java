package com.duni.teamproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    Context context;
    private final String[] values;
    private final String[] description;
    private final int[] images;

    public ListAdapter(Context context, String[] values, String[] description, int[] images) {
        this.context = context;
        this.values = values;
        this.description = description;
        this.images = images;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.bluetooth_device_format, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.bluetooth_title);
            viewHolder.txtDesc = (TextView) convertView.findViewById(R.id.bluetooth_description);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.bluetooth_logo);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtName.setText(values[position]);
        viewHolder.txtDesc.setText("Device Data: " + description[position]);
        viewHolder.icon.setImageResource(images[position]);

        return result;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtDesc;
        ImageView icon;
    }

}
