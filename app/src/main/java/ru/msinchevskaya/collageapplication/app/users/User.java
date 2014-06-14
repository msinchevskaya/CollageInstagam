package ru.msinchevskaya.collageapplication.app.users;

import android.util.Log;

/**
 * Created by Мария on 13.06.2014.
 */
public class User {

    private final String id;
    private final String username;

    public User(String id,
                String username){
        this.id = id;
        this.username = username;
        Log.i("User", "id = "  + id);
        Log.i("User", "username = "  + username);
    }

    public String getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public String toString() {
        return username;
    }
}
