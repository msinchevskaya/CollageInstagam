package ru.msinchevskaya.collageapplication.app.collage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

/**
 * Created by Мария on 14.06.2014.
 */
public final class CollageMaker {

    private CollageMaker(){};

    /**
     *
     * @param photos - непустой массив массив Bitmap
     * @return Bitmap - получившийся коллаж
     * Создает коллаж из массива картинок
     */
    public static Bitmap makeCollage(List<Bitmap> photos){
        if (photos.isEmpty())
            throw new IllegalArgumentException("Number of photos must be > 0");

        Bitmap bitmap = photos.get(0);

        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();
        Log.i("Collage", "bitmapWidth = " + bitmapWidth);
        Log.i("Collage", "bitmapHeight = " + bitmapHeight);

        final int canvasWidth;
        final int canvasHeight;

        //Считаем ширину и высоту канвы
        if (photos.size() == 1) {
            canvasWidth = bitmap.getWidth();
            canvasHeight = bitmap.getHeight();
        }
        else {
            canvasWidth = bitmap.getWidth() * 2;
            canvasHeight = bitmap.getHeight() * ((photos.size() + 1) / 2);
        }

        final Bitmap collage = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(collage);
        canvas.drawColor(Color.WHITE);

        final Paint paint = new Paint();

        int i = 0;
        for (Bitmap photo : photos){
            //Делаем коллаж из двух столбцов фотографий
            //Считаем координаты левого верзнего угла каждой фотографии
            int left = (i % 2) * bitmapWidth;
            int top = (i / 2) * bitmapHeight;
            Log.i("Collage", " i = " + i +  "left = " + left + "top = " + top);
            canvas.drawBitmap(photo, left, top, paint);
            i++;
        }
        return collage;
    }

}
