package com.softopers.asaedr.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.model.ChangePasswordRequset;
import com.softopers.asaedr.model.ResponseResult;
import com.softopers.asaedr.util.PrefUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

/**
 * Created by Krunal on 02-08-2015.
 */
public class ChangePasswordFragment extends Fragment {

    private static final String TAG = ChangePasswordFragment.class.getSimpleName();
    EditText employee_change_password_new, employee_change_password_old;
    View focusView = null;
    private View mProgressView;
    private View mEmployeeChangePasswordFormView;

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        employee_change_password_new = (EditText) view.findViewById(R.id.employee_change_password_new);
        employee_change_password_old = (EditText) view.findViewById(R.id.employee_change_password_old);
        mEmployeeChangePasswordFormView = view.findViewById(R.id.employee_change_password_form);
        mProgressView = view.findViewById(R.id.employee_change_password_progress);
        FrameLayout submit_button = (FrameLayout) view.findViewById(R.id.submit_button);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptEmployeeChangePassword();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView profile_image = (ImageView) view.findViewById(R.id.employee_change_password_image);
        if (!PrefUtils.getUser(getActivity()).getImage().equals("")) {
            Picasso.with(getActivity())
                    .load("http://app.allsportsacademy.in/Image/" + PrefUtils.getUser(getActivity()).getImage())
                    .placeholder(R.drawable.person_image_empty)
                    .into(profile_image);
        } else {
            profile_image.setImageResource(R.drawable.person_image_empty);
        }
    }

    public void attemptEmployeeChangePassword() {

        // Reset errors.
        employee_change_password_old.setError(null);
        employee_change_password_new.setError(null);

        // Store values at the time of the login attempt.
        String oldPassword = employee_change_password_old.getText().toString();
        String newPassword = employee_change_password_new.getText().toString();

        boolean cancel = false;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
        if (TextUtils.isEmpty(oldPassword)) {
            employee_change_password_old.setError(getString(R.string.error_field_required));
            focusView = employee_change_password_old;
            cancel = true;
        } else if (!PrefUtils.getPassword(getActivity()).equals(oldPassword)) {
            employee_change_password_old.setError(getString(R.string.error_invalid_current_password));
            focusView = employee_change_password_old;
            cancel = true;
        } else if (!isPasswordValid(oldPassword)) {
            employee_change_password_old.setError(getString(R.string.error_invalid_password));
            focusView = employee_change_password_old;
            cancel = true;
        }

        if (TextUtils.isEmpty(newPassword)) {
            employee_change_password_new.setError(getString(R.string.error_field_required));
            focusView = employee_change_password_new;
            cancel = true;
        } else if (!isPasswordValid(newPassword)) {
            employee_change_password_new.setError(getString(R.string.error_invalid_password));
            focusView = employee_change_password_new;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
            Intent employeeRegisterIntent = new Intent(getActivity(), RestAPIClientService.class);
            ChangePasswordRequset changePasswordRequset = new ChangePasswordRequset();
            changePasswordRequset.setEmpId(PrefUtils.getUser(getActivity()).getEmpId());
            changePasswordRequset.setNewPassword(newPassword);
            changePasswordRequset.setCurrentPassword(oldPassword);
            employeeRegisterIntent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.CHANGE_USER_PASSWORD);
            employeeRegisterIntent.putExtra(App.CHANGE_USER_PASSWORD, changePasswordRequset);
            WakefulIntentService.sendWakefulWork(getActivity(), employeeRegisterIntent);
        }
    }

    public void onEvent(final ResponseResult e) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.v(TAG, e.getStatus());
                showProgress(false);

                if (e.getStatus().equals("Success")) {
                    Toast.makeText(getActivity(), "Your Password has been successfully changed.", Toast.LENGTH_SHORT).show();
                    PrefUtils.setPassword(getActivity(), employee_change_password_new.getText().toString());
                } else {
                    Toast.makeText(getActivity(), e.getResult(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 20;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mEmployeeChangePasswordFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mEmployeeChangePasswordFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEmployeeChangePasswordFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mEmployeeChangePasswordFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}