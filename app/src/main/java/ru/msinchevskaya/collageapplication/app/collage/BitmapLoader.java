package ru.msinchevskaya.collageapplication.app.collage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Мария on 14.06.2014.
 */
public class BitmapLoader {

    private Photo photo;
    private ImageView iv;

    public void loadBitmapFromNetwork(Photo photo,ImageView iv){
        this.photo = photo;
        this.iv = iv;
        new BitmapLoaderAsyncTask().execute();
    }



    private class  BitmapLoaderAsyncTask extends AsyncTask<Void, Bitmap, Bitmap>{
        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(photo.getUrl()).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null){
                //Задаем поле bitmap и сразу добавляем картинку в imageView
                photo.setBitmap(bitmap);
                if (iv!= null)
                    iv.setImageBitmap(bitmap);
            }
            super.onPostExecute(bitmap);
        }
    }

}
