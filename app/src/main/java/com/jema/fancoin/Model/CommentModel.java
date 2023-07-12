package com.jema.fancoin.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CommentModel {

    String descr, photo, uid, username;

    public CommentModel(){};

    public CommentModel(String descr, String photo, String uid, String username) {
        this.descr = descr;
        this.photo = photo;
        this.uid = uid;
        this.username = username;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
