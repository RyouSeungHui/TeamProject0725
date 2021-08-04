package com.example.photosnsproject;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {
    public static final String PREFERENCES_NAME = "rebuild_preference";

    public static final String USER_ID = "USER_ID";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setUserID(Context context, String userID){
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userID);
        editor.commit();
    }
    public static String getUserId(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        String userID = sharedPreferences.getString(USER_ID, null);
        return userID;
    }
}
