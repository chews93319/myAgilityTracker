package oregonstate.chews.myagilitytracker;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import org.json.JSONException;
import org.w3c.dom.Text;


public class APIActivity extends AppCompatActivity {

    private AuthorizationService mAuthorizationService;
    private AuthState mAuthState;
    //private OkHttpClient mOkHttpClient;


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
                            ((TextView)findViewById(R.id.textAccessToken)).setText(accessToken);
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    };

    @Override
    protected void onStart() {
        mAuthState = getOrCreateAuthState();
        super.onStart();
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
