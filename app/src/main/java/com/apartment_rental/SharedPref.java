package com.apartment_rental;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String FIRSTNAME="firstname";
    private static final String LASTNAME="firstname";
    private static final String ISLOGIN="firstname";
    private static final String CONTACTNUMBER="firstname";
    private static final String USERID="userid";
    private static final String USERTYPE="type";



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

    public void setFirstname(String firstname){
        editor.putString("fname",firstname);
        editor.commit();
    }
    public String getFirstname(){
        return sharedPref.getString("fname","");
    }
    public void setLastname(String lastname){
        editor.putString(LASTNAME,lastname);
        editor.commit();
    }
    public String getLastname(){
        return sharedPref.getString(LASTNAME,"");

    }
    public void setContactnumber(String number){
        editor.putString(CONTACTNUMBER,number);
        editor.commit();
    }
    public String getContactnumber(){
        return sharedPref.getString(CONTACTNUMBER,"");
    }

    public void setUserId(int userId){
        editor.putInt(USERID,userId);
        editor.commit();
    }
    public int getUserid(){
        return sharedPref.getInt(USERID,0);


    }



    public void setType(String type){
        editor.putString(USERTYPE,type);
        editor.commit();
    }
    public String getType(){
        return sharedPref.getString(USERTYPE,"");


    }


//
//    public void setIslogin(boolean isFirstTime) {
//        editor.putBoolean(ISLOGIN, isFirstTime);
//        editor.commit();
//    }
//
//    public boolean isLogin() {
//        return sharedPref.getBoolean(ISLOGIN, true);
//    }

    public  void clearPreferences(){
        setFirstname("");
        setUserId(0);
        setType("");
        //setIslogin(false);

    }

}
