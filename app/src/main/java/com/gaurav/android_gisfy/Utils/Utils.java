package com.gaurav.android_gisfy.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Utils
{



    private static Context context;
    private static Activity currentActivity;
    private static final Utils ourInstance = new Utils();
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;




    public static Utils getInstance(Context context, Activity activity) {
        Utils.context = context;
        currentActivity = activity;
        return ourInstance;
    }


    public boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();


        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(currentActivity, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
