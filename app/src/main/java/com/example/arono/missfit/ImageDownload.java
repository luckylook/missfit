package com.example.arono.missfit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by arono on 02/03/2016.
 */
public class ImageDownload extends AsyncTask {

        private String requestUrl;
        private ImageView view;
        private Bitmap pic;
        private ProgressBar progressBar;

        public ImageDownload(String requestUrl, ImageView view,ProgressBar progressBar) {
            this.requestUrl = requestUrl;
            this.view = view;
            this.progressBar = progressBar;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                pic = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressBar.setVisibility(View.INVISIBLE);
            view.setImageBitmap(pic);
            progressBar.setVisibility(View.INVISIBLE);

        }

}

