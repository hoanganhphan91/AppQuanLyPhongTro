package com.example.duan1.model;

import android.content.Context;
import android.content.SharedPreferences;


public class SessionManage {
    public static String USERNAME = "username" ;
    public static String PASSWORD = "password";
    public static String NAME = "name" ;
    public static String PHONE = "phone";
    public static String TITLE = "title" ;
    public static String IMAGE = "image";

    private SharedPreferences pref ;
    public SessionManage(Context context) {
        pref = context.getSharedPreferences("account", Context.MODE_PRIVATE);
    }
    public void saveAccount(Account account){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(SessionManage.USERNAME, account.getUsername());
        editor.putString(SessionManage.PASSWORD, account.getPassword());
        editor.putString(SessionManage.NAME, account.getName());
        editor.putString(SessionManage.PHONE, account.getPhone());
        editor.putString(SessionManage.TITLE, account.getTitle());
        editor.putString(SessionManage.IMAGE, account.getImage());
    }

    public Account fetchAccount(){
        Account account = new Account();
        account.setUsername(pref.getString(SessionManage.USERNAME,""));
        account.setPassword(pref.getString(SessionManage.PASSWORD,""));
        account.setName(pref.getString(SessionManage.NAME,""));
        account.setTitle(pref.getString(SessionManage.TITLE,""));
        account.setPhone(pref.getString(SessionManage.PHONE,""));
        account.setImage(pref.getString(SessionManage.IMAGE,""));
        return account;
    }
}
