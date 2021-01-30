package com.gaurav.android_gisfy.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gaurav.android_gisfy.ImageActivity;
import com.gaurav.android_gisfy.R;
import com.gaurav.android_gisfy.Utils.VolleyHelper;
import com.gaurav.android_gisfy.VideoActivity;
import com.gaurav.android_gisfy.database.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>
{


     Context mCtx;
     List<User> taskList;

    public UserAdapter(Context mCtx, List<User> taskList) {
        this.mCtx = mCtx;
        this.taskList = taskList;
    }


    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position)
    {
        User user = taskList.get(position);
        holder.nameUser.setText(user.getName());
        holder.classUser.setText(user.getUserClass());
        holder.latUser.setText(user.getLatitude());
        holder.longUser.setText(user.getLongitude());

        byte[] decodedString = Base64.decode(user.getPhoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.userImage.setImageBitmap(decodedByte);
        holder.exerciseVideo.setVideoPath(user.getVideo());



        holder.exerciseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(mCtx, VideoActivity.class);
                i.putExtra("VideoUser",taskList.get(position).getVideo());
                mCtx.startActivity(i);
            }
        });



        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(mCtx, ImageActivity.class);
                i.putExtra("ImageUser",taskList.get(position).getPhoto());
                mCtx.startActivity(i);
            }
        });


        holder.uploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String postName = user.getName();
                String postClass = user.getUserClass();
                String postImage = taskList.get(position).getPhoto();
                String postVideo = taskList.get(position).getVideo();

                uploadFile(postName,postClass,postImage,postVideo);


            }
        });









    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder
    {
        TextView nameUser,classUser,latUser,longUser;
        ImageView userImage,uploadData;
        VideoView exerciseVideo;

        public UserHolder(@NonNull View itemView) {

            super(itemView);
            nameUser = itemView.findViewById(R.id.nameUser);
            classUser = itemView.findViewById(R.id.classUser);
            longUser = itemView.findViewById(R.id.longUser);
            latUser = itemView.findViewById(R.id.latUser);
            userImage = itemView.findViewById(R.id.userImage);
            exerciseVideo = itemView.findViewById(R.id.exerciseVideo);
            uploadData = itemView.findViewById(R.id.uploadData);

        }
    }

    void uploadFile(String name,String userClass,String image,String video)
    {
        final ProgressDialog progressDialog = ProgressDialog.show(mCtx,"Uploading Item...","Please wait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.googleapis.com/upload/drive/v3/files?uploadType=media", new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                Toast.makeText(mCtx, response, Toast.LENGTH_SHORT).show();
                System.out.print("SuccessResponse"+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mCtx, error.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.print("ErrorResponse"+error);
            }
        }


        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("action", "addItem");
                params.put("itemName", "Mobile");
                params.put("brand", "Lg");
                return params;
            }
        };



        VolleyHelper.getInstance(mCtx).addToRequestQueue(stringRequest);

    }



}
