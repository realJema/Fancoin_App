package com.jema.fancoin.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class CommentModel {

    String descr, photo, commenter_uid, owner_uid, commenter_username;
    Date date;

    public CommentModel(){};

    public CommentModel(String descr, String photo, String commenter_uid, String owner_uid, String commenter_username, Date date) {
        this.descr = descr;
        this.photo = photo;
        this.commenter_uid = commenter_uid;
        this.owner_uid = owner_uid;
        this.commenter_username = commenter_username;
        this.date = date;
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

    public String getCommenter_uid() {
        return commenter_uid;
    }

    public void setCommenter_uid(String commenter_uid) {
        this.commenter_uid = commenter_uid;
    }

    public String getOwner_uid() {
        return owner_uid;
    }

    public void setOwner_uid(String owner_uid) {
        this.owner_uid = owner_uid;
    }

    public String getCommenter_username() {
        return commenter_username;
    }

    public void setCommenter_username(String commenter_username) {
        this.commenter_username = commenter_username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
