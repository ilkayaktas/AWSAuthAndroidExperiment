package com.ilkayaktas.awsauthexperiment.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.ForgotPasswordResult;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ilkayaktas.awsauthexperiment.R;

public class ForgotPasswordActivity extends BaseActivity {

    private final String TAG = "ForgotPasswordActivity";

    @BindView(R.id.username) EditText usernameView;
    @BindView(R.id.confirmationCode) EditText confirmationView;
    @BindView(R.id.newpassword) EditText newPasswordView;
    @BindView(R.id.confirmButton) Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
    }

    @Override
    int getContentView() {
        return R.layout.activity_forgot_password;
    }


    public void sendCodeAction(View view) {
        String username = usernameView.getText().toString();

        if(username.isEmpty()){
            YoYo.with(Techniques.Tada)
                    .duration(300)
                    .repeat(1)
                    .playOn(usernameView);
            return;
        }

        forgatPassword(username);
    }

    public void confirmAction(View view) {
        String newPasword = newPasswordView.getText().toString();
        String confirmationCode = confirmationView.getText().toString();

        if(newPasword.isEmpty()){
            YoYo.with(Techniques.Tada)
                    .duration(300)
                    .repeat(1)
                    .playOn(newPasswordView);
            return;
        }

        if(confirmationCode.isEmpty()){
            YoYo.with(Techniques.Tada)
                    .duration(300)
                    .repeat(1)
                    .playOn(confirmationView);
            return;
        }

        enterConfirmationCodeForForgotPassword(newPasword, confirmationCode);
    }

    private void forgatPassword(String username){
        AWSMobileClient.getInstance().forgotPassword(username, new Callback<ForgotPasswordResult>() {
            @Override
            public void onResult(final ForgotPasswordResult result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "forgot password state: " + result.getState());
                        switch (result.getState()) {
                            case CONFIRMATION_CODE:
                                makeToast("Confirmation code is sent to reset password");
                                confirmationView.setVisibility(View.VISIBLE);
                                newPasswordView.setVisibility(View.VISIBLE);
                                confirmButton.setVisibility(View.VISIBLE);
                                break;
                            default:
                                Log.e(TAG, "un-supported forgot password state");
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeToast("Forgot password error! "+e.getLocalizedMessage());
                    }
                });
                Log.e(TAG, "Forgot password error", e);
            }
        });


    }

    private void enterConfirmationCodeForForgotPassword(String newPassword, String confirmationCode){
        AWSMobileClient.getInstance().confirmForgotPassword(newPassword, confirmationCode, new Callback<ForgotPasswordResult>() {
            @Override
            public void onResult(final ForgotPasswordResult result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "forgot password state: " + result.getState());
                        switch (result.getState()) {
                            case DONE:
                                makeToast("Password changed successfully");
                                finish();
                                break;
                            default:
                                Log.e(TAG, "un-supported forgot password state");
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "forgot password error", e);
            }
        });
    }

}
