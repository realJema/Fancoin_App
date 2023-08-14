package com.jema.fancoin.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private final LiveData<User> userInfo;

    private AppDatabase appDatabase;

    public PostViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDbInstance(this.getApplication());

        userInfo = appDatabase.allDao().getUserInfo();
    }


    public LiveData<Post> getAllPosts() {
        return getAllPosts();
    }


    public void insertPosts(List<Post> allposts) {
        new updatePostsAsyncTask(appDatabase).execute(allposts);
    }
    public Boolean check4Post(String id) {
        try {
            List<User> postExist = appDatabase.allDao().check4Post(id);
            if(postExist.size() != 0){
                return true;
            }
            return false;
        } catch (Exception e){
            return false;
        }
    }


    private static class updatePostsAsyncTask extends AsyncTask<User, Void, Void> {

        private AppDatabase db;

        updatePostsAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        public void execute(List<Post> allposts) {
            db.allDao().insertPosts(allposts);
        }

        @Override
        protected Void doInBackground(User... users) {
            return null;
        }
    }
}
