package com.example.foodplanner.db;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "MyPrefs";
    private static final String FAVORITE_KEY = "isMealFavorite";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveFavoriteStatus(Context context, boolean isFavorite, String mealId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(FAVORITE_KEY + mealId, isFavorite);
        editor.apply();
    }

    public static boolean loadFavoriteStatus(Context context, String mealId) {
        return getSharedPreferences(context).getBoolean(FAVORITE_KEY + mealId, false);
    }


    public static void saveUserEmail(Context context,String email){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("email", email);
        editor.apply();
    }

    public static String getUserEmail(Context context){
        return getSharedPreferences(context).getString("email","");
    }


}
