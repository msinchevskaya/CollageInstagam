package ru.msinchevskaya.collageapplication.app.collage;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import ru.msinchevskaya.collageapplication.app.R;

/**
 * Created by Мария on 14.06.2014.
 */
public class PhotoAdapter<Photo> extends ArrayAdapter<ru.msinchevskaya.collageapplication.app.collage.Photo> {

    private final Context mContext;
    private final LayoutInflater inflater;


    public PhotoAdapter(Context context, int resId, List<ru.msinchevskaya.collageapplication.app.collage.Photo> list) {
        super(context, resId, list);
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ru.msinchevskaya.collageapplication.app.collage.Photo photo = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_photo, null);

            final ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);

            if (photo.getBitmap() == null) {
                //Если нет картинки, загружаем из сети
                new BitmapLoader().loadBitmapFromNetwork(photo, ivPhoto);

            }
        }

        final ImageView ivCheck = (ImageView)convertView.findViewById(R.id.iv_check);
            if (!photo.isChecked())
                ivCheck.setVisibility(View.INVISIBLE);
            else
                ivCheck.setVisibility(View.VISIBLE);
        return convertView;
    }

}
