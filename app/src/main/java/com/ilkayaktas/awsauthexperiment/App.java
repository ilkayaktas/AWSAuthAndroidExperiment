package com.ilkayaktas.awsauthexperiment;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService;

public class App extends Application {

    private static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();

        // Transfer service will be run here

    }

}
