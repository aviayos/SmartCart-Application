package com.example.orella.smartcart;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesConfig {

    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferencesConfig(Context context)
    {
        this.context = context;

        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference),Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.meta_preference),Context.MODE_PRIVATE);

    }

    public void writeLoginStatus(boolean status)
    {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(context.getResources().getString(R.string.login_status_preference),status);

        editor.commit();

    }

    public boolean readLoginStatus()
    {
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preference),false);
        return status;
    }


    public void writeMetaStatus(boolean status)
    {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(context.getResources().getString(R.string.meta_status_preference),status);

        editor.commit();

    }

    public boolean readMetaStatus()
    {
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.meta_status_preference),false);
        return status;
    }

}
