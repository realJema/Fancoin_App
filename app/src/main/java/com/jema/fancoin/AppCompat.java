package com.jema.fancoin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AppCompat extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageManager lang = new LanguageManager(this);
        ThemeManager theme = new ThemeManager(this);

        lang.updateResource(lang.getLang());
        theme.updateTheme(theme.getTheme());
    }
}
