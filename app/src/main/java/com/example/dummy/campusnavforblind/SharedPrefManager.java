package com.example.dummy.campusnavforblind;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "deviceToken";

    private static SharedPrefManager sharedPrefManager;
    private static Context cntx;

    private SharedPrefManager(Context context) {
        cntx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (sharedPrefManager == null) {
            sharedPrefManager = new SharedPrefManager(context);
        }
        return sharedPrefManager;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = cntx.getSharedPreferences("FCMSharedPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("deviceToken", token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = cntx.getSharedPreferences("FCMSharedPreference", Context.MODE_PRIVATE);
        return  sharedPreferences.getString("deviceToken", null);
    }
}
