package com.fallntic.multithreadingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText mEditText;
    private ListView mListView;
    public static String [] listOfImages;
    private ProgressBar mProgressBar;
    public static LinearLayout loadingSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.downloadURL);
        mListView = (ListView) findViewById(R.id.urlList);
        mListView.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.imageUrls);
        mProgressBar = (ProgressBar) findViewById(R.id.downloadProgress);
        loadingSection = (LinearLayout) findViewById(R.id.loadingSection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void downloadImage(View view){

        String url = mEditText.getText().toString();
        Thread myThread = new Thread(new DownloadImagesThread(this, url));
        myThread.start();
    }

    public static boolean downloadImageUsingThreads(Activity activity, String url){

        /*
         * 1 Create the url object that represents the url
         * 2 Open connection using that url object
         * 3 Read data using input stream into a byte array
         * 4 Open a file output stream to save data on sd card
         * 5 Write data to the fileoutputstream
         * 6 Close connection
         */

        boolean successful = false;
        URL downloadURL = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            downloadURL = new URL(url);
            //Recommend it by goole for all purposes while establishing connection
            connection = (HttpURLConnection) downloadURL.openConnection();
            inputStream = connection.getInputStream();

            file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .getAbsoluteFile() + "/" + Uri.parse(url).getLastPathSegment());
            fileOutputStream = new FileOutputStream(file);
            L.m("" + file.getAbsolutePath());
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1){

                fileOutputStream.write(buffer, 0, read);
                //L.m(""+read);
            }
            successful = true;
        } catch (MalformedURLException e) {
            L.m(e + "");
            e.printStackTrace();
        } catch (IOException e) {
            L.m(e + "");
            e.printStackTrace();
        }
        finally {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingSection.setVisibility(View.GONE);
                }
            });

            if (connection != null){
                connection.disconnect();
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    L.m(e + "");
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    L.m(e + "");
                    e.printStackTrace();
                }
            }
        }

        return successful;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        mEditText.setText(listOfImages[i]);
    }
}
