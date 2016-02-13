package com.example.arono.missfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by arono on 13/02/2016.
 */
public class ImageAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList arrayList;
    public ImageAdapter(Context c,ArrayList arrayList){
        this.context = c;
        inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
    }
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = (RelativeLayout)inflater.inflate(R.layout.single_row,viewGroup,false);
        TextView tvName = (TextView)row.findViewById(R.id.itemName);
        TextView tvPrice = (TextView)row.findViewById(R.id.itemPrice);
        ImageView imageView = (ImageView)row.findViewById(R.id.imageView);
        tvName.setText("zion");
        tvName.setTextSize(32f);
        tvPrice.setText("1000");
        imageView.setImageResource(R.drawable.cry1);
        return row;
    }
}
