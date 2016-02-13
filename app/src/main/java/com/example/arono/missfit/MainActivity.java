package com.example.arono.missfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final float flHeight = 0.78125f;
    LinearLayout rlMain;
    FrameLayout frameLayout;
    GridView gvItems;
    DisplayMetrics displayMetrics;
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightPixels = displayMetrics.heightPixels;
        int flDimension = (int) (heightPixels * flHeight);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("stam");
        arrayList.add("stam");
        arrayList.add("stam");
        arrayList.add("stam");
        arrayList.add("stam");
        arrayList.add("stam");
        arrayList.add("stam");
        arrayList.add("stam");
        arrayList.add("stam");
        arrayList.add("stam");
        arrayList.add("stam");
        arrayList.add("stam");
        imageAdapter = new ImageAdapter(this,arrayList);

        rlMain = (LinearLayout)findViewById(R.id.rlMain);
        frameLayout = new FrameLayout(this);
        gvItems = new GridView(this);
        FrameLayout.LayoutParams flLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,flDimension);
        gvItems.setNumColumns(2);
        gvItems.setAdapter(imageAdapter);
        frameLayout.addView(gvItems);
        Button btn = new Button(this);
        FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,heightPixels-flDimension);
        rlMain.addView(btn,fl);
        rlMain.addView(frameLayout, flLayoutParams);
    }


}
