package com.jema.fancoin.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final LiveData<User> userInfo;

    private AppDatabase appDatabase;

    public UserViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDbInstance(this.getApplication());

        userInfo = appDatabase.allDao().getUserInfo();
    }


    public LiveData<User> getUserInfo() {
        return userInfo;
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

    public void updateUser(String uname, String ufullname, String uemail) {
        new updateUserAsyncTask(appDatabase).execute(uname, ufullname, uemail);
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

        public void execute(String uname, String ufullname, String uemail) {
            db.allDao().updateUser(uname, ufullname, uemail);
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
