package com.jema.fancoin.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class CommentModel {

    String descr, commenter_photo, commenter_uid, star_uid, commenter_username;
    Date date;

    public CommentModel(){};

    public CommentModel(String descr, String photo, String commenter_uid, String owner_uid, String commenter_username, Date date, String star_uid, String commenter_photo) {
        this.descr = descr;
        this.commenter_uid = commenter_uid;
        this.commenter_photo = commenter_photo;
        this.star_uid = owner_uid;
        this.commenter_username = commenter_username;
        this.date = date;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }


    public String getCommenter_uid() {
        return commenter_uid;
    }

    public void setCommenter_uid(String commenter_uid) {
        this.commenter_uid = commenter_uid;
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

    public String getCommenter_photo() {
        return commenter_photo;
    }

    public void setCommenter_photo(String commenter_photo) {
        this.commenter_photo = commenter_photo;
    }

    public String getStar_uid() {
        return star_uid;
    }

    public void setStar_uid(String star_uid) {
        this.star_uid = star_uid;
    }
}
