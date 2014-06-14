package ru.msinchevskaya.collageapplication.app.auth;

import android.content.Context;
import android.content.SharedPreferences;

import ru.msinchevskaya.collageapplication.app.R;

/**
 * Created by Мария on 13.06.2014.
 */
public class Account {
    private static Account ourInstance;

    private static final String ID_KEY = "Id";
    private static final String ACCESS_TOKEN_KEY = "AcessToken";
    private static final String USER_NAME_KEY = "UserName";
    private static final String FULL_NAME_KEY = "FullName";

    private String id;
    private String token;
    private String userName;
    private String fullName;
    private final Context mContext;

    public static Account getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new Account(context);
        return ourInstance;
    }

    private Account(Context context) {
        mContext = context;
        //Получаем данные из настроек - по умолчанию null
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString(R.string.preferences), Context.MODE_PRIVATE);
        id = prefs.getString(ID_KEY, null);
        token = prefs.getString(ACCESS_TOKEN_KEY, null);
        userName = prefs.getString(USER_NAME_KEY, null);
        fullName = prefs.getString(FULL_NAME_KEY, null);

    }

    public String getToken(){
        return  token;
    }

    public String getId(){
        return id;
    }

    public String getUserName(){
        return userName;
    }

    public String getFullName(){
        return fullName;
    }

    void setAccountParams(String id, String token, String userName, String fullName){
        this.id = id;
        this.token = token;
        this.userName = userName;
        this.fullName = fullName;

        //Сохраняем параметры аккаунта
        SharedPreferences.Editor editor = mContext.getSharedPreferences(mContext.getString(R.string.preferences), Context.MODE_PRIVATE).edit();
        editor.putString(ACCESS_TOKEN_KEY, id);
        editor.putString(ACCESS_TOKEN_KEY, token);
        editor.putString(USER_NAME_KEY, userName);
        editor.putString(FULL_NAME_KEY, fullName);
        editor.commit();
    }
}
