package com.ilkayaktas.awsauthexperiment.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.ilkayaktas.awsauthexperiment.R;

public class SplashScreenActivity extends BaseActivity {

    private final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeAWS();
    }

    @Override
    int getContentView() {
        return R.layout.activity_splash_screen;
    }

    private void initializeAWS(){
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        Log.wtf(TAG, "initializeAWS: onResult: " + userStateDetails.getUserState());

                        switch (userStateDetails.getUserState()){
                            case SIGNED_IN:
                                Log.wtf(TAG, "Signed in!");
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                finish();
                                break;
                            case SIGNED_OUT:
                                Log.wtf(TAG, "Signed out!");
                                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                                finish();
                                break;
                            default:
                                AWSMobileClient.getInstance().signOut();
                                break;
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Initialization error.", e);
                    }
                }
        );
    }
}
