package ru.msinchevskaya.collageapplication.app.activities;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.List;

        import ru.msinchevskaya.collageapplication.app.R;
        import ru.msinchevskaya.collageapplication.app.auth.Account;
        import ru.msinchevskaya.collageapplication.app.auth.Auth;
        import ru.msinchevskaya.collageapplication.app.users.Search;
        import ru.msinchevskaya.collageapplication.app.users.User;
        import ru.msinchevskaya.collageapplication.app.users.UsersDialog;


public class EnterActivity extends Activity{

    private Context mContext;
    private Auth mAuth;
    private Search mSearch;
    private String searchUserName;
    private TextView tvAccountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        mContext = this;

        final Account account = Account.getInstance(this);
        mAuth = new Auth(mContext);
        mSearch = new Search(mContext, searchListener);

        tvAccountName = ((TextView)findViewById(R.id.tvAccountName));

        if (account.getUserName() != null)
            //Отображаем авторизованного пользователя в правом верхнем углу
            tvAccountName.setText(account.getUserName());
        mAuth.setAuthListener(authListener);
    }

    public void onCollageClick(View view) {
        TextView tv = (TextView) findViewById(R.id.tvUserName);
        searchUserName = tv.getText().toString();
        if (searchUserName.length() <= 0)
            Toast.makeText(getApplicationContext(), getString(R.string.empty_user_name), Toast.LENGTH_LONG).show();
        else {
            mAuth.authorize();
        }
    }

    //слушатель событий авторизации
    private Auth.AuthListener authListener = new Auth.AuthListener() {
        @Override
        public void onSuccess() {
            //Сразу после авторизации начинаем поиск по запросу
            mSearch.searchUser(searchUserName);
        }

        @Override
        public void onFail() {
            Toast.makeText(mContext, getString(R.string.auth_fail), Toast.LENGTH_LONG).show();
        }
    };

    //Слушатель событий поиска пользователя по имени
    private Search.OnSearchCompleteListener searchListener = new Search.OnSearchCompleteListener() {
        @Override
        public void searchComplete(List<User> listUsers) {
            if (listUsers.size() == 1)
                //Если один пользователь выдан по запросу - сразу открываем новое активити
                startPhotoActivity(listUsers.get(0));
            else
                //Если больше - предлагаем уточнить, ког опользователь имел ввиду
                startChangeUserList(listUsers);
        }

        @Override
        public void usersNotFound() {
            Toast.makeText(mContext, getString(R.string.user_not_found), Toast.LENGTH_LONG).show();
        }
    };

    void startPhotoActivity(User user){
        Intent intent = new Intent(mContext, PhotoActivity.class);
        //Передаем имя пользователя и Id через intent
        intent.putExtra(getString(R.string.user_id_key), user.getId());
        intent.putExtra(getString(R.string.user_name_key), user.getUsername());
        startActivity(intent);
    }

    void startChangeUserList(List<User> listUsers){
        UsersDialog usersDialog = new UsersDialog(mContext, listUsers, userListener);
        usersDialog.setTitle(getString(R.string.choice_user));
        usersDialog.show();
    }

    private UsersDialog.OnUserSelectionLestener userListener = new UsersDialog.OnUserSelectionLestener() {
        @Override
        public void onUserSelected(User user) {
            startPhotoActivity(user);
        }
    };
}
