package com.example.arono.missfit.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.arono.missfit.Drawer.BaseActivityWithNavigationDrawer;
import com.example.arono.missfit.R;

import java.util.ArrayList;

public class ShoppingCartActivity extends BaseActivityWithNavigationDrawer {

    LayoutInflater inflater;
    FrameLayout contentFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentFrame = getContentFrame();
        inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_shopping_cart,null,false);
        contentFrame.addView(view);

        Intent titleIntent = getIntent();
        String title =  titleIntent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

    }


}
