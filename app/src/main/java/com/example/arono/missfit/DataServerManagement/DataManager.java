package com.example.arono.missfit.DataServerManagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.arono.missfit.Activities.FeedActivity;
import com.example.arono.missfit.Item;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arono on 24/02/2016.
 */
public class DataManager {

    public final static String PICTURE_URL ="https://api.backendless.com/"+BackendUtility.APPLIATION_ID+"/"+BackendUtility.VERSION+"/files/"+"mypics/";
    ArrayList<Item> items;
    ArrayList<Item> itemsTops,itemsBottoms,itemShoes,itemsCustom;

    public DataManager(){

    }


    public void uploadToServer(Item item, final Context context){
        Backendless.Data.of(Item.class).save(item, new AsyncCallback<Item>() {
            @Override
            public void handleResponse(Item response) {
                Toast.makeText(context, "saved successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("Error", fault.getMessage());
            }
        });
    }

    public String uploadPicture(ImageView imageView,int id,String name){
        String url = "";

        Bitmap photo = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        Backendless.Files.Android.upload( photo, Bitmap.CompressFormat.PNG, 100, name+id+".png", "mypics", new AsyncCallback<BackendlessFile>()
        {
            @Override
            public void handleResponse( final BackendlessFile backendlessFile )
            {
                Log.e("Error", "work");
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                Log.e("Error",backendlessFault.getMessage());
            }
        });
        url = DataManager.PICTURE_URL+name+id+".png";
        return url;
    }

    public interface GetAllCallback {
        void done(ArrayList<Item> value, BackendlessException e);
    }


    public void getAllObjects(final GetAllCallback callback) {
        if (callback == null) {
            return;
        }
        final BackendlessDataQuery query = new BackendlessDataQuery();
        Backendless.Persistence.of(Item.class).find(query, new AsyncCallback<BackendlessCollection<Item>>() {


            @Override
            public void handleResponse(BackendlessCollection<Item> response) {
                ArrayList<Item> savedItems = new ArrayList<Item>(response.getData());
                callback.done(savedItems, null);

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("Error", "... Failed to get all objects");
                callback.done(null, new BackendlessException(fault.getCode(), fault.getMessage()));
            }
        });
    }

    public void getSpecificUserItems(BackendlessUser user,final GetAllCallback callback){
        BackendlessDataQuery query = new BackendlessDataQuery();
        String whereCaluse = "ownerId = '" + user.getObjectId() + "'";
        query.setWhereClause(whereCaluse);
        Backendless.Persistence.of(Item.class).find(query, new AsyncCallback<BackendlessCollection<Item>>() {
            @Override
            public void handleResponse(BackendlessCollection<Item> response) {
                ArrayList<Item> itemArrayList  = (ArrayList<Item>) response.getData();
                Log.e("Item",""+itemArrayList.size());
                Log.e("Item", itemArrayList.get(0).getName());
                callback.done(itemArrayList,null);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                callback.done(null,new BackendlessException(fault.getCode(), fault.getMessage()));
            }
        });

    }


    public void downloadImageFromServer(Context context,Item item,ImageView imageView, final ProgressBar progressBar){
        Picasso.with(context).load(item.getPhotoOne()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {

            }
        });
    }

    public void setItems(ArrayList<Item> items){
        this.items = items;
    }
    public ArrayList<Item> getItems(){
        return items;
    }


    public void orderingItemsByCategory(ArrayList<Item> itemsArray){
        itemsTops = new ArrayList<>();
        itemsBottoms = new ArrayList<>();
        itemShoes = new ArrayList<>();
        itemsCustom = new ArrayList<>();
        Log.e("Error","Ordering Items");
        for(int i = 0 ; i < itemsArray.size() ; i++) {
            Item item =  itemsArray.get(i);
            String type = item.getType();
            if(type.toUpperCase().compareTo(FeedActivity.TOPS) == 0)
                itemsTops.add(item);
            else if(type.toUpperCase().compareTo(FeedActivity.BOTTOMS) == 0)
                itemsBottoms.add(item);
            else{
                itemShoes.add(item);
            }
        }
        setItemsTops(itemsTops);
        setItemsBottoms(itemsBottoms);
        setItemsShoes(itemShoes);
        setItemsCustom(itemsCustom);
    }

    public void setItemsTops(ArrayList<Item> itemsTops){
        this.itemsTops = itemsTops;
    }
    public void setItemsShoes(ArrayList<Item> itemShoes){
        this.itemShoes = itemShoes;
    }
    public void setItemsBottoms(ArrayList<Item> itemsBottoms){
        this.itemsBottoms = itemsBottoms;
    }
    public void setItemsCustom(ArrayList<Item> itemsCustom){
        this.itemsCustom = itemsCustom;
    }
    public ArrayList<Item> getItemsCustom(){
        return itemsCustom;
    }
    public ArrayList<Item> getItemsTops(){
        return itemsTops;
    }
    public ArrayList<Item> getItemsBottoms(){
        return itemsBottoms;
    }
    public ArrayList<Item> getItemShoes(){
        return itemShoes;
    }

}
