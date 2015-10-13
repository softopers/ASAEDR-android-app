package com.softopers.asaedr.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.model.LoginDetail;
import com.softopers.asaedr.model.User;
import com.softopers.asaedr.ui.admin.reporting.ReportsActivity;
import com.softopers.asaedr.ui.user.MainActivity;
import com.softopers.asaedr.util.ConfigUtils;
import com.softopers.asaedr.util.PrefUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;

import de.greenrobot.event.EventBus;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    public static String TAG = LoginActivity.class.getSimpleName();
    // UI references.
    private EditText login_username, login_password;
    private View mProgressView;
    private View mLoginFormView;
    private View mApiError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if the EULA has been accepted; if not, show it.
        if (!PrefUtils.isTosAccepted(this)) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        login_username = (EditText) findViewById(R.id.login_username);

        login_password = (EditText) findViewById(R.id.login_password);
        login_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        FrameLayout login_sign_in_button = (FrameLayout) findViewById(R.id.login_sign_in_button);
        login_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mApiError = findViewById(R.id.api_error);

    }

    public void attemptLogin() {

        // Reset errors.
        login_username.setError(null);
        login_password.setError(null);

        // Store values at the time of the login attempt.
        String email = login_username.getText().toString();
        String password = login_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            login_password.setError(getString(R.string.error_invalid_password));
//            focusView = login_password;
//            cancel = true;
//        }
        if (TextUtils.isEmpty(password)) {
            login_password.setError(getString(R.string.error_field_required));
            focusView = login_password;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            login_password.setError(getString(R.string.error_invalid_password));
            focusView = login_password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            login_username.setError(getString(R.string.error_field_required));
            focusView = login_username;
            cancel = true;
        } else if (!isEmailValid(email)) {
            login_username.setError(getString(R.string.error_invalid_username));
            focusView = login_username;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (ConfigUtils.isOnline(this)) {

                hideApiError();
                showProgress(true);

                Intent loginIntent = new Intent(LoginActivity.this, RestAPIClientService.class);
                User user = new User();
                user.setUserName(email);
                user.setPassword(password);
                user.setDeviceToken(PrefUtils.getDeviceToken(this));
                loginIntent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.LOGIN_USER);
                loginIntent.putExtra(App.USER, user);
                WakefulIntentService.sendWakefulWork(LoginActivity.this, loginIntent);
            } else {
                showApiError();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConfigUtils.isOnline(this)) {
            hideApiError();
        }
    }

    void showApiError() {
        mApiError.setVisibility(View.VISIBLE);
    }

    void hideApiError() {
        mApiError.setVisibility(View.GONE);
    }


    private boolean isEmailValid(String email) {
        return email.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(final LoginDetail loginDetail) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Log.v(TAG, loginDetail.getStatus());
                showProgress(false);

                if (loginDetail.getStatus().equals("Success")) {
                    finish();
                    PrefUtils.markLoggedIn(LoginActivity.this);
                    Intent loginIntent;
                    if (loginDetail.getUser().getIsAdmin()) {
                        PrefUtils.setUserRole(getApplicationContext(), App.ADMIN);
                        loginIntent = new Intent(LoginActivity.this, ReportsActivity.class);
                    } else {
                        PrefUtils.setUserRole(getApplicationContext(), App.USER);
                        loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    }
                    PrefUtils.setEmail(getApplicationContext(), login_username.getText().toString());
                    PrefUtils.setPassword(getApplicationContext(), login_password.getText().toString());
                    PrefUtils.setUser(getApplicationContext(), loginDetail.getUser());
                    startActivity(loginIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Username or Password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
