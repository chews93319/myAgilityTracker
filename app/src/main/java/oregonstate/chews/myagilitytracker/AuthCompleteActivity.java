package oregonstate.chews.myagilitytracker;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.TokenResponse;


//  Handles the exchanging of the AccessCode for a Token and saving the AuthState
public class AuthCompleteActivity extends AppCompatActivity {

    private AuthorizationService mAuthorizationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_complete);

        mAuthorizationService = new AuthorizationService(this);
//        Uri redirectUri = getIntent().getData();

        //   These are specific classes from the OAuth Library
        AuthorizationResponse resp = AuthorizationResponse.fromIntent(getIntent());
        AuthorizationException ex = AuthorizationException.fromIntent(getIntent());

        if(resp != null){
            final AuthState authState = new AuthState(resp, ex);
            mAuthorizationService.performTokenRequest(resp.createTokenExchangeRequest(),
                    new AuthorizationService.TokenResponseCallback() {
                        // When this TokenResponseCallback is complete, the current AuthState will
                        //    be shared in SharedPreferences
                        @Override
                        public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException e) {
                            authState.update(tokenResponse, e);
                            SharedPreferences authPreferences = getSharedPreferences("auth", MODE_PRIVATE);
                            authPreferences.edit().putString("stateJson", authState.jsonSerializeString()).apply();
                            finish();
                        }
                    });
        }
    }
}
