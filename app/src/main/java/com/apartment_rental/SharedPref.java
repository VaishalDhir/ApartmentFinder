package com.apartment_rental;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public SharedPref(Context con)
    {
        sharedPref = con.getSharedPreferences("Apartment_rental", Context.MODE_PRIVATE);
        editor=sharedPref.edit();
    }
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
