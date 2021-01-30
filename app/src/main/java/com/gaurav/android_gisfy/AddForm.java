package com.gaurav.android_gisfy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gaurav.android_gisfy.Utils.Utils;
import com.gaurav.android_gisfy.database.DatabaseClient;
import com.gaurav.android_gisfy.database.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AddForm extends AppCompatActivity {

    EditText et_name, et_class;
    Button btnPhoto, btnVideo, btnSave;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    String str_encodedBitmapCamera = "empty";
    String videoFileName;

    private static final int VIDEO_CAPTURE = 101;

    String getName,getClass,getImage,getVideo= "empty";

    GPSTracker gps;
    String getlong,getLat;
    ImageView iv_photo;
    VideoView iv_video;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS= 7;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_form);


        et_name = findViewById(R.id.et_name);
        et_class = findViewById(R.id.et_class);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnVideo = findViewById(R.id.btnVideo);
        btnSave = findViewById(R.id.btnSave);
        iv_photo = findViewById(R.id.iv_photo);
        iv_video = findViewById(R.id.iv_video);


        Long tsLong = System.currentTimeMillis() / 1000;
        videoFileName = tsLong.toString();



        checkAndroidVersion();




        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });


        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecordingVideo();

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {


                validation();




            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void validation()
    {
        if (et_name.getText().toString().isEmpty())
        {
            Toast.makeText(AddForm.this, "Enter name", Toast.LENGTH_SHORT).show();
        }
        else if(!et_name.getText().toString().isEmpty() && et_class.getText().toString().isEmpty())
        {
            Toast.makeText(AddForm.this, "Enter class", Toast.LENGTH_SHORT).show();

        }

        else if (!et_name.getText().toString().isEmpty() && !et_class.getText().toString().isEmpty() && str_encodedBitmapCamera.equalsIgnoreCase("empty"))
        {
            Toast.makeText(AddForm.this, "please upload photo", Toast.LENGTH_SHORT).show();

        }
        else if (!et_name.getText().toString().isEmpty() && !et_class.getText().toString().isEmpty() && !str_encodedBitmapCamera.equalsIgnoreCase("empty") && getVideo.equalsIgnoreCase("empty"))
        {
            Toast.makeText(AddForm.this, "please upload video", Toast.LENGTH_SHORT).show();

        }

        else
        {
            updateAddress();
        }




    }


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        }

    }


    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(AddForm.this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(AddForm.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(AddForm.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int location = ContextCompat.checkSelfPermission(AddForm.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(AddForm.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int location3 = ContextCompat.checkSelfPermission(AddForm.this, Manifest.permission.LOCATION_HARDWARE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }


        if (location2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }


        if (location3 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.LOCATION_HARDWARE);
        }





        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(AddForm.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
     void updateAddress()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (Utils.getInstance(getApplicationContext(), AddForm.this).checkAndRequestPermissions()) {

                gps = new GPSTracker(AddForm.this);

                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    System.out.println("present values===" + latitude + longitude);

                    getlong = String.valueOf(longitude);
                    getLat = String.valueOf(latitude);


                    getName = et_name.getText().toString();
                    getClass = et_class.getText().toString();

                    class SaveTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {

                            //creating a task
                            User user = new User();
                            user.setName(getName);
                            user.setUserClass(getClass);
                            user.setLatitude(getLat);
                            user.setLongitude(getlong);
                            user.setPhoto(str_encodedBitmapCamera);
                            user.setVideo(getVideo);

                            //adding to database
                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                    .myDao()
                                    .insert(user);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                        }
                    }

                    SaveTask st = new SaveTask();
                    st.execute();


                } else {
                    gps.showSettingsAlert();
                }

            }

        } else {
            showGPSDisabledAlertToUser();
            System.out.println("ggps not found");


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
           // Bitmap compressBitmap = scaleBitmap(photo);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream);
            byte[] byteArrayCamera = byteArrayOutputStream.toByteArray();

            str_encodedBitmapCamera = Base64.encodeToString(byteArrayCamera, Base64.DEFAULT);

            System.out.print("Bitmap>>"+str_encodedBitmapCamera);
            System.out.print("byteArray>>"+byteArrayCamera);

            iv_photo.setVisibility(View.VISIBLE);
            iv_photo.setImageBitmap(photo);


        } else {
            Uri videoUri = data.getData();

            if (resultCode == RESULT_OK)
            {
                MediaController mediaController = new MediaController(AddForm.this);
                mediaController.setAnchorView(iv_video);
                iv_video.setVisibility(View.VISIBLE);
                iv_video.setVideoURI(videoUri);
                getVideo = videoUri.toString();
                iv_video.start();
                Log.d("Video>>>>",getVideo);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        }
    }


    private Bitmap scaleBitmap(Bitmap bm) {
        int min = 100;

        int width = bm.getWidth();
        int height = bm.getHeight();

        if (width > height) {
            // landscape
            float ratio = (float) width / height;
            height = min;
            width = (int) (height * ratio);
        } else {
            float ratio = (float) height / width;
            width = min;
            height = (int) (width * ratio);
        }

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }


    public void startRecordingVideo() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory().getPath() + videoFileName + ".mp4");
            startActivityForResult(takeVideoIntent, VIDEO_CAPTURE);
        } else {
            Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
        }
    }


    public class GPSTracker extends Service implements android.location.LocationListener {

        private final Context mContext;

        // Flag for GPS status
        boolean isGPSEnabled = false;

        // Flag for network status
        boolean isNetworkEnabled = false;

        // Flag for GPS status
        boolean canGetLocation = false;

        Location location; // Location
        double latitude; // Latitude
        double longitude; // Longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                // Getting GPS status
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // Getting network status
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    Toast.makeText(AddForm.this, "GPS not enable", Toast.LENGTH_SHORT).show();
                } else {
                    this.canGetLocation = true;
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // If GPS enabled, get latitude/longitude using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        /**
         * Stop using GPS listener
         * Calling this function will stop using GPS in your app.
         */
        public void stopUsingGPS() {
            if (locationManager != null) {
                locationManager.removeUpdates(AddForm.GPSTracker.this);
            }
        }


        /**
         * Function to get latitude
         */
        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }


        /**
         * Function to get longitude
         */
        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/Wi-Fi enabled
         *
         * @return boolean
         */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }


        /**
         * Function to show settings alert dialog.
         * On pressing the Settings button it will launch Settings Options.
         */
        public void showSettingsAlert() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing the Settings button.
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            // On pressing the cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }


        @Override
        public void onLocationChanged(Location location) {
        }


        @Override
        public void onProviderDisabled(String provider) {
        }


        @Override
        public void onProviderEnabled(String provider) {
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }


        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }
    }


    public void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}