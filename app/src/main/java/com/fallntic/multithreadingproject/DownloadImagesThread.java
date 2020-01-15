package com.fallntic.multithreadingproject;

import android.app.Activity;
import android.view.View;

import static com.fallntic.multithreadingproject.MainActivity.downloadImageUsingThreads;
import static com.fallntic.multithreadingproject.MainActivity.loadingSection;

public class DownloadImagesThread implements Runnable {

    private String url;
    private Activity mActivity;

    public DownloadImagesThread(Activity activity, String url){
        this.url = url;
        this.mActivity = activity;
    }

    @Override
    public void run() {

        /*
         * Because we cannot modify views from a background thread
         * Then we call our MainActivity in which the main thread is running
         * activity object in our case
         */

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //The linear layout that contain the progressBar
                loadingSection.setVisibility(View.VISIBLE);
            }
        });
        downloadImageUsingThreads(this.mActivity, url);
    }
}
