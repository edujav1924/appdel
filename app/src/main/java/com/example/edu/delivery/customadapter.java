package com.example.edu.delivery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class customadapter extends BaseAdapter {
    Context context;
    String[] countryNames;
    String[] subs;
    LayoutInflater inflter;

    public customadapter(Context applicationContext, String[] countryNames, String subs[]) {
        this.context = applicationContext;
        this.countryNames = countryNames;
        this.subs = subs;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return countryNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView names = view.findViewById(R.id.title);
        TextView subtitlulo = view.findViewById(R.id.sub);
        names.setText(countryNames[i]);
        subtitlulo.setText(subs[i]);
        return view;
    }
}
