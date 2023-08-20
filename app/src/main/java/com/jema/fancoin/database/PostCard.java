package com.jema.fancoin.database;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import java.util.ArrayList;

@Entity
public class PostCard {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "pricing")
    public String pricing;

    @ColumnInfo(name = "bio")
    public String bio;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "application_followers")
    public int application_followers;

    @ColumnInfo(name = "application_social")
    public String application_social;

    @ColumnInfo(name = "application_status")
    public String application_status; // ArrayList of strings

    @ColumnInfo(name = "application_username")
    public String application_username;

    @ColumnInfo(name = "followers")
    public ArrayList<String> followers; // ArrayList of strings for followers

    @ColumnInfo(name = "following")
    public ArrayList<String> following;

    public void Postcard(String image, String name, String pricing, String bio, String category,
                         int application_followers, String application_social, String application_status,
                         String application_username, ArrayList<String> followers, ArrayList<String> following) {

        this.image = image;
        this.name = name;
        this.pricing = pricing;
        this.bio = bio;
        this.category = category;
        this.application_followers = application_followers;
        this.application_social = application_social;
        this.application_status = application_status;
        this.application_username = application_username;
        this.followers = followers;
        this.following = following;

    }

    public PostCard() {

    }

}
