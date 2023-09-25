package com.jema.fancoin.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final LiveData<User> userInfo;
    private final User userinfonormal;

    private AppDatabase appDatabase;

    public UserViewModel(Application application) {
        super(application);


        appDatabase = AppDatabase.getDbInstance(this.getApplication());

        userInfo = appDatabase.allDao().getUserInfo();
        userinfonormal = appDatabase.allDao().getUserInfoNormal();
    }


    public LiveData<User> getUserInfo() {
        return userInfo;
    }
    public User getUser(){
        return userinfonormal;
    }

    public String getApplicationStatus() {
        return userinfonormal.application_status;
    }

    public Boolean check4User() {
        try {
            List<User> userExist = appDatabase.allDao().check4User();
            if(userExist.size() != 0){
                return true;
            }
            return false;
        } catch (Exception e){

            return false;
        }
    }

    public void delete(User user) {
        new deleteAsyncTask(appDatabase).execute(user);
    }

    public void updateUsername(String username) {
        new updateNameAsyncTask(appDatabase).execute(username);
    }

    public void updateImage(String image) {
        new updateImageAsyncTask(appDatabase).execute(image);
    }

    public void updateUser(String username, String full_name, String application_status, String category, String email, String bio, String image, String phone, String pricing, String myfollo, String myfolli){
        new updateUserAsyncTask(appDatabase).execute(username, full_name, application_status, category, email, bio, image, phone, pricing, myfollo, myfolli);
    }

    private static class updateNameAsyncTask extends AsyncTask<User, Void, Void> {

        private AppDatabase db;

        updateNameAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        public void execute(String username) {
            db.allDao().updateImage(username);
        }

        @Override
        protected Void doInBackground(User... users) {
            return null;
        }
    }
    private static class updateImageAsyncTask extends AsyncTask<User, Void, Void> {

        private AppDatabase db;

        updateImageAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        public void execute(String image) {
            db.allDao().updateImage(image);
        }

        @Override
        protected Void doInBackground(User... users) {
            return null;
        }
    }
    private static class updateUserAsyncTask extends AsyncTask<User, Void, Void> {

        private AppDatabase db;

        updateUserAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final User... params) {
            db.allDao().delete(params[0]);
            return null;
        }

        public void execute(String username, String full_name, String application_status, String category, String email, String bio, String image, String phone, String pricing, String myfollo, String myfolli) {
            db.allDao().updateUser(username, full_name, application_status, category, email, bio, image, phone, pricing, myfollo, myfolli);
        }
    }
    private static class deleteAsyncTask extends AsyncTask<User, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final User... params) {
            db.allDao().delete(params[0]);
            return null;
        }

    }
}
