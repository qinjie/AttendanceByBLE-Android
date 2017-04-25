package com.example.sonata.attendancetakingapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordActivity extends AppCompatActivity {

    private static final int CODE_INCORRECT_PASSWORD = 11;

    @BindView(R.id.old_password)
    EditText _currentpasswordText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_confirmpass)
    EditText _confirmedPasswordText;
    @BindView(R.id.btn_submit)
    Button _submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        this.setTitle(R.string.change_password_title);

        ButterKnife.bind(this);

        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // app icon in action bar clicked; go to parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changePassword() {
        if (!validate()) {
            onChangePasswordFailed();
            return;
        }

        changePasswordAction();
    }

    public void onChangePasswordSuccess() {
        Preferences.dismissLoading();

        _currentpasswordText.setText("");
        _passwordText.setText("");
        _confirmedPasswordText.setText("");

        finish();

        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
        startActivity(intent);
    }

    public void onChangePasswordFailed() {
        Preferences.dismissLoading();
    }

    public boolean validate() {

        boolean valid = true;

        String currentPassword = _currentpasswordText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirmedPassword = _confirmedPasswordText.getText().toString();

        if (currentPassword.isEmpty() || currentPassword.length() < 4 || currentPassword.length() > 10) {
            _currentpasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _currentpasswordText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Password must be between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (password.compareTo(currentPassword) == 0) {
            _passwordText.setError("New password must be different with current password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (confirmedPassword.compareTo(password) != 0) {
            _confirmedPasswordText.setError("These passwords don't match. Try again?");
            valid = false;
        } else {
            _confirmedPasswordText.setError(null);
        }

        return valid;
    }

    void changePasswordAction() {

        Preferences.showLoading(ChangePasswordActivity.this, "Reset Password", "Processing...");

        SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
        String auCode = pref.getString("authorizationCode", null);

        String currentPassword = _currentpasswordText.getText().toString();
        String password = _passwordText.getText().toString();

        JsonObject toUp = new JsonObject();
        toUp.addProperty("oldPassword", currentPassword);
        toUp.addProperty("newPassword", password);

        ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
        Call<ResponseBody> call = client.changePassword(toUp);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    int messageCode = response.code();
                    if (messageCode == 200) // SUCCESS
                    {
                        onChangePasswordSuccess();
                        Toast.makeText(getBaseContext(), "Change password successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        onChangePasswordFailed();
                        if (messageCode == 400) // BAD REQUEST HTTP
                        {
                            JSONObject data = new JSONObject(response.errorBody().string());
                            int errorCode = data.getInt("code");
                            if (errorCode == CODE_INCORRECT_PASSWORD) {
                                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(ChangePasswordActivity.this).create();
                                alertDialog.setTitle("Change password failed");

                                alertDialog.setMessage("Wrong password.");

                                alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
//                            Preferences.showBadRequestNotificationDialog(ChangePasswordActivity.this, errorCode, R.string.change_password_title);
                        }else{
                            onChangePasswordFailed();
                            android.app.AlertDialog builder = new android.app.AlertDialog.Builder(ChangePasswordActivity.this).create();
                            builder.setTitle(getString(R.string.another_login_title));
                            builder.setMessage(getString(R.string.another_login_content));

                            builder.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Preferences.clearStudentInfo();
                                            Intent intent = new Intent(getBaseContext(), LogInActivity.class);
                                            startActivity(intent);                                        }
                                    });
                            builder.show();

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onChangePasswordFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(Preferences.checkInternetOn()){
                    onChangePasswordFailed();
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(ChangePasswordActivity.this).create();
                    alertDialog.setTitle("Change password failed");

                    alertDialog.setMessage("Please turn on internet connection.");


                    alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }
        });

    }
}
