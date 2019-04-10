package com.example.hejiayuan.vocabulary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class DictSentenceListAdapter extends BaseAdapter {
    private Context context = null;
    private int resources;
    private ArrayList<HashMap<String, Object>> list = null;
    private String [] from;
    private int [] to;

    public DictSentenceListAdapter(Context context, int resources, ArrayList<HashMap<String, Object>> list, String[] from, int[] to) {
        super();
        this.context = context;
        this.resources = resources;
        this.list = list;
        this.from = from;
        this.to = to;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resources, null);
        TextView text = (TextView) convertView.findViewById(to[0]);
        text.setText((String)(list.get(position).get(from[0])));
        return convertView;
    }
}
