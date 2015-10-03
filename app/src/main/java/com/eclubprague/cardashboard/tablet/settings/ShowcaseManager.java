package com.eclubprague.cardashboard.tablet.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.eclubprague.cardashboard.core.application.GlobalDataProvider;

/**
 * Created by Michael on 23.09.2015.
 */
public class ShowcaseManager {

    private static final String PREF_FILE = ShowcaseManager.class.getName();
    private static SharedPreferences preferences;


    public static void putBoolean(String key, boolean value){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key){
        return getPreferences().getBoolean(key, false);
    }

    private static SharedPreferences getPreferences(){
        if(preferences == null){
            preferences = GlobalDataProvider.getInstance().getActivity().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        }
        return preferences;
    }
}
