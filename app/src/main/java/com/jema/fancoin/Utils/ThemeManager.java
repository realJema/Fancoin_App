package com.jema.fancoin.Utils;

import static android.content.Context.MODE_PRIVATE;
import static com.jema.fancoin.Home.SHARED_PREFS;
import static com.jema.fancoin.Home.THEME;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private Context ct;
    private SharedPreferences sharedPreferences;
    public ThemeManager(Context ctx) {
        ct = ctx;
        sharedPreferences = ct.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    public void updateTheme(String theme){
        if(theme.equalsIgnoreCase("1")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else if(theme.equalsIgnoreCase("2")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        }
        saveTheme(theme);
    }

    public String getTheme() {
        return sharedPreferences.getString(THEME, "3");
    }

    public void saveTheme(String code) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(THEME, code);
        editor.commit();
    }


}
