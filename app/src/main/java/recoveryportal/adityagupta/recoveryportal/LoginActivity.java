package recoveryportal.adityagupta.recoveryportal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import recoveryportal.adityagupta.recoveryportal.Data.LoginDetails;
import recoveryportal.adityagupta.recoveryportal.Placeholders.NOInternetPlaceHolder;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;
import recoveryportal.adityagupta.recoveryportal.Utils.ReadJsonFile;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    boolean back = false;

    boolean clicked = false;
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    public void onBackPressed() {
        if (back)
            this.finishAffinity();
        else {
            back = true;
            Toast.makeText(this, "Press Back Again to Close the App", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        AppCompatButton mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (Common.isNetworkAvailable(this)) {
                if (!clicked) {
                    clicked = true;
                    Common.startDownload(this, String.format(Common.LOGIN_LINK, mEmailView.getText(), mPasswordView.getText()), "Login.json", new Handler() {

                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.toString().contains("arg1=1")) {
                                try {
                                    JSONObject mainObject = new JSONObject(ReadJsonFile.getJSONObject(getCacheDir() + "/alljsons/Login.json").toString());

                                    if (mainObject.has("Full_Name")) {
                                        Common.loginDetails = new LoginDetails(
                                                mainObject.getString("Full_Name"),
                                                mainObject.getString("User_Name"),
                                                mainObject.getString("Address"),
                                                mainObject.getString("Mobile_No"),
                                                mainObject.getString("Email_Id"),
                                                mainObject.getString("ID"),
                                                mainObject.getString("Gender"),
                                                mainObject.getString("Password")
                                        );

                                        sharedPreferences.edit().putString("Username", mEmailView.getText().toString()).apply();
                                        sharedPreferences.edit().putString("Password", mPasswordView.getText().toString()).apply();

                                        Intent i = new Intent(LoginActivity.this, ParentActivity.class);
                                        startActivity(i);
                                    } else {
                                        clicked = false;
                                        mEmailView.setError("Invalid ID/Password Combination");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            } else {
                startActivity(new Intent(this, NOInternetPlaceHolder.class));
            }


        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}

