package ru.msinchevskaya.collageapplication.app.collage;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by Мария on 14.06.2014.
 */
public class Photo {

    private final int width;
    private final int height;
    private final String url;
    private boolean isChecked;
    private Bitmap picture;

    public Photo(int width, int height, String url){
        this.width = width;
        this.height = height;
        this.url = url;

        Log.i("Photo", "width = " + width + " height = " + height + " url = " + url);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public String getUrl(){
        return url;
    }

    public boolean isChecked(){
        return isChecked;
    }

    public Bitmap getBitmap(){
        return picture;
    }

    public void setChecked(boolean isChecked){
        this.isChecked = isChecked;
    }

    public void setBitmap(Bitmap bitmap){
        picture = bitmap;
    }

    @Override
    public String toString() {
        return url;
    }
}
