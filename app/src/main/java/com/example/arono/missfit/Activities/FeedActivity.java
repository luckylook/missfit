package com.example.arono.missfit.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessException;
import com.example.arono.missfit.DataServerManagement.DataManager;
import com.example.arono.missfit.Drawer.BaseActivityWithNavigationDrawer;
import com.example.arono.missfit.FragmentCategory;
import com.example.arono.missfit.ImageAdapter;
import com.example.arono.missfit.Item;
import com.example.arono.missfit.R;
import com.example.arono.missfit.Slide.SlidingTabLayout;

import java.util.ArrayList;


public class FeedActivity extends BaseActivityWithNavigationDrawer {

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
    DataManager dataManager;
    ArrayList<Item> itemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        contentFrame = getContentFrame();
        inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_feed, null, false);
        contentFrame.addView(view);

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

        imageAdapter = new ImageAdapter[SIZE];
        for(int i = 0 ; i < SIZE ; i++)
            imageAdapter[i] = new ImageAdapter(getApplicationContext());

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        itemArrayList = (ArrayList<Item>) getIntent().getSerializableExtra("itemArray");
        dataManager = new DataManager();
        dataManager.setItems(itemArrayList);
        dataManager.orderingItemsByCategory(dataManager.getItems());
        
        FragmentManager fm = getSupportFragmentManager();
        tabAdapter = new TabAdapter(fm,imageAdapter,dataManager);
        viewPager.setAdapter(tabAdapter);
        tabs = (SlidingTabLayout)findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);

        Log.e("FeedActivity", "FeedActivity");


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
                imageAdapter[0].getFilter().filter(newText);
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
}

class TabAdapter extends FragmentStatePagerAdapter {


    FragmentCategory fragmentCategory;
    ImageAdapter[] im;
    DataManager dataManager;

    public TabAdapter(FragmentManager fm,ImageAdapter[] imageAdapter,DataManager dataManager) {
        super(fm);
        im = imageAdapter;
        this.dataManager = dataManager;
    }

    @Override
    public Fragment getItem(int position) {
        fragmentCategory = FragmentCategory.getInstace(position, im[position],dataManager);
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


