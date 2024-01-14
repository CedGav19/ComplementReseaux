package com.example.mymarket.controller;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LanguageController {
    public static void changeLanguage(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        // Changer la locale en français si elle est actuellement en anglais, et vice versa
        if (configuration.locale.getLanguage().equals("en")) {
            setLocale(context,"fr");
        } else {
            setLocale(context,"en");
        }
    }

    private static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        // Enregistrer la langue sélectionnée dans les préférences partagées (si nécessaire)
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("language", languageCode);
        editor.apply();
    }
}
