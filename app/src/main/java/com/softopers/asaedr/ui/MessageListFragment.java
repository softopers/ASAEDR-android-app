package com.softopers.asaedr.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.adapter.MessageListAdapter;
import com.softopers.asaedr.model.MessageListResponse;
import com.softopers.asaedr.model.MessageMaster;
import com.softopers.asaedr.model.RequestByIds;
import com.softopers.asaedr.util.ConfigUtils;
import com.softopers.asaedr.util.PrefUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Krunal on 02-08-2015.
 */
public class MessageListFragment extends Fragment implements AbsListView.OnScrollListener {

    ListView fragment_reporting_list_view;
    MessageListAdapter messageListAdapter;
    ArrayList<MessageMaster> messageMasters;
    private int pageNumber = 0;
    private View mApiError;
    private View mLoadingView;
    private LinearLayout mainLinear;
    private ProgressBar mProgressBarLoadMore;
    private int mCurrentScrollState;
    private boolean mIsLoadingMore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_message_list, container, false);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        fragment_reporting_list_view = (ListView) root.findViewById(R.id.fragment_reporting_list_view);

        LayoutInflater mInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout mFooterView = (RelativeLayout) mInflater.inflate(R.layout.load_more_footer, fragment_reporting_list_view, false);
        mProgressBarLoadMore = (ProgressBar) mFooterView
                .findViewById(R.id.load_more_progressBar);
        fragment_reporting_list_view.addFooterView(mFooterView);

        mLoadingView = root.findViewById(R.id.fragment_reporting_progress);
        mainLinear = (LinearLayout) root.findViewById(R.id.mainLinear);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mApiError = view.findViewById(R.id.api_error);
        view.findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });

    }

    private void requestDataRefresh() {

        RequestByIds requestByIds = new RequestByIds();
        requestByIds.setEmpId(PrefUtils.getUser(getActivity()).getEmpId());
        requestByIds.setPageNumber(pageNumber);
        Intent intent = new Intent(getActivity(), RestAPIClientService.class);
        intent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.MESSAGE_LIST_BY_EMPID);
        intent.putExtra(App.MESSAGE_LIST_BY_EMPID, requestByIds);
        WakefulIntentService.sendWakefulWork(getActivity(), intent);
    }

    public void onEvent(final MessageListResponse messageListResponse) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                showProgress(false);

                if (messageListResponse.getStatus().equals("Success")) {
                    if (messageListResponse.getMessageMaster().size() > 0) {
                        if (messageListAdapter == null || pageNumber == 0) {
                            messageMasters = messageListResponse.getMessageMaster();
                            messageListAdapter = new MessageListAdapter(getActivity(), messageListResponse.getMessageMaster());
                            fragment_reporting_list_view.setAdapter(messageListAdapter);
                        } else {
                            showLoadMoreProgress(false);
                            mIsLoadingMore = false;
                            messageMasters.addAll(messageListResponse.getMessageMaster());
                            messageListAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    showLoadMoreProgress(false);
                    mIsLoadingMore = false;
                    fragment_reporting_list_view.setOnScrollListener(null);
                }
            }
        });
    }

    void showApiError() {
        mApiError.setVisibility(View.VISIBLE);
        showProgress(false);
        mainLinear.setVisibility(View.GONE);
    }

    void hideApiError() {
        mApiError.setVisibility(View.GONE);
    }

    void retry() {

        if (ConfigUtils.isOnline(getActivity())) {
            hideApiError();
            pageNumber = 0;
            showProgress(true);
            requestDataRefresh();
        } else {
            showApiError();
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainLinear.setVisibility(show ? View.GONE : View.VISIBLE);
            mainLinear.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainLinear.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mainLinear.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showLoadMoreProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressBarLoadMore.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBarLoadMore.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBarLoadMore.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBarLoadMore.setVisibility(show ? View.VISIBLE : View.GONE);
//            mCollectionView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ConfigUtils.isOnline(getActivity())) {
            hideApiError();
        } else {
            showApiError();
        }
        fragment_reporting_list_view.setOnScrollListener(this);
        retry();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            view.invalidateViews();
        }
        mCurrentScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount == 0 || messageListAdapter == null) {
            showLoadMoreProgress(false);
            return;
        }
        if (visibleItemCount == totalItemCount) {
            showLoadMoreProgress(false);
            return;
        }
        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
        if (!mIsLoadingMore && loadMore && mCurrentScrollState != SCROLL_STATE_IDLE) {
            if (ConfigUtils.isOnline(getActivity())) {
                hideApiError();
                showLoadMoreProgress(true);
                mIsLoadingMore = true;
                ++pageNumber;
                requestDataRefresh();
            } else {
                showApiError();
            }
        }
    }
}