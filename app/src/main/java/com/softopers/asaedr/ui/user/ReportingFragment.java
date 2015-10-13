package com.softopers.asaedr.ui.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.adapter.EmployeeReportingListAdapter;
import com.softopers.asaedr.model.Report;
import com.softopers.asaedr.model.ReportList;
import com.softopers.asaedr.model.RequestByIds;
import com.softopers.asaedr.model.ResponseReportingList;
import com.softopers.asaedr.model.ResponseResult;
import com.softopers.asaedr.ui.App;
import com.softopers.asaedr.util.ConfigUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Krunal on 02-08-2015.
 */
public class ReportingFragment extends Fragment implements AbsListView.OnScrollListener {

    ListView fragment_reporting_list_view;
    EmployeeReportingListAdapter employeeReportingListAdapter;
    ArrayList<ReportList> reports;
    ImageButton fragment_reporting_template;
    private int pageNumber = 0;
    private EditText fragment_reporting_comment;
    private View mApiError;
    private View mLoadingView;
    private LinearLayout mainLinear, reportLinear;
    private ProgressBar mProgressBarLoadMore;
    private int mCurrentScrollState;
    private boolean mIsLoadingMore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_reporting, container, false);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        fragment_reporting_list_view = (ListView) root.findViewById(R.id.fragment_reporting_list_view);

        fragment_reporting_template = (ImageButton) root.findViewById(R.id.fragment_reporting_template);

        LayoutInflater mInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout mFooterView = (RelativeLayout) mInflater.inflate(R.layout.load_more_footer, fragment_reporting_list_view, false);
        mProgressBarLoadMore = (ProgressBar) mFooterView
                .findViewById(R.id.load_more_progressBar);
        fragment_reporting_list_view.addFooterView(mFooterView);
        reportLinear = (LinearLayout) root.findViewById(R.id.reportLinear);

        fragment_reporting_list_view.setOnScrollListener(this);

        if (getArguments().getBoolean("isLock")) {
            reportLinear.setVisibility(View.GONE);
        }

        mLoadingView = root.findViewById(R.id.fragment_reporting_progress);
        mainLinear = (LinearLayout) root.findViewById(R.id.mainLinear);

        fragment_reporting_comment = (EditText) root.findViewById(R.id.fragment_reporting_comment);
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

        view.findViewById(R.id.fragment_reporting_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptReporting();
            }
        });

        fragment_reporting_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TemplateActivity.class);
                startActivityForResult(intent, 1);
                fragment_reporting_comment.setText("");
            }
        });

        retry();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                fragment_reporting_comment.setText(result);
            }
        }
    }

    private void attemptReporting() {

        fragment_reporting_comment.setError(null);
        String reporting = fragment_reporting_comment.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(reporting.trim())) {
            fragment_reporting_comment.setError(getString(R.string.error_field_required));
            fragment_reporting_comment.setText("");
            focusView = fragment_reporting_comment;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            if (ConfigUtils.isOnline(getActivity())) {

                showProgress(true);

                if (!EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().register(this);

                Intent intent = new Intent(getActivity(), RestAPIClientService.class);
                Report report = new Report();
                report.setContent(reporting.trim());
                report.setDayStatusId(Integer.valueOf(getArguments().getString("DayStatusId")));
                Report report1 = new Report(report);
                intent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.INSERT_REPORT);
                intent.putExtra(App.INSERT_REPORT, report1);
                WakefulIntentService.sendWakefulWork(getActivity(), intent);
            } else {
                showApiError();
                getActivity().findViewById(R.id.retry).setVisibility(View.GONE);
            }
        }
    }

    private void requestDataRefresh() {

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        RequestByIds requestByIds = new RequestByIds();
        requestByIds.setDayStatusId(Integer.valueOf(getArguments().getString("DayStatusId")));
        requestByIds.setPageNumber(pageNumber);
        Intent intent = new Intent(getActivity(), RestAPIClientService.class);
        intent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.EMPLOYEE_REPORTING_LIST);
        intent.putExtra(App.EMPLOYEE_REPORTING_LIST, requestByIds);
        WakefulIntentService.sendWakefulWork(getActivity(), intent);
    }

    public void onEvent(final ResponseReportingList responseReportingList) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().unregister(this);
                Log.v("status", responseReportingList.getStatus());

                showProgress(false);

                if (responseReportingList.getStatus().equals("Success")) {
                    if (responseReportingList.getReportList().size() > 0) {
                        if (employeeReportingListAdapter == null || pageNumber == 0) {
                            reports = responseReportingList.getReportList();
                            employeeReportingListAdapter = new EmployeeReportingListAdapter(getActivity(), responseReportingList.getReportList());
                            fragment_reporting_list_view.setAdapter(employeeReportingListAdapter);
                        } else {
                            showLoadMoreProgress(false);
                            mIsLoadingMore = false;
                            reports.addAll(responseReportingList.getReportList());
                            employeeReportingListAdapter.notifyDataSetChanged();
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

    public void onEvent(final ResponseResult e) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                if (EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().unregister(this);

                if (e.getStatus().equals("Success")) {
                    if (ConfigUtils.isOnline(getActivity())) {
                        hideApiError();
                        pageNumber = 0;
                        showProgress(true);
                        requestDataRefresh();
                    } else {
                        showApiError();
                    }
                    fragment_reporting_comment.setText("");
                }
            }
        });
    }

    void showApiError() {
        mApiError.setVisibility(View.VISIBLE);
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
        if (totalItemCount == 0 || employeeReportingListAdapter == null) {
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

    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}