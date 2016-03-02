package com.example.arono.missfit.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.arono.missfit.DataServerManagement.BackendUtility;
import com.example.arono.missfit.DataServerManagement.DataManager;
import com.example.arono.missfit.Drawer.BaseActivityWithNavigationDrawer;
import com.example.arono.missfit.Item;
import com.example.arono.missfit.Picture;
import com.example.arono.missfit.R;


public class AddItemActivity extends BaseActivityWithNavigationDrawer implements View.OnClickListener{


    private static int RESULT_LOAD_IMAGE = 1;
    public static int id = 0;
    private Button btnCancel,btnSave;
    private ImageView ivFirstPic,ivSecPic,ivThirdtPic;
    private CheckBox checkBoxIvFirst,checkBoxIvSecond,checkBoxIvThird;
    private Bitmap bitmapRotate = null;
    private Spinner sizeSpinner,typeSpinner;
    private EditText etPrice,etDescription;
    private Picture picture;
    int num = 0;
    LayoutInflater inflater;
    FrameLayout contentFrame;
    private DataManager dataManager;
    BackendlessUser user;
    String url;
    String[] b;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Backendless.initApp(this, BackendUtility.APPLIATION_ID, BackendUtility.APPLIATION_Key, BackendUtility.VERSION);

        contentFrame = getContentFrame();
        inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_add_item, null, false);
        contentFrame.addView(view);

        dataManager = new DataManager();


        Intent titleIntent = getIntent();
        String title =  titleIntent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);

        checkBoxIvFirst = (CheckBox) findViewById(R.id.checkBoxIvFirst);
        checkBoxIvSecond = (CheckBox) findViewById(R.id.checkBoxIvSecond);
        checkBoxIvThird = (CheckBox) findViewById(R.id.checkBoxIvThird);

        ivFirstPic = (ImageView)findViewById(R.id.ivFirstPic);
        ivSecPic = (ImageView)findViewById(R.id.ivSecPic);
        ivThirdtPic = (ImageView)findViewById(R.id.ivThirdtPic);


        sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etDescription = (EditText) findViewById(R.id.etDescription);

        ivFirstPic.setImageResource(R.drawable.add3);
        ivSecPic.setImageResource(R.drawable.add3);
        ivThirdtPic.setImageResource(R.drawable.add3);

        ivFirstPic.setOnClickListener(this);
        ivSecPic.setOnClickListener(this);
        ivThirdtPic.setOnClickListener(this);

        picture = new Picture(this);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkBoxEnable();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = typeSpinner.getSelectedItem().toString();
                Item.Size size = Item.stringToSize(sizeSpinner.getSelectedItem().toString());
                Bitmap bitmap[] = new Bitmap[3];
                bitmap[0] = ((BitmapDrawable) ivFirstPic.getDrawable()).getBitmap();
                bitmap[1] = ((BitmapDrawable) ivSecPic.getDrawable()).getBitmap();
                bitmap[2] = ((BitmapDrawable) ivThirdtPic.getDrawable()).getBitmap();
                String description = etDescription.getText().toString();
                Float price = Float.parseFloat(etPrice.getText().toString());
                user = Backendless.UserService.CurrentUser();
                Item item = new Item();
                item.setItem(description, price, type, user, size);

                /*String[] photos = new String[3];
                photos[0] = dataManager.uploadPicture(ivFirstPic,3);
                photos[1] = dataManager.uploadPicture(ivSecPic,4);
                photos[2] = dataManager.uploadPicture(ivThirdtPic,5);


                item.setPhotoOne(photos[0]);
                item.setPhotoTwo(photos[1]);
                item.setPhotoThird(photos[2]);
                dataManager.uploadToServer(item);*/
                dataManager.getSpecificUserItems(user);

            }
        });
    }


    public void checkBoxEnable(){
        checkBoxIvFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxIvFirst.isChecked()){
                    checkBoxIvFirst.setChecked(true);
                    checkBoxIvSecond.setChecked(false);
                    checkBoxIvThird.setChecked(false);
                }else {
                    checkBoxIvFirst.setChecked(false);
                }

            }
        });
        checkBoxIvSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxIvSecond.isChecked()) {
                    checkBoxIvSecond.setChecked(true);
                    checkBoxIvThird.setChecked(false);
                    checkBoxIvFirst.setChecked(false);
                }else {
                    checkBoxIvSecond.setChecked(false);
                }
            }
        });
        checkBoxIvThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxIvThird.isChecked()) {
                    checkBoxIvThird.setChecked(true);
                    checkBoxIvSecond.setChecked(false);
                    checkBoxIvFirst.setChecked(false);
                } else {
                    checkBoxIvThird.setChecked(false);
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Bitmap bitmap = picture.getPictureFromGallery(data);

            switch(num){
                case 1: ivFirstPic.setImageBitmap(bitmap);
                    break;
                case 2: ivSecPic.setImageBitmap(bitmap);
                    break;
                case 3: ivThirdtPic.setImageBitmap(bitmap);
                    break;
            }
            num = 0;

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.rotateId){
            if (checkBoxIvFirst.isChecked()) {
                bitmapRotate = ((BitmapDrawable) ivFirstPic.getDrawable()).getBitmap();
                ivFirstPic.setImageBitmap(picture.rotateImage(bitmapRotate, picture.ORIENTATION_ROTATE_90));
            }
            if (checkBoxIvSecond.isChecked()) {
                bitmapRotate = ((BitmapDrawable) ivSecPic.getDrawable()).getBitmap();
                ivSecPic.setImageBitmap(picture.rotateImage(bitmapRotate, picture.ORIENTATION_ROTATE_90));
            }
            if (checkBoxIvThird.isChecked()) {
                bitmapRotate = ((BitmapDrawable) ivThirdtPic.getDrawable()).getBitmap();
                ivThirdtPic.setImageBitmap(picture.rotateImage(bitmapRotate, picture.ORIENTATION_ROTATE_90));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        switch(id){
            case R.id.ivFirstPic:   num = 1;
                break;
            case R.id.ivSecPic:     num = 2;
                break;
            case R.id.ivThirdtPic:  num = 3;
                break;
        }
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


}


