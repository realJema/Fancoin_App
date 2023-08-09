package com.jema.fancoin;

import static android.content.Context.MODE_PRIVATE;
import static com.jema.fancoin.Home.LANG;
import static com.jema.fancoin.Home.SHARED_PREFS;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {

    private Context ct;
    private SharedPreferences sharedPreferences;
    public LanguageManager(Context ctx) {
        ct = ctx;
        sharedPreferences = ct.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    public void updateResource(String languageCode){
        Locale locale = new Locale(languageCode);

        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setLang(languageCode);
    }

    public String getLang() {
        return sharedPreferences.getString(LANG, "en");
    }
    public void setLang(String code) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANG, code);
        editor.commit();
    }

}
