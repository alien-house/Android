package com.example.shinji.imagedownloadasyntask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText selectiontext;
    ListView chooseImageList;
    String[] listofImages;
    ProgressBar downloadImageProgress;
    ArrayAdapter<Bitmap> adpterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectiontext = (EditText) findViewById(R.id.urlselectionText);
        chooseImageList = (ListView) findViewById(R.id.chooseImageList);
        downloadImageProgress = (ProgressBar) findViewById(R.id.downloadProgress);
        listofImages = getResources().getStringArray(R.array.imageuris);
        chooseImageList.setOnItemClickListener(this);
//
//        ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
//        chooseImageList.setAdapter(a);
//
//        adpterImage = new ArrayAdapter<Bitmap>(this, android.R.layout.simple_list_item_1, new ArrayList<Bitmap>());
//        chooseImageList.setAdapter(adpterImage);

    }

    public void downloadImage(View view) {
        if (selectiontext.getText().toString() != null
                && (selectiontext.getText().toString().length()) > 0) {
            MyTask myTask = new MyTask();
            myTask.execute(selectiontext.getText().toString());
        }
//        new MyTask("http://www.crossshoresolutions.com/blog/wp-content/uploads/2014/06/Android_1.5_Cupcake.jpg").execute();
            // create instance of subClass (MyTask).
            // call method execute() on it and it accepts text read from textview as parameter.


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectiontext.setText(listofImages[position]);
    }

    class MyTask extends AsyncTask<String,Integer,Boolean>{

        ArrayAdapter<ImageView> adpter;
        private int contentLength = -1;
        private int counter = 0;
        private String imgURL = "";

        @Override
        protected void onPreExecute() {
            //set visibility of progessbar to visible
//            adpter = (ArrayAdapter<ImageView>) chooseImageList.getAdapter();
            downloadImageProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.e("doInBackground","=========");
//            if(isExternalStorageWritable()){
//                Log.e("OK","yes save");
//            }else{
//                Log.e("none","no save");
//            }
            boolean successful = false;
            URL downloadURL = null;
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            File file = null;
            try {
                downloadURL = new URL(params[0]);
                connection = (HttpURLConnection) downloadURL.openConnection();
                contentLength = connection.getContentLength();
                inputStream = connection.getInputStream();
//                File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + Uri.parse(params[0]).getLastPathSegment());
                File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
//                File path = new File("/Users/shinji/Documents/git/alien_Android/ImageDownloadAsynTask/");
                file = new File(path + Uri.parse(params[0]).getLastPathSegment());
//                path.mkdirs();
                file.getParentFile().mkdir();
                fileOutputStream = new FileOutputStream(file);
                int read = -1;
                byte[] buffer = new byte[1024];
                while((read = inputStream.read(buffer)) != -1){
                    fileOutputStream.write(buffer, 0, read);
                    counter = counter + read;
                    publishProgress(counter);
                }
                Log.e("doInBackground","Success");
                successful = true;
            } catch (Exception e) {
                Log.e("Exception", String.valueOf(e));
                return false;
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
                if (inputStream != null){
                    try{
                        inputStream.close();
                    }catch (IOException e){
                        Log.e("IOException", String.valueOf(e));
                    }
                }
                if(fileOutputStream != null){
                    try{
                        fileOutputStream.close();
                    }catch (IOException e){
                        Log.e("fileOutputStream", String.valueOf(e));
                    }
                }
            }
            return successful;
//            return null;
            // Ceate an instance of URL, HttpURLConnection, InputStream, FileOutputStream,File class
            // Create a boolean variable successfull and set its intial value to false
            // if image download succesfully set it to true. return a boolean value of success
            // Write a code that download the image from internet
            // count how many bytes are downloded for that image and use this count to show the progress
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //calculate the progress and show it on progressbar
            int calculatedProgress = (int)(((double)values[0]/contentLength)*100);
            Log.e("onProgressUpdate", String.valueOf(calculatedProgress));
            downloadImageProgress.setProgress(calculatedProgress);

//            int val_progress = (int)((( (double)count / (double)cities.length )) * 100);
//            downloadImageProgress.setProgress(values);
        }

//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            //set visibility of progessbar to gone
//            ImageView imgvi = (ImageView) findViewById(R.id.imgvi);
//            saveBitmap(bitmap);
////            imgvi.setImageBitmap(bitmap);
////            File file = new File(context.getFilesDir(), filename);
//        }
//
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //set visibility of progessbar to gone
            Toast.makeText(MainActivity.this, "ALL TIME AND SUCESSFFLY", Toast.LENGTH_SHORT).show();
            downloadImageProgress.setVisibility(View.GONE);
        }

        public void saveBitmap(Bitmap bitmap){
            String fileName = "stored_image.jpg";
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String pathDir = baseDir + "/Android/data/com.mypackage.myapplication/";

            Log.e("saveBitmap:","----");
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File("/saved_images");
            myDir.mkdirs();
            Bitmap finalBitmap = null;
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Image-"+ n +".jpg";
            File dir = new File ("/Users/shinji/Documents/git/alien_Android/ImageDownloadAsynTask");

            if (!dir.exists());
            dir.mkdirs();
            File file = new File(dir, fname);
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }




    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}