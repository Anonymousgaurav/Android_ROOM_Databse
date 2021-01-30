package com.gaurav.android_gisfy.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;



public class VolleyHelper
{
    private static VolleyHelper mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private VolleyHelper(Context context)
    {
        // Specify the application context
        mContext = context;
        // Get the request queue
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyHelper getInstance(Context context)
    {
        // If Instance is null then initialize new Instance
        if(mInstance == null)
        {
            mInstance = new VolleyHelper(context);
        }
        // Return MySingleton new Instance
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        // If RequestQueue is null the initialize new RequestQueue
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        // Return RequestQueue
        return mRequestQueue;
    }

    public<T> void addToRequestQueue(Request<T> request){
        // Add the specified request to the request queue
        getRequestQueue().add(request);
    }

}
