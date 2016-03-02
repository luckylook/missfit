package com.example.arono.missfit.DataServerManagement;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.arono.missfit.Item;

import java.util.ArrayList;

/**
 * Created by arono on 24/02/2016.
 */
public class DataManager {

    public final static String PICTURE_URL ="https://api.backendless.com/"+BackendUtility.APPLIATION_ID+"/"+BackendUtility.VERSION+"/files/"+"mypics/";
    ArrayList<Item> items;
    public DataManager(){

    }


    public void uploadToServer(Item item){
        Backendless.Data.of(Item.class).save(item, new AsyncCallback<Item>() {
            @Override
            public void handleResponse(Item response) {
                Log.e("Error", "ye");
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("Error", fault.getMessage());
            }
        });
    }

    public String uploadPicture(ImageView imageView,int id){
        String url = "";

        Bitmap photo = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        Backendless.Files.Android.upload( photo, Bitmap.CompressFormat.PNG, 100, id+".png", "mypics", new AsyncCallback<BackendlessFile>()
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
        url = DataManager.PICTURE_URL+id+".png";
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

    public void getSpecificUserItems(BackendlessUser user){
        BackendlessDataQuery query = new BackendlessDataQuery();
        String whereCaluse = "ownerId = '" + user.getObjectId() + "'";
        query.setWhereClause(whereCaluse);
        Backendless.Persistence.of(Item.class).find(query, new AsyncCallback<BackendlessCollection<Item>>() {
            @Override
            public void handleResponse(BackendlessCollection<Item> response) {
                ArrayList<Item> itemArrayList  = (ArrayList<Item>) response.getData();
                Log.e("Item",""+itemArrayList.size());
                Log.e("Item", itemArrayList.get(0).getName());
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

    }

    public void setItems(ArrayList<Item> items){
        this.items = items;
    }
    public ArrayList<Item> getItems(){
        return items;
    }
}
