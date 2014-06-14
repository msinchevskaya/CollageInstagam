package ru.msinchevskaya.collageapplication.app.auth;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ru.msinchevskaya.collageapplication.app.R;

/**
 * Created by Мария on 13.06.2014.
 */
public class Auth implements AuthDialog.AuthDialogListener{

    private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKENURL = "https://api.instagram.com/oauth/access_token";

    private Account account;
    private final Context mContext;
    private AuthListener mListener;

    private String clientId;
    private String clientSecret;
    private String redirectUrl;

    private String mToken;

    public Auth(Context context){
        if (context == null)
            throw  new NullPointerException("Context cannot be null");
        mContext = context;
        account = Account.getInstance(mContext);
    }

    public interface AuthListener{
        public void onSuccess();
        public void onFail();
    }

    public void setAuthListener(AuthListener listener){
        mListener = listener;
    }

    public void authorize(){
        clientId = mContext.getString(R.string.client_id);
        clientSecret = mContext.getString(R.string.client_secret);
        redirectUrl = mContext.getString(R.string.redirect_url);

        if (account.getToken() !=  null) {//Успешная авторизация
            if (mListener != null)
                //Если есть параметры аккаунта - авторизация совершена
                mListener.onSuccess();
        }
        else {
            String url = AUTHURL + "?client_id=" + clientId + "&redirect_uri=" + redirectUrl + "&response_type=code&display=touch&scope=likes+comments+relationships";
            AuthDialog dialog = new AuthDialog(mContext, url, this);
            dialog.setTitle(mContext.getString(R.string.auth));
            dialog.show();
        }
    }

    @Override
    public void onComplete(String token) {
        Log.i("Auth", "Auth complete, token = " + token);
        mToken = token;
        new AuthTokenAsyncTask().execute();
    }

    @Override
    public void onError(String error) {
        Log.i("Auth", "onError");
    }

    class AuthTokenAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String tokenString = TOKENURL + "?client_id=" + clientId + "&client_secret=" + clientSecret + "&redirect_uri=" + redirectUrl + "&grant_type=authorization_code";
            try {
                URL url = new URL(tokenString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write("client_id="+clientId+
                        "&client_secret="+ clientSecret +
                        "&grant_type=authorization_code" +
                        "&redirect_uri="+ redirectUrl +
                        "&code=" + mToken);

                outputStreamWriter.flush();

                //Преобразуем потом в строку
                String response = streamToString(httpsURLConnection.getInputStream());

                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                String token = jsonObject.getString("access_token");

                JSONObject userJsonObject = jsonObject.getJSONObject("user");

                //User ID
                String id = userJsonObject.getString("id");
                //Username
                String username = userJsonObject.getString("username");
                //User full name
                String fullname = userJsonObject.getString("full_name");

                //Записываем параметры аккаунта
                account.setAccountParams(id , token, username, fullname );
                mListener.onSuccess();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public String streamToString(InputStream is) throws IOException {
        String string = "";

        if (is != null) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            string = stringBuilder.toString();
        }

        return string;
    }
}
