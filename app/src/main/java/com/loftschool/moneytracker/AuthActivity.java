package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loftschool.moneytracker.api.Api;
import com.loftschool.moneytracker.api.AuthResult;

import java.io.IOException;

public class AuthActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 999;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        ImageButton gPlusButton = findViewById(R.id.gplus_button);

        gPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(googleApiClient), RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            final GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess() && googleSignInResult.getSignInAccount() != null) {
                final GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                getSupportLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<AuthResult>() {
                    @Override
                    public Loader<AuthResult> onCreateLoader(int id, Bundle args) {
                        return new android.support.v4.content.AsyncTaskLoader<AuthResult>(AuthActivity.this) {
                            @Override
                            public AuthResult loadInBackground() {
                                Api api = ((App) getApplication()).getApi();
                                try {
                                    return  api.auth(googleSignInAccount.getId()).execute().body();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }
                        };
                    }

                    @Override
                    public void onLoadFinished(Loader<AuthResult> loader, AuthResult data) {
                        if (data != null) {
                            ((App) getApplicationContext()).setAuthToken(data.authToken);
                            finish();
                      } else {
                            showError();
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<AuthResult> loader) {

                    }
                }).forceLoad();
            }
        }
    }

    public void showError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }
}
