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

        userInfo = appDatabase.userDao().getUserInfo();
    }


    public LiveData<User> getUserInfo() {
        return userInfo;
    }

    public Boolean check4User() {
        List<User> userExist = appDatabase.userDao().check4User();
        if(userExist.size() != 0){
            return true;
        }
        return false;
    }

    public void delete(User user) {
        new deleteAsyncTask(appDatabase).execute(user);
    }

    public void updateUser(String uname, String ufullname, String uemail) {
        new updateUserAsyncTask(appDatabase).execute(uname, ufullname, uemail);
    }

    private static class updateUserAsyncTask extends AsyncTask<User, Void, Void> {

        private AppDatabase db;

        updateUserAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final User... params) {
            db.userDao().delete(params[0]);
            return null;
        }

        public void execute(String uname, String ufullname, String uemail) {
            db.userDao().updateUser(uname, ufullname, uemail);
        }
    }
    private static class deleteAsyncTask extends AsyncTask<User, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final User... params) {
            db.userDao().delete(params[0]);
            return null;
        }

    }
}
