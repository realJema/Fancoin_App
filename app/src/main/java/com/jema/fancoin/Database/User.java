package com.jema.fancoin.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "full_name")
    public String full_name;

    @ColumnInfo(name = "application_status")
    public String application_status;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "bio")
    public String bio;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "pricing")
    public String pricing;

    @ColumnInfo(name = "uid")
    public String uid;

    public User(int id, String username, String full_name, String application_status, String category, String email, String bio, String followers, String following, String image, String phone, String pricing, String uid) {
        this.id = id;
        this.username = username;
        this.full_name = full_name;
        this.application_status = application_status;
        this.category = category;
        this.email = email;
        this.bio = bio;
        this.image = image;
        this.phone = phone;
        this.pricing = pricing;
        this.uid = uid;
    }

    public User() {

    }
}