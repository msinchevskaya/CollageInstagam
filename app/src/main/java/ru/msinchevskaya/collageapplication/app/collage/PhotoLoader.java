package ru.msinchevskaya.collageapplication.app.collage;

import android.content.Context;
import android.os.AsyncTask;

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
 * Created by Мария on 14.06.2014.
 */
public class PhotoLoader {

    private final Context mContext;
    private final OnPhotoLoaderCallback mCallback;
    private String urlString;

    public interface OnPhotoLoaderCallback{
        public void photoLoaded(List<Photo> listPhoto);
        public void photoNotLoaded();
    }

    public PhotoLoader(Context context, OnPhotoLoaderCallback callback){
        mContext = context;
        mCallback = callback;
    }

    public void loadPhoto(String userId){
        urlString = mContext.getString(R.string.instagram_api) + "/users/" + userId + "/media/recent/?access_token=" + Account.getInstance(mContext).getToken();
        new PhotoLoaderAsyncTask().execute();
    }

    class PhotoLoaderAsyncTask extends AsyncTask<Void, List<Photo>, List<Photo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Photo> doInBackground(Void... voids) {
            final List<Photo> listPhoto = new ArrayList<Photo>();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(urlString);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                String response = httpclient.execute(httpget, responseHandler);
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject imageJson = jsonArray.getJSONObject(i).getJSONObject("images").getJSONObject("thumbnail");
                    int width = imageJson.getInt("width");
                    int height = imageJson.getInt("height");
                    String url = imageJson.getString("url");
                    Photo photo = new Photo(width, height, url);
                    listPhoto.add(photo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listPhoto;
        }

        @Override
        protected void onPostExecute(List<Photo> listPhoto) {
            if (listPhoto.isEmpty())
                mCallback.photoNotLoaded();
            else
                mCallback.photoLoaded(listPhoto);
            super.onPostExecute(listPhoto);
        }
    }
}
