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
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ilkayaktas.awsauthexperiment.R;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends BaseActivity {

    private final String TAG = "SignupActivity";

    @BindView(R.id.username) EditText usernameView;
    @BindView(R.id.password) EditText passwordView;
    @BindView(R.id.confirmationCode) EditText confirmationView;
    @BindView(R.id.confirmButton) Button confirmButton;
    @BindView(R.id.resendButton) Button resendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    int getContentView() {
        return R.layout.activity_signup;
    }


    public void signupAction(View view) {
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

        signUp(username, password);
    }

    public void confirmAction(View view) {
        String username = usernameView.getText().toString();
        String confirmCode = confirmationView.getText().toString();

        if(confirmCode.isEmpty()){
            YoYo.with(Techniques.Tada)
                    .duration(300)
                    .repeat(1)
                    .playOn(confirmationView);
            return;
        }

        if(username.isEmpty()){
            YoYo.with(Techniques.Tada)
                    .duration(300)
                    .repeat(1)
                    .playOn(usernameView);
            return;
        }

        confirmSignUp(username, confirmCode);
    }

    public void resendAction(View view) {
        String username = usernameView.getText().toString();

        if(username.isEmpty()){
            YoYo.with(Techniques.Tada)
                    .duration(300)
                    .repeat(1)
                    .playOn(usernameView);
            return;
        }

        resendConfirmation(username);
    }

    private void signUp(String username, String password){
        final Map<String, String> attributes = new HashMap<>();
        attributes.put("name", "İlkay Aktaş");
        attributes.put("email", username);
        AWSMobileClient.getInstance().signUp(username, password, attributes, null, new Callback<SignUpResult>() {
            @Override
            public void onResult(final SignUpResult signUpResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                        if (!signUpResult.getConfirmationState()) {
                            final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
                            makeToast("Confirm sign-up with: " + details.getDestination());

                            confirmationView.setVisibility(View.VISIBLE);
                            confirmButton.setVisibility(View.VISIBLE);
                            resendButton.setVisibility(View.VISIBLE);

                        } else {
                            makeToast("Sign-up done.");
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeToast("Sign-up error! "+e.getLocalizedMessage());
                    }
                });

                Log.e(TAG, "Sign-up error", e);
            }
        });
    }

    private void confirmSignUp(String username, String code){
        AWSMobileClient.getInstance().confirmSignUp(username, code, new Callback<SignUpResult>() {
            @Override
            public void onResult(final SignUpResult signUpResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                        if (!signUpResult.getConfirmationState()) {
                            final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
                            makeToast("Confirm sign-up with: " + details.getDestination());
                        } else {
                            makeToast("Sign-up done.");
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeToast("Confirm sign-up error! "+e.getLocalizedMessage());
                    }
                });

                Log.e(TAG, "Confirm sign-up error", e);
                
            }
        });
    }

    private void resendConfirmation(String username){
        AWSMobileClient.getInstance().resendSignUp(username, new Callback<SignUpResult>() {
            @Override
            public void onResult(SignUpResult signUpResult) {
                makeToast("A verification code has been sent!");
                Log.i(TAG, "A verification code has been sent via" +
                        signUpResult.getUserCodeDeliveryDetails().getDeliveryMedium()
                        + " at " +
                        signUpResult.getUserCodeDeliveryDetails().getDestination());
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeToast("Resend error! "+e.getLocalizedMessage());
                    }
                });

                makeToast("Resend error:");
                Log.e(TAG, "Resend error: ", e);
            }
        });
    }

}
