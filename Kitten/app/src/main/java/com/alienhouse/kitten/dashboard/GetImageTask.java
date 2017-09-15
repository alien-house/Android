package com.alienhouse.kitten.dashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by shinji on 2017/08/29.
 */

public class GetImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public GetImageTask(ImageView bmImage){
        this.bmImage = bmImage;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        //Retreiving the url for FBprogile image
        String urlDisplay = strings[0];
        Bitmap myImage = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            myImage = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myImage;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        System.out.println("GetImageTask:onPostExecute :: ");
        bmImage.setImageBitmap(bitmap);
    }

    public ImageView getImgView(){
        return bmImage;
    }

}
