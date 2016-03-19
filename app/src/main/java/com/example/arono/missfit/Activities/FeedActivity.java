package com.example.arono.missfit.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.backendless.exceptions.BackendlessException;
import com.example.arono.missfit.DataServerManagement.DataManager;
import com.example.arono.missfit.Drawer.BaseActivityWithNavigationDrawer;
import com.example.arono.missfit.FragmentCategory;
import com.example.arono.missfit.ImageAdapter;
import com.example.arono.missfit.Item;
import com.example.arono.missfit.R;
import com.example.arono.missfit.Slide.SlidingTabLayout;

import java.util.ArrayList;


public class FeedActivity extends BaseActivityWithNavigationDrawer implements FetchDataListener {

    public static final int SIZE = 4;
    public static final String TOPS = "TOPS";
    public static final String BOTTOMS = "BOTTOMS";
    public static final String SHOES = "SHOES";
    public static final String CUSTOM = "CUSTOM";
    ViewPager viewPager = null;
    TabAdapter tabAdapter;
    SlidingTabLayout tabs;
    SearchView searchView;
    ImageAdapter[] imageAdapter;
    FrameLayout contentFrame;
    LayoutInflater inflater;
    View popUp ;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        contentFrame = getContentFrame();
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_feed, null, false);
        contentFrame.addView(view);

        imageAdapter = new ImageAdapter[SIZE];
        for (int i = 0; i < SIZE; i++)
            imageAdapter[i] = new ImageAdapter(getApplicationContext());

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentManager fm = getSupportFragmentManager();
        tabAdapter = new TabAdapter(fm, imageAdapter);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);


        Intent intent = getIntent();
        boolean flag = intent.getBooleanExtra("STOP", false);
        if (!flag) {
            popUp = inflater.inflate(R.layout.logo, null, false);
            progressBar = (ProgressBar) popUp.findViewById(R.id.progressId);
            contentFrame.addView(popUp);

            getSupportActionBar().hide();
            LoadAllItemAsyncTask s = new LoadAllItemAsyncTask(getApplication().getApplicationContext(), progressBar);
            s.setListener(this);
            s.execute();
            /*itemArrayList = (ArrayList<Item>) getIntent().getSerializableExtra("itemArray");

            dataManager.setItems(itemArrayList);
            dataManager.orderingItemsByCategory(dataManager.getItems());


            viewPager.setAdapter(tabAdapter);

            tabs.setViewPager(viewPager);*/
            Log.e("Error", "dasddHere");
        } else {
            final View popUp = inflater.inflate(R.layout.pop_up_progress_bar, null, false);
            final ProgressBar progressBar = (ProgressBar) popUp.findViewById(R.id.popUpProgressBar);
            contentFrame.addView(popUp);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    contentFrame.removeView(popUp);
                }

            }, 2000L);
            getSupportActionBar().show();
            LoadAllItemAsyncTask s = new LoadAllItemAsyncTask(getApplication().getApplicationContext(), progressBar);
            s.execute();
            Log.e("Error", "Aftercgvcv");
        }
        Log.e("FeedActivity", "FeedActivity");


    }

    private void removeSplashScreen() {
        progressBar.setVisibility(View.INVISIBLE);
        contentFrame.removeView(popUp);
        getSupportActionBar().show();
    }

    @Override
    public void onDataReceived() {
        removeSplashScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.searchID).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewPager.setCurrentItem(FeedActivity.SIZE-1);
                imageAdapter[3].getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

            if (id == R.id.filterID) {
                ListPopupWindow listPopupWindow = new ListPopupWindow(this);
                listPopupWindow.setModal(true);

                //to prevent null inside String
                String strings[] = new String[]{""};
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        strings = new String[]{"Jacket", "Shirt", "Coat"};
                        break;
                    case 1:
                        strings = new String[]{"Socks", "Pants", "Underwear"};
                        break;
                    case 2:
                        strings = new String[]{"Boots", "FlipFlops", "Sports"};
                        break;
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.filter_item, strings);

                // listPopupWindow.setListSelector(getResources().getDrawable(R.drawable.btn_borderless));
                listPopupWindow.setAdapter(adapter);
                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CheckedTextView checkedTextView = (CheckedTextView) view;
                        if (checkedTextView.isChecked())
                            checkedTextView.setChecked(false);
                        else
                            checkedTextView.setChecked(true);
                    }
                });
                listPopupWindow.setAnchorView(findViewById(R.id.filterID));
                listPopupWindow.setWidth(450);
                listPopupWindow.show();
            }


        return super.onOptionsItemSelected(item);
    }

    class LoadAllItemAsyncTask extends AsyncTask {

    private FetchDataListener fetchListener;

    private ProgressBar progressBar;
    private DataManager dataManager;
    private Context c;
    private boolean flag = false;
    private ArrayList<Item> itemArrayList;


    public LoadAllItemAsyncTask(Context c, ProgressBar progressBar){
        this.c = c;
        this.progressBar = progressBar;
        this.dataManager = DataManager.getInstance();
        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    protected Object doInBackground(Object... objects) {
        dataManager.getAllObjects(new DataManager.GetAllCallback() {
            @Override

            public void done(ArrayList<Item> value, BackendlessException e) {
                itemArrayList = value;
                flag = true;
                Log.e("Error","finish load all the items");
            }
        });
        try {
            Thread.sleep(2000);
            Log.e("Error", "sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        if(flag) {
            if (fetchListener != null) {
                fetchListener.onDataReceived();
            }
            progressBar.setVisibility(View.INVISIBLE);
            dataManager.setItems(itemArrayList);
            dataManager.orderingItemsByCategory(dataManager.getItems());
            viewPager.setAdapter(tabAdapter);
            tabs.setViewPager(viewPager);
        }
    }

        public void setListener(FetchDataListener listener) {
            this.fetchListener = listener;
        }
    }
}

class TabAdapter extends FragmentStatePagerAdapter {


    FragmentCategory fragmentCategory;
    ImageAdapter[] im;
    DataManager dataManager;

    public TabAdapter(FragmentManager fm,ImageAdapter[] imageAdapter) {
        super(fm);
        im = imageAdapter;
        this.dataManager = DataManager.getInstance();
    }

    @Override
    public Fragment getItem(int position) {
        fragmentCategory = FragmentCategory.getInstance(position, im[position]);
        return fragmentCategory;
    }

    @Override
    public int getCount() {
        return FeedActivity.SIZE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return FeedActivity.TOPS;
            case 1: return FeedActivity.BOTTOMS;
            case 2: return FeedActivity.SHOES;
            case 3: return FeedActivity.CUSTOM;
        }
        return null;
    }
}



