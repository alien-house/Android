package com.alienhouse.kitten.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shinji on 2017/09/03.
 */

public class GetImageTaskUpload extends AsyncTask<String, Void, Bitmap> {
    Bitmap bmImage;
    private CallBackTask callbacktask;

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
        System.out.println("GetImageTaskUpload:onPostExecute :: ");
        callbacktask.CallBack(bitmap);
    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public static class CallBackTask {
        public void CallBack(Bitmap bitmap) {
        }
    }
}
