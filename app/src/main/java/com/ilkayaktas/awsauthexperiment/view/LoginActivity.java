package com.ilkayaktas.awsauthexperiment.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.SignInResult;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ilkayaktas.awsauthexperiment.R;

public class LoginActivity extends BaseActivity {

    private final String TAG = "LoginActivity";

    @BindView(R.id.username) EditText usernameView;
    @BindView(R.id.password) EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    int getContentView() {
        return R.layout.activity_login;
    }

    public void loginAction(View view) {
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        if(username.isEmpty()){
            YoYo.with(Techniques.Tada)
                    .duration(300)
                    .repeat(1)
                    .playOn(usernameView);
            return;
        }

        if(password.isEmpty()){
            YoYo.with(Techniques.Tada)
                    .duration(300)
                    .repeat(1)
                    .playOn(passwordView);
            return;
        }

        signIn(username, password);
    }

    private void signIn(String username, String password){
        AWSMobileClient.getInstance().signIn(username, password, null, new Callback<SignInResult>() {
            @Override
            public void onResult(final SignInResult signInResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Sign-in callback state: " + signInResult.getSignInState());
                        switch (signInResult.getSignInState()) {
                            case DONE:
                                makeToast("Sign-in done.");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                break;
                            case SMS_MFA:
                                makeToast("Please confirm sign-in with SMS.");
                                break;
                            case NEW_PASSWORD_REQUIRED:
                                makeToast("Please confirm sign-in with new password.");
                                break;
                            default:
                                makeToast("Unsupported sign-in confirmation: " + signInResult.getSignInState());
                                break;
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeToast("Sign-in error! "+e.getLocalizedMessage());
                    }
                });
                Log.e(TAG, "Sign-in error", e);
            }
        });
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    public void dontHaveAccount(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }


    private void confirmSignIn(String signInChallengeResponse){ // Confirm with sms
        AWSMobileClient.getInstance().confirmSignIn(signInChallengeResponse, new Callback<SignInResult>() {
            @Override
            public void onResult(SignInResult signInResult) {
                Log.d(TAG, "Sign-in callback state: " + signInResult.getSignInState());
                switch (signInResult.getSignInState()) {
                    case DONE:
                        makeToast("Sign-in done.");
                        break;
                    case SMS_MFA:
                        makeToast("Please confirm sign-in with SMS.");
                        break;
                    case NEW_PASSWORD_REQUIRED:
                        makeToast("Please confirm sign-in with new password.");
                        break;
                    default:
                        makeToast("Unsupported sign-in confirmation: " + signInResult.getSignInState());
                        break;
                }
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeToast("Sign-in error! "+e.getLocalizedMessage());
                    }
                });
                Log.e(TAG, "Sign-in error", e);
            }
        });
    }

    private void forceChangePassword(String newPassword){

        AWSMobileClient.getInstance().confirmSignIn(newPassword, new Callback<SignInResult>() {
            @Override
            public void onResult(SignInResult signInResult) {
                Log.d(TAG, "Sign-in callback state: " + signInResult.getSignInState());
                switch (signInResult.getSignInState()) {
                    case DONE:
                        makeToast("Sign-in done.");
                        break;
                    case SMS_MFA:
                        makeToast("Please confirm sign-in with SMS.");
                        break;
                    default:
                        makeToast("Unsupported sign-in confirmation: " + signInResult.getSignInState());
                        break;
                }
            }
            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeToast("Sign-in error! "+e.getLocalizedMessage());
                    }
                });
                Log.e(TAG, "Sign-in error", e);
            }
        });
    }

}
