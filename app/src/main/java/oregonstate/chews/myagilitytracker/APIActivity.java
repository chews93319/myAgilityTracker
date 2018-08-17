package oregonstate.chews.myagilitytracker;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import oregonstate.chews.myagilitytracker.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class APIActivity extends AppCompatActivity {

    private AuthorizationService mAuthorizationService;
    private AuthState mAuthState;
    private OkHttpClient mOkHttpClient;

    //Create a Log TAG [ref: 1]
    private static final String TAG = "APIActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences authPreference = getSharedPreferences("auth", MODE_PRIVATE);
        setContentView(R.layout.activity_api);
        mAuthorizationService = new AuthorizationService(this);
        ((Button)findViewById(R.id.btngplus_getpost)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    mAuthState.performActionWithFreshTokens(mAuthorizationService, new AuthState.AuthStateAction(){
                        @Override
                        public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException e) {
                            if (e == null){
                                mOkHttpClient = new OkHttpClient();
                                HttpUrl reqUrl = HttpUrl.parse("https://www.googleapis.com/plusDomains/v1/people/me/activities/user");
                                Request request = new Request.Builder()
                                        .url(reqUrl)
                                        .addHeader("Authorization", "Bearer " + accessToken)
                                        .build();
                                mOkHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String r = response.body().string();
                                        Log.d(TAG, r);

                                        try {
                                            JSONObject j = new JSONObject(r);
                                            JSONArray items = j.getJSONArray("items");

                                            List<Map<String,String>> posts = new ArrayList<>();
                                            for(int i = 0; i < items.length(); i++){
                                                HashMap<String, String> m = new HashMap<>();
                                                m.put("published", items.getJSONObject(i).getString("published"));
                                                m.put("title",items.getJSONObject(i).getString("title"));
                                                if (i < 3){
                                                    posts.add(m);
                                                }
                                            }
                                            final SimpleAdapter postAdapter = new SimpleAdapter(
                                                    APIActivity.this,
                                                    posts,
                                                    R.layout.google_plus_item,
                                                    new String[]{"published", "title"},
                                                    new int[]{R.id.google_plus_item_date_text, R.id.google_plus_item_text});
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ((ListView)findViewById(R.id.google_post_list)).setAdapter(postAdapter);
                                                }
                                            });
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });

                            }
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        ((Button)findViewById(R.id.btngplus_user)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    mAuthState.performActionWithFreshTokens(mAuthorizationService, new AuthState.AuthStateAction(){
                        @Override
                        public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException e) {
                            if (e == null){
                                mOkHttpClient = new OkHttpClient();
                                HttpUrl reqUrl = HttpUrl.parse("https://www.googleapis.com/plusDomains/v1/people/me");
                                Request request = new Request.Builder()
                                        .url(reqUrl)
                                        .addHeader("Authorization", "Bearer " + accessToken)
                                        .build();
                                mOkHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String r = response.body().string();
                                        Log.d(TAG, r);

                                        try {
                                            JSONObject j = new JSONObject(r);
                                            List<Map<String,String>> posts = new ArrayList<>();
                                            HashMap<String, String> m = new HashMap<>();

                                            m.put("published", j.getString("id"));  //u_id
                                            m.put("title",j.getString("displayName"));  //u_name
                                            posts.add(m);

                                            final SimpleAdapter postAdapter = new SimpleAdapter(
                                                    APIActivity.this,
                                                    posts,
                                                    R.layout.google_plus_item,
                                                    new String[]{"published", "title"},
                                                    new int[]{R.id.google_plus_item_date_text, R.id.google_plus_item_text});
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ((ListView)findViewById(R.id.google_post_list)).setAdapter(postAdapter);
                                                }
                                            });
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });

                            }
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        ((Button)findViewById(R.id.btnPost)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    mAuthState.performActionWithFreshTokens(mAuthorizationService, new AuthState.AuthStateAction(){
                        @Override
                        public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException e) {
                            if (e == null){
                                String postContent = ((EditText)findViewById(R.id.inputPost)).getText().toString();
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                                mOkHttpClient = new OkHttpClient();
                                HttpUrl reqUrl = HttpUrl.parse("https://www.googleapis.com/plusDomains/v1/people/me/activities");

                                String msg = ((EditText)findViewById(R.id.inputPost)).getText().toString();
                                JSONObject myJ = new JSONObject();
                                JSONObject myMsg = new JSONObject();
                                JSONObject myAccess = new JSONObject();
                                JSONArray myItems = new JSONArray();
                                JSONObject myItem = new JSONObject();
                                try {
                                    myMsg.put("content",msg);
                                    myItem.put("type","domain");
                                    myItems.put(myItem);
                                    myAccess.put("items",myItems);
                                    myAccess.put("domainRestricted",true);

                                    myJ.put("object",myMsg);
                                    myJ.put("access",myAccess);

                                    Log.d(TAG, myJ.toString());
                                } catch (JSONException e2) {
                                    e2.printStackTrace();
                                }

                                RequestBody mBody = RequestBody.create(JSON, myJ.toString());
                                Request request = new Request.Builder()
                                        .url(reqUrl)
                                        .addHeader("Authorization", "Bearer " + accessToken)
                                        .post(mBody)
                                        .build();
                                mOkHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String r = response.body().string();
                                        Log.d(TAG, r);
                                    }
                                });
                            }
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }




/*
                String myPost(String url, String json) throws IOException {
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    Response response = mOkHttpClient.newCall(request).execute();
                    return response.body().string();
                }
*/
                ((EditText)findViewById(R.id.inputPost)).setText("");
            }
        });

    };

    @Override
    protected void onStart() {
        mAuthState = getOrCreateAuthState();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        mAuthorizationService.dispose();
        super.onDestroy();
    }

    //   Function will check if we have a good authorization
    AuthState getOrCreateAuthState(){
        AuthState auth = null;
        //   This is where settings for the app are stored for long term purposes
        SharedPreferences authPreference = getSharedPreferences("auth", MODE_PRIVATE);
        String stateJson = authPreference.getString("stateJson",null);

        if(stateJson != null) {  //  if a key is already present
            try {
                // AuthState will create a new 'authorization state'
                auth = AuthState.jsonDeserialize(stateJson);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        //  if the auth contains and AccessToken, then return the auth, else try to get a new one
        if (auth != null && auth.getAccessToken() != null){
            return auth;
        } else {
            updateAuthState();
            return null;
        }
    }

    //   Function
    void updateAuthState(){
        Uri authEndpoint = new Uri.Builder().scheme("https").authority("accounts.google.com").path("/o/oauth2/v2/auth").build();
        Uri tokenEndpoint = new Uri.Builder().scheme("https").authority("www.googleapis.com").path("/oauth2/v4/token").build();

        // Include path("foo") to trick Uri.Builder() to append a '\foo' to the scheme due to a
        //    bug in how GoogleAPI handles only a scheme versus how the library handles only a scheme.
        //    The appended '\foo' forces a critical '\' to be appended
        Uri redirect = new Uri.Builder().scheme("oregonstate.chews.myagilitytracker").path("foo").build();

        // config controls how the AuthorizationRequest will work
        AuthorizationServiceConfiguration config = new AuthorizationServiceConfiguration(authEndpoint, tokenEndpoint, null);
        AuthorizationRequest req = new AuthorizationRequest.Builder(config, "403888094196-iptjjltckhbpu7b6r2v0lknv7p0a2b1b.apps.googleusercontent.com", ResponseTypeValues.CODE, redirect)
                .setScopes("https://www.googleapis.com/auth/plus.me", "https://www.googleapis.com/auth/plus.stream.write", "https://www.googleapis.com/auth/plus.stream.read")
                .build();

        Intent authComplete = new Intent(this, AuthCompleteActivity.class);
        mAuthorizationService.performAuthorizationRequest(req, PendingIntent.getActivity(this, req.hashCode(),authComplete, 0));
    }
}
