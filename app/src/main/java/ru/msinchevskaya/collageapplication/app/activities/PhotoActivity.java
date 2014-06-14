package ru.msinchevskaya.collageapplication.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.msinchevskaya.collageapplication.app.collage.CollageDialog;
import ru.msinchevskaya.collageapplication.app.collage.CollageMaker;
import ru.msinchevskaya.collageapplication.app.collage.Photo;
import ru.msinchevskaya.collageapplication.app.collage.PhotoAdapter;
import ru.msinchevskaya.collageapplication.app.collage.PhotoLoader;
import ru.msinchevskaya.collageapplication.app.R;

/**
 * Created by Мария on 13.06.2014.
 */
public class PhotoActivity  extends Activity{

    private PhotoLoader mLoader;
    private Context mContext;
    private GridView gvPhoto;
    private final List<Photo> listPhotos = new ArrayList<Photo>();
    private PhotoAdapter<Photo> photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mContext = this;

        Intent intent = getIntent();
        setTitle(intent.getStringExtra(getString(R.string.user_name_key)));

        gvPhoto = (GridView) findViewById(R.id.gv_photo);
        photoAdapter = new PhotoAdapter<Photo>(mContext, R.layout.adapter_photo, listPhotos);
        gvPhoto.setOnItemClickListener(itemClickListener);
        gvPhoto.setAdapter(photoAdapter);

        mLoader = new PhotoLoader(mContext, photoCallback);
        mLoader.loadPhoto(intent.getStringExtra(getString(R.string.user_id_key)));
    }

    //Слушатель загрузчика данных о фотографиях
    private PhotoLoader.OnPhotoLoaderCallback photoCallback = new PhotoLoader.OnPhotoLoaderCallback() {
        @Override
        public void photoLoaded(List<Photo> listPhoto) {
            listPhotos.clear();
            listPhotos.addAll(listPhoto);
            photoAdapter.notifyDataSetChanged();
        }

        @Override
        public void photoNotLoaded() {
            Toast.makeText(mContext, getString(R.string.photo_not_loaded), Toast.LENGTH_LONG).show();
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Выбираем или снимаем выбор с фотографии
            listPhotos.get(i).setChecked(!listPhotos.get(i).isChecked());
            photoAdapter.notifyDataSetChanged();
        }
    };

    public void onPreviewClick(View view) {
        List<Bitmap> listBitmap = new ArrayList<Bitmap>();
        for (Photo photo : listPhotos){
            if (photo.isChecked())
                listBitmap.add(photo.getBitmap()); //Добавляем картинки выбранных фотографий в массив
        }
        if (listBitmap.size() > 0){
            Bitmap collage = CollageMaker.makeCollage(listBitmap);
            CollageDialog collageDialog = new CollageDialog(mContext, collage);
            collageDialog.setTitle(getString(R.string.preview));
            collageDialog.show();
        }
        else
            Toast.makeText(mContext, getString(R.string.choicePhoto), Toast.LENGTH_LONG).show();
    }
}
