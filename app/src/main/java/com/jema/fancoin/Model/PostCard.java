package com.jema.fancoin.Model;

import java.util.ArrayList;

public class PostCard {
    String image, name, pricing, bio, category, id, application_followers, application_social, application_status, application_username;
    ArrayList<String> followers;

    public PostCard(){};

    public PostCard(String image, String name, String pricing, String bio, String category, String id, String application_followers, String application_social, String application_status, String application_username, ArrayList<String> followers) {
        this.image = image;
        this.name = name;
        this.pricing = pricing;
        this.bio = bio;
        this.category = category;
        this.id = id;
        this.application_followers = application_followers;
        this.application_social = application_social;
        this.application_status = application_status;
        this.application_username = application_username;
        this.followers = followers;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public String getApplication_followers() {
        return application_followers;
    }

    public void setApplication_followers(String application_followers) {
        this.application_followers = application_followers;
    }

    public String getApplication_social() {
        return application_social;
    }

    public void setApplication_social(String application_social) {
        this.application_social = application_social;
    }

    public String getApplication_status() {
        return application_status;
    }

    public void setApplication_status(String application_status) {
        this.application_status = application_status;
    }

    public String getApplication_username() {
        return application_username;
    }

    public void setApplication_username(String application_username) {
        this.application_username = application_username;
    }
}
