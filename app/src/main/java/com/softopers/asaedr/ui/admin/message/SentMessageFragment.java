package com.softopers.asaedr.ui.admin.message;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.adapter.SentMessageAdapter;
import com.softopers.asaedr.model.RequestByIds;
import com.softopers.asaedr.model.ResponseMessage;
import com.softopers.asaedr.model.ResponseSentMessageDetail;
import com.softopers.asaedr.ui.App;
import com.softopers.asaedr.util.ConfigUtils;
import com.softopers.asaedr.util.PrefUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;

import de.greenrobot.event.EventBus;

/**
 * Created by Krunal on 02-08-2015.
 */
public class SentMessageFragment extends Fragment {

    private static int count = 0;
    private ListView list;
    private TextView mEmptyView;
    private View mLoadingView;
    private View mApiError;
    private SentMessageAdapter sentMessageAdapter;

    public static SentMessageFragment newInstance() {
        SentMessageFragment fragment = new SentMessageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sent_message, container, false);
        list = (ListView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mEmptyView = (TextView) view.findViewById(R.id.empty_text);
        mLoadingView = view.findViewById(R.id.fragment_main_progress);

        mApiError = view.findViewById(R.id.api_error);
        view.findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });

    }

    private void requestDataRefresh() {

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        RequestByIds requestByIds = new RequestByIds();
        requestByIds.setEmpId(PrefUtils.getUser(getActivity()).getEmpId());
        requestByIds.setMessageId(getArguments().getString("message_id"));
        Intent intent = new Intent(getActivity(), RestAPIClientService.class);
        intent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.SENT_MESSAGE_DETAIL);
        intent.putExtra(App.SENT_MESSAGE_DETAIL, requestByIds);
        WakefulIntentService.sendWakefulWork(getActivity(), intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        retry();
    }

    void showApiError() {
        mApiError.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
        showProgress(false);
    }

    void hideApiError() {
        mApiError.setVisibility(View.GONE);
    }

    void retry() {

        if (ConfigUtils.isOnline(getActivity())) {
            hideApiError();
            showProgress(true);
            requestDataRefresh();
        } else {
            showApiError();
        }
    }


    public void onEvent(final ResponseSentMessageDetail responseSentMessageDetail) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                showProgress(false);
                if (EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().unregister(this);

                if (responseSentMessageDetail.getStatus().equals("Success")) {
                    if (responseSentMessageDetail.getSentMessages().size() > 0) {
                        sentMessageAdapter = new SentMessageAdapter(getActivity(), responseSentMessageDetail.getSentMessages());
                        list.setAdapter(sentMessageAdapter);
                    }
                }
            }
        });
    }

    public void onEvent(final ResponseMessage e) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.v("TAG", e.getStatus());
                showProgress(false);

            }
        });
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            list.setVisibility(show ? View.GONE : View.VISIBLE);
            list.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    list.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoadingView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
            list.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}