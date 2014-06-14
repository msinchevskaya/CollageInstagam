package ru.msinchevskaya.collageapplication.app.users;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import ru.msinchevskaya.collageapplication.app.R;

/**
 * Created by Мария on 14.06.2014.
 */
public class UsersDialog extends Dialog {

    private final Context mContext;
    private final List<User> listUsers;
    private OnUserSelectionLestener mListener;

    public interface OnUserSelectionLestener{
        public void onUserSelected(User user);
    }

    public UsersDialog(Context context, List<User> listUsers, OnUserSelectionLestener listener){
        super(context);
        mContext = context;
        this.listUsers = listUsers;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_users);
        ListView list = (ListView) findViewById(R.id.lv_users);
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(mContext, android.R.layout.simple_list_item_1, listUsers);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Отправляем ссылку на выбранного пользователя
                mListener.onUserSelected(listUsers.get(position));
                dismiss();
            }
        });
    }


}
