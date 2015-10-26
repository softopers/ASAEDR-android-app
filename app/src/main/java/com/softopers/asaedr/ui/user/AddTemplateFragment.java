package com.softopers.asaedr.ui.user;

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

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.model.ResponseResult;
import com.softopers.asaedr.model.TemplateMaster;
import com.softopers.asaedr.ui.App;
import com.softopers.asaedr.util.ConfigUtils;
import com.softopers.asaedr.util.PrefUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;

import de.greenrobot.event.EventBus;

/**
 * Created by Krunal on 9/6/2015.
 */
public class AddTemplateFragment extends Fragment {

    private static final String TAG = AddTemplateFragment.class.getSimpleName();
    View focusView = null;
    private EditText template_name, template_content;
    private View mApiError;
    private View mProgressView;
    private View mLoginFormView;

    public static AddTemplateFragment newInstance() {
        AddTemplateFragment fragment = new AddTemplateFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_template, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginFormView = view.findViewById(R.id.employee_template_form);
        mProgressView = view.findViewById(R.id.template_progress);
        mApiError = view.findViewById(R.id.api_error);

        template_name = (EditText) view.findViewById(R.id.template_name);
        template_content = (EditText) view.findViewById(R.id.template_content);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        view.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAddTemplate();
            }
        });
    }

    private void attemptAddTemplate() {
        template_name.setError(null);
        template_content.setError(null);

        String templateName = template_name.getText().toString();
        String templateContent = template_content.getText().toString();

        boolean cancel = false;

        if (TextUtils.isEmpty(templateName.trim())) {
            template_name.setError(getString(R.string.error_field_required));
            template_name.setText("");
            focusView = template_name;
            cancel = true;
        } else if (!isTemplateNameValid(templateName.trim())) {
            template_name.setError(getString(R.string.error_invalid_name));
            focusView = template_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(templateContent.trim())) {
            template_content.setError(getString(R.string.error_field_required));
            template_content.setText("");
            focusView = template_content;
            cancel = true;
        } else if (!isTemplateContentValid(templateContent.trim())) {
            template_content.setError(getString(R.string.error_invalid_content));
            focusView = template_content;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (ConfigUtils.isOnline(getActivity())) {

                hideApiError();
                showProgress(true);

                if (!EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().register(this);

                Intent intent = new Intent(getActivity(), RestAPIClientService.class);
                TemplateMaster templateMaster = new TemplateMaster(new TemplateMaster(templateName, templateContent, PrefUtils.getUser(getActivity()).getEmpId()));
                intent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.INSERT_TEMPLATE);
                intent.putExtra(App.INSERT_TEMPLATE, templateMaster);
                WakefulIntentService.sendWakefulWork(getActivity(), intent);
            } else {
                showApiError();
            }
        }
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

    private boolean isTemplateContentValid(String content) {
        return content.length() >= 10 && content.length() <= 2000;
    }

    private boolean isTemplateNameValid(String name) {
        return name.length() >= 5 && name.length() <= 100;
    }

    public void onEvent(final ResponseResult e) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.v(TAG, e.getStatus());

                if (EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().unregister(this);
                showProgress(false);

                if (e.getStatus().equals("Success")) {
                    getActivity().finish();
                } else {
                    template_name.setError(e.getResult());
                    focusView = template_name;
                    focusView.requestFocus();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConfigUtils.isOnline(getActivity())) {
            hideApiError();
        } else {
            showApiError();
        }
    }

    void showApiError() {
        mApiError.setVisibility(View.VISIBLE);
    }

    void hideApiError() {
        mApiError.setVisibility(View.GONE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
