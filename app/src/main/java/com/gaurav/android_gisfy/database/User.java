package com.gaurav.android_gisfy.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_name")
    private String name;

    @ColumnInfo(name = "user_class")
    private String userClass;


    @ColumnInfo(name = "user_photo")
    private String photo;


    @ColumnInfo(name = "user_video")
    private String video;




    @ColumnInfo(name = "user_latitude")
    private String latitude;



    @ColumnInfo(name = "user_longitude")
    private String longitude;


    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserClass()
    {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }
}