package ru.msinchevskaya.collageapplication.app.collage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import ru.msinchevskaya.collageapplication.app.R;

/**
 * Created by Мария on 14.06.2014.
 */
public class CollageDialog extends Dialog {

    private final Context mContext;
    private final Bitmap collage;

    public CollageDialog(Context context, Bitmap collage){
        super(context);
        mContext = context;
        this.collage = Bitmap.createBitmap(collage);
        collage.recycle();
        collage = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_collage);

        ImageView ivCollage = (ImageView) findViewById(R.id.ivCollage);
        ivCollage.setImageBitmap(collage);
        Button btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, mContext.getString(R.string.collage_sended), Toast.LENGTH_LONG).show();
            dismiss();
        }
    };
}
