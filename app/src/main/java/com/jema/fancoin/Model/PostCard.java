package com.jema.fancoin.Model;

public class PostCard {
    String image, name, pricing, bio, category;

    public PostCard(){};

    public PostCard(String image, String name, String pricing, String bio, String category){
        this.image = image;
        this.name = name;
        this.pricing = pricing;
        this.bio = bio;
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
