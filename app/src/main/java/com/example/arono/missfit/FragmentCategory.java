package com.example.arono.missfit;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.example.arono.missfit.Activities.FeedActivity;
import com.example.arono.missfit.Activities.MyItemsActivity;
import com.example.arono.missfit.DataServerManagement.DataManager;

import java.util.ArrayList;


public class FragmentCategory extends Fragment {

    final float FLHEIGHT = 0.8f;
    FrameLayout frameLayoutChild;
    RelativeLayout rl;
    FrameLayout.LayoutParams flLayoutParams;
    GridView gvItems;
    DisplayMetrics displayMetrics;
    public ImageAdapter imageAdapter;
    RelativeLayout.LayoutParams relativeLayoutParams;
    int flDimension,position;
    DataManager dataManager;
    ArrayList<Item> itemsArray;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        
        View layout = null;
        try {
            if (bundle != null) {
                position = bundle.getInt("position");
                switch (position) {
                    case 0:
                        layout = inflater.inflate(R.layout.fragment_tops, container, false);
                        break;
                    case 1:
                        layout = inflater.inflate(R.layout.fragment_bottom, container, false);
                        break;
                    case 2:
                        layout = inflater.inflate(R.layout.fragment_shoes, container, false);
                        break;
                    case 3:
                        layout = inflater.inflate(R.layout.fragment_custom, container, false);
                        break;
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        return layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // create twice in the first Category and the second one
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("OnActivityCreated:", "Here");

        
        switch (position) {
            case 0: rl = (RelativeLayout)getActivity().findViewById(R.id.rl1);
                init(dataManager.getItemsTops());
                break;
            case 1: rl = (RelativeLayout)getActivity().findViewById(R.id.rl2);
                init(dataManager.getItemsBottoms());
                break;
            case 2: rl = (RelativeLayout)getActivity().findViewById(R.id.rl3);
                init(dataManager.getItemShoes());
                break;
            case 3: rl = (RelativeLayout)getActivity().findViewById(R.id.rl4);
                init(dataManager.getItemsCustom());
                break;
        }

        gridViewSelectedItems();


    }


    public void gridViewSelectedItems(){
        gvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MyItemsActivity.class);
                startActivity(intent);
            }
        });
    }
    public int display(){
        displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightPixels = displayMetrics.heightPixels;
        return (int) (heightPixels * FLHEIGHT);
    }

    public static FragmentCategory getInstace(int position,ImageAdapter imageAdapter,DataManager dataManger){
        FragmentCategory FragmentCategory = new FragmentCategory();
        FragmentCategory.imageAdapter = imageAdapter;
        FragmentCategory.dataManager = dataManger;
        
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        FragmentCategory.setArguments(bundle);
        return FragmentCategory;
    }

    public void init(ArrayList<Item> itemArray){
        imageAdapter.setItems(itemArray);
        frameLayoutChild = new FrameLayout(getActivity());
        gvItems = new GridView(getActivity());
        flDimension = display();
        flLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,flDimension);
        gvItems.setNumColumns(2);
        gvItems.setAdapter(imageAdapter);
        frameLayoutChild.addView(gvItems);
        rl.addView(frameLayoutChild, flLayoutParams);
    }


}
