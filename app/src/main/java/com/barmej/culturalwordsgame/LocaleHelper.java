package com.barmej.culturalwordsgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleHelper {
    public static Context setLocale(Context context, String language){
        return updateResourcesLegacy(context, language);
    }

    @SuppressLint("NewApi")
    private static Context updateResourcesLegacy(Context context, String language){
        Locale locale = new Locale(language);
        locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}
