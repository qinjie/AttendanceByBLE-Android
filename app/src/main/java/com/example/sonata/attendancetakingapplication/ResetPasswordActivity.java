package com.example.sonata.attendancetakingapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.input_email)    EditText _emailText;
    @BindView(R.id.btn_forgotPass) Button   _resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ButterKnife.bind(this);

        this.setTitle("Reset Password");

        _resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNewPass();
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
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void requestNewPass() {

        if (!validate()) {
            onResetPassFailed();
            return;
        }

        Preferences.showLoading(ResetPasswordActivity.this, "Reset Password", "Processing...");

        _resetPasswordButton.setEnabled(false);

        String email = _emailText.getText().toString();
        resetPassAction(email);
    }

    public void onResetPassSuccess() {
        setResult(RESULT_OK, null);
        _resetPasswordButton.setEnabled(true);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset password");
        builder.setMessage("Please check your email to get reset password link!");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( final DialogInterface dialogInterface, final int i) {
                        Intent intent = new Intent(ResetPasswordActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }
                });

        builder.create().show();
    }

    public void onResetPassFailed() {
        _resetPasswordButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        return valid;
    }

    public void resetPassAction(String email) {

        ServerApi client = ServiceGenerator.createService(ServerApi.class);

        JsonObject userEmail = new JsonObject();
        userEmail.addProperty("email", email);

        Call<ResponseBody> call = client.resetPassword(userEmail);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Preferences.dismissLoading();
                    int messageCode = response.code();

                    if (messageCode == 200) // SUCCESS
                    {
                        onResetPassSuccess();
                    }
                    else
                    {
                        onResetPassFailed();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                    Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
//                startActivity(intent);
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(ResetPasswordActivity.this).create();
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
        });
    }
}
