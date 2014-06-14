package ru.msinchevskaya.collageapplication.app.users;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.msinchevskaya.collageapplication.app.R;
import ru.msinchevskaya.collageapplication.app.auth.Account;

/**
 * Created by Мария on 13.06.2014.
 */
public class Search {

    private final Context mContext;
    private final String apiVersion;
    private String searchString;
    private OnSearchCompleteListener mListener;

    public interface OnSearchCompleteListener{
        public void searchComplete(List<User> listUsers);
        public void usersNotFound();
    }

    public Search(Context context,OnSearchCompleteListener listener){
        if (context == null)
            throw new NullPointerException("Context cannot be null");
        if (listener == null)
            throw  new NullPointerException("SearchListener cannot be null");
        mContext = context;

        mListener = listener;
        apiVersion = mContext.getString(R.string.instagram_api);
    }

    public void searchUser(String userName){
        searchString = apiVersion + "/users/search?q=" + userName + "&access_token=" + Account.getInstance(mContext).getToken();
        new SearchAsyncTask().execute();
    }

    private class SearchAsyncTask extends AsyncTask<Void, List<User>, List<User>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            List<User> listUsers = new ArrayList<User>();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(searchString);
            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                String response = httpclient.execute(httpget, responseHandler);
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                Log.i("Auth", ""+response);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String id = obj.getString("id");
                    String username = obj.getString("username");
                    User user = new User(id, username);
                    listUsers.add(user);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpclient.getConnectionManager().shutdown();
            }
            return listUsers;
        }

        @Override
        protected void onPostExecute(List<User> listUser) {
            if (listUser.isEmpty())
                mListener.usersNotFound();
            else
                mListener.searchComplete(listUser);
            super.onPostExecute(listUser);
        }
    }
}
