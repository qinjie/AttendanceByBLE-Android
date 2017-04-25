package com.example.sonata.attendancetakingapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sonata.attendancetakingapplication.Model.LoginInfo;
import com.example.sonata.attendancetakingapplication.Model.LoginResult;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerCallBack;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferencesTag;
import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferences_ModeTag;

public class LogInActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_FORGOT_PASSWORD = 1;

    @BindView(R.id.input_username)
    EditText _usernameText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_forgotPass)
    TextView _forgotPasswordLink;
//    @BindView(R.id.link_signup)
//    TextView _signupLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_log_in);

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _forgotPasswordLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Forgot Password activity
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivityForResult(intent, REQUEST_FORGOT_PASSWORD);
            }
        });

//        _signupLink.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Start the Signup activity
//                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
//                startActivityForResult(intent, REQUEST_SIGNUP);
//            }
//        });

        SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("isLogin", "false");
        editor.apply();

//        _usernameText.clearFocus();
//        _passwordText.clearFocus();
    }


    public void login() {

        if (!validate()) {
            _loginButton.setEnabled(true);
            return;
        }

        _loginButton.setEnabled(false);

        final String username = _usernameText.getText().toString();
        final String password = _passwordText.getText().toString();

        loginAction(username, password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    public void onBackPressed() {
        // disable going back to the NavigationActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
        Preferences.dismissLoading();
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LogInActivity.this).create();
        alertDialog.setTitle("Login Failed");
        if (checkInternetOn()) {
            alertDialog.setMessage("Please check username and password again.");

        } else {
            alertDialog.setMessage("Please turn on internet connection.");

        }
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public boolean checkInternetOn() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null) {
            return false;
        } else {
            return true;
        }

    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty() || username.length() < 4 || username.length() > 255) {
            _usernameText.setError("Enter a valid username address");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 255) {
            _passwordText.setError("Password is required at least 6 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void startNavigation() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("isLogin", "true");
        editor.apply();
    }

    public void onRegisterFailed(int errorCode) {
//        Preferences.showBadRequestNotificationDialog(this, errorCode, R.string.login_title);
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LogInActivity.this).create();
        alertDialog.setTitle("Register Failed");
        if (checkInternetOn() && errorCode ==400) {
            alertDialog.setMessage("This device has been registered with another account. Please contact to teacher for help.");

        } else {
            alertDialog.setMessage("Please turn on internet connection.");

        }
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void registerNewDevice() {
        Preferences.showLoading(this, "Register device", "Processing...");

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        JsonObject toUp = new JsonObject();
        toUp.addProperty("username", username);
        toUp.addProperty("password", password);
        toUp.addProperty("device_hash", Preferences.getMac(this));

        ServerApi client = ServiceGenerator.createService(ServerApi.class);
        Call<ResponseBody> call = client.registerDevice(toUp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Preferences.dismissLoading();
                    int messageCode = response.code();
                    if (messageCode == 200) // SUCCESS
                    {
                        SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("status", "1");
                        editor.apply();

                        onLoginSuccess();
                        startNavigation();
                    } else {
                        if (messageCode == 400) // BAD REQUEST HTTP
                        {
                            JSONObject data = new JSONObject(response.errorBody().string());
                            onRegisterFailed(messageCode);
                        }
                    }
                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void requestRegisterNewDevice() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.not_register_device_title);
        builder.setMessage(R.string.not_register_device_message);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                Preferences.clearStudentInfo();
                return;
            }
        });
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        registerNewDevice();
                    }
                });
        builder.create().show();
    }

    public void loginAction(String username, String password) {


        Preferences.showLoading(this, "Log In", "Authenticating...");
        final Activity act = this;
        ServerApi client = ServiceGenerator.createService(ServerApi.class);
        LoginInfo up = new LoginInfo(username, password, this);
        Call<LoginResult> call = client.login(up);
        call.enqueue(new ServerCallBack<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                Preferences.setActivity(act);
                Preferences.dismissLoading();
                int messageCode = response.code();
                if (messageCode == 200) // SUCCESS
                {
                    if (response.body().getDevice_hash().equals(Preferences.getMac(getBaseContext()))) {
                        Preferences.setStudentInfo(response.body());
                        startNavigation();
                        onLoginSuccess();
                    } else {
                        Preferences.setStudentInfo(response.body());
                        requestRegisterNewDevice();
                    }
                } else {
                    onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                onLoginFailed();
            }
        });

    }
}
