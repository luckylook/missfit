package com.example.arono.missfit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

/**
 * Created by arono on 21/02/2016.
 */
public class Picture {

    public static final int ORIENTATION_ROTATE_90 = 90;
    private Context context;

    public Picture(Context context){
        this.context = context;
    }

    public Bitmap rotateImage(Bitmap bitmap,int angle){
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public int checkOrientation(String path){
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int angle;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = 270;
                break;
            default:
                angle = 0;
                break;
        }
        return angle;
    }

    public Bitmap getPictureFromGallery(Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

        int angle = checkOrientation(picturePath);

        if(angle == 0 && bitmap.getWidth() < bitmap.getHeight()) {
            bitmap = rotateImage(bitmap,ORIENTATION_ROTATE_90);
        }else
            bitmap = rotateImage(bitmap,angle);

        return bitmap;
    }


}
