package com.example.arono.missfit.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.example.arono.missfit.DataServerManagement.DataManager;
import com.example.arono.missfit.Drawer.BaseActivityWithNavigationDrawer;
import com.example.arono.missfit.ImageAdapter;
import com.example.arono.missfit.Item;
import com.example.arono.missfit.R;

import java.util.ArrayList;

public class MyItemsActivity extends BaseActivityWithNavigationDrawer {

    FrameLayout contentFrame;
    LayoutInflater inflater;
    ImageAdapter imageAdapter;
    RelativeLayout rl;
    DataManager dataManager = new DataManager();
    GridView gvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentFrame = getContentFrame();
        inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_myitems,null,false);

        final View popUp = inflater.inflate(R.layout.pop_up_progress_bar,null,false);
        final ProgressBar progressBar = (ProgressBar) popUp.findViewById(R.id.popUpProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        contentFrame.addView(view);
        contentFrame.addView(popUp);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                contentFrame.removeView(popUp);
            }

        }, 2000L);

        BackendlessUser user = Backendless.UserService.CurrentUser();

        new LoadItemsFromSpecificUserAsyncTask(dataManager,this,user).execute();

        rl = (RelativeLayout)this.findViewById(R.id.rlMyItems);
        Intent titleIntent = getIntent();
        String title =  titleIntent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

        imageAdapter = new ImageAdapter(getApplicationContext());
        gvItems = new GridView(this);
        gvItems.setNumColumns(2);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
class LoadItemsFromSpecificUserAsyncTask extends AsyncTask {

    private DataManager dataManager;
    private Context c;
    private boolean flag = false;
    private BackendlessUser user;
    private ArrayList<Item> itemArrayList;

    public LoadItemsFromSpecificUserAsyncTask(DataManager dataManager, Context c, BackendlessUser user){
        this.c = c;
        this.dataManager = dataManager;
        this.user = user;
    }
    @Override
    protected Object doInBackground(Object... objects) {
        dataManager.getSpecificUserItems(user, new DataManager.GetAllCallback() {
            @Override

            public void done(ArrayList<Item> value, BackendlessException e) {
                if (e != null) {
                    Log.e("LoadItemsFromSpecificUserAsyncTask", e.toString());
                }
                itemArrayList = value;
                flag = true;
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(flag) {
            imageAdapter.setItems(itemArrayList);
            gvItems.setAdapter(imageAdapter);
            rl.addView(gvItems);
        }


    }
}

}
