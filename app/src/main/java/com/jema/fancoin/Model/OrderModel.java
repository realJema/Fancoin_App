package com.jema.fancoin.Model;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class OrderModel {

    String date, description, recipient, id, star_uid, star_image, star_pricing, star_name, client_uid, client_image, client_name, client_phoneNumber, client_email;

    public OrderModel(){};

    public OrderModel(String date, String description, String recipient, String id, String star_uid, String star_image, String star_pricing, String star_name, String client_uid, String client_image, String client_name, String client_phoneNumber, String client_email) {
        this.date = date;
        this.description = description;
        this.recipient = recipient;
        this.id = id;
        this.star_uid = star_uid;
        this.star_image = star_image;
        this.star_pricing = star_pricing;
        this.star_name = star_name;
        this.client_uid = client_uid;
        this.client_image = client_image;
        this.client_name = client_name;
        this.client_phoneNumber = client_phoneNumber;
        this.client_email = client_email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStar_uid() {
        return star_uid;
    }

    public void setStar_uid(String star_uid) {
        this.star_uid = star_uid;
    }

    public String getStar_image() {
        return star_image;
    }

    public void setStar_image(String star_image) {
        this.star_image = star_image;
    }

    public String getStar_pricing() {
        return star_pricing;
    }

    public void setStar_pricing(String star_pricing) {
        this.star_pricing = star_pricing;
    }

    public String getStar_name() {
        return star_name;
    }

    public void setStar_name(String star_name) {
        this.star_name = star_name;
    }

    public String getClient_uid() {
        return client_uid;
    }

    public void setClient_uid(String client_uid) {
        this.client_uid = client_uid;
    }

    public String getClient_image() {
        return client_image;
    }

    public void setClient_image(String client_image) {
        this.client_image = client_image;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_phoneNumber() {
        return client_phoneNumber;
    }

    public void setClient_phoneNumber(String client_phoneNumber) {
        this.client_phoneNumber = client_phoneNumber;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }
}
