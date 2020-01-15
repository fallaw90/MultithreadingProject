package com.fallntic.multithreadingproject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class L {

    public static void m(String message){
        Log.d("MultiThreadingProject", message);
    }

    public static void s(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
