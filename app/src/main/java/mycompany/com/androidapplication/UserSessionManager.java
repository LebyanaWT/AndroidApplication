package mycompany.com.androidapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by MANDELACOMP2 on 2017/12/12.
 */

public class UserSessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PREF_MODE = 0;

    public static final String PREF_NAME = "lebyanawt@gmail.com";
    public static final String IS_USER_LOGGED_IN = "IsUserLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "name";

    public UserSessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PREF_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String email,String name){
        editor.putBoolean(IS_USER_LOGGED_IN,true);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.commit();
    }

//    public boolean checkLogin(){
//
//        if(!this.IsUserLoggedIn()){
//            Intent login = new Intent(context,loginActivity.class);
//            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(login);
//            return true;
//        }
//        return false;
//    }

    public HashMap<String,Object> getUserDetails(){
        HashMap<String,Object> user = new HashMap<String ,Object>();
        user.put(KEY_NAME,pref.getString(KEY_NAME,null));
        user.put(KEY_EMAIL,pref.getString(KEY_EMAIL,null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent login = new Intent(context,loginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(login);
    }

}
