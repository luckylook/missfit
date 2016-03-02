package com.example.arono.missfit.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.backendless.exceptions.BackendlessException;
import com.example.arono.missfit.Activities.FeedActivity;
import com.example.arono.missfit.DataServerManagement.DataManager;
import com.example.arono.missfit.Item;
import com.example.arono.missfit.R;

import java.util.ArrayList;

public class LoadItemsActivity extends AppCompatActivity {

    ProgressBar progressBar;
    DataManager dataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_items);

        Log.e("error","hereLoadSomeItem");
        progressBar = (ProgressBar) findViewById(R.id.progressId);
        dataManager = new DataManager();
        Stam s = new Stam(dataManager,this,progressBar);
        s.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class Stam extends AsyncTask{

        private ProgressBar progressBar;
        private DataManager dataManager;
        private Context c;
        private boolean flag = false;
        private ArrayList<Item> itemArrayList;

        public Stam(DataManager dataManager,Context c,ProgressBar progressBar){
            this.c = c;
            this.progressBar = progressBar;
            this.dataManager = dataManager;
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Object doInBackground(Object... objects) {
            dataManager.getAllObjects(new DataManager.GetAllCallback() {
                @Override

                public void done(ArrayList<Item> value, BackendlessException e) {
                    Log.e("Stam", "Stam");
                    itemArrayList = value;
                    for(int i = 0 ; i < 10 ; i++){
                        itemArrayList.add(value.get(1));
                    }
                    flag = true;
                    onPostExecute(flag);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(flag) {
                progressBar.setVisibility(View.INVISIBLE);
                Intent feedIntent = new Intent(c,FeedActivity.class);
                feedIntent.putExtra("itemArray",itemArrayList);
                startActivity(feedIntent);
                finish();
            }


        }
    }

}
