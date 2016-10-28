package com.example.sonata.attendancetakingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sonata.attendancetakingapplication.Model.SignupInfo;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static boolean isRegisterDevice = false;

    @InjectView(R.id.input_username)    EditText _usernameText;
    @InjectView(R.id.input_email)       EditText _emailText;
    @InjectView(R.id.input_studentId)   EditText _studentIdText;
    @InjectView(R.id.input_password)    EditText _passwordText;
    @InjectView(R.id.input_confirmpass) EditText _confirmedPasswordText;
    @InjectView(R.id.btn_signup)        Button   _signupButton;
    @InjectView(R.id.link_login)        TextView _loginLink;
    @InjectView(R.id.item_check)        CheckBox _checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setTitle("Sign Up");

        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

        _checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_checkBox.isChecked())
                {
                    isRegisterDevice = true;
                }
                else {
                    isRegisterDevice = false;
                }
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

    public void signup() {
        Preferences.showLoading(SignUpActivity.this, "Sign Up", "Creating Account...");
        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        String username  = _usernameText.getText().toString();
        String password  = _passwordText.getText().toString();
        String studentId = _studentIdText.getText().toString();
        String email     = _emailText.getText().toString();

        SignupInfo user = new SignupInfo(username, password, email, studentId, this);
        signupAction(user);
    }

    public void onSignupSuccess() {
        setResult(RESULT_OK, null);

        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);
    }

    public void onSignupFailed() {
        _signupButton.setEnabled(true);

    }

    public boolean validate() {
        boolean valid = true;

        String username          = _usernameText.getText().toString();
        String email             = _emailText.getText().toString();
        String password          = _passwordText.getText().toString();
        String confirmedPassword = _confirmedPasswordText.getText().toString();
        String studentId         = _studentIdText.getText().toString();


        if (username.isEmpty() || username.length() < 4 || username.length() > 255) {
            _usernameText.setError("enter a valid username");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (studentId.isEmpty()) {
            _studentIdText.setError("enter a valid email student number");
            valid = false;
        } else {
            _studentIdText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (confirmedPassword.compareTo(password) != 0) {
            _confirmedPasswordText.setError("These passwords don't match. Try again?");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (isRegisterDevice == false)
        {
            Preferences.showNotificationDialog(SignUpActivity.this, R.string.signup_title, R.string.register_device_message);
            valid = false;
        }

        return valid;
    }

    public void signupAction(SignupInfo user) {
        ServerApi client = ServiceGenerator.createService(ServerApi.class);
        Call<ResponseBody> call = client.signup(user);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Preferences.dismissLoading();
                    int messageCode = response.code();
                    if (messageCode == 200) // SUCCESS
                    {
                        onSignupSuccess();
                    }
                    else
                    {
                        onSignupFailed();
                    }

                }
                catch(Exception e){
                    e.printStackTrace();
                    Intent intent = new Intent(SignUpActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Intent intent = new Intent(SignUpActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}

