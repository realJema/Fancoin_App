package com.jema.fancoin.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Post.class}, version  = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AllDao allDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {

        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Fancoin_DB")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }
}