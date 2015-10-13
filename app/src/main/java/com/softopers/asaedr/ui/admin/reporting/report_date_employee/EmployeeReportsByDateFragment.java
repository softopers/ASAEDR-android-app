package com.softopers.asaedr.ui.admin.reporting.report_date_employee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.adapter.AdminReportTab1Adapter;
import com.softopers.asaedr.model.AdminEmployeeList;
import com.softopers.asaedr.model.RequestByIds;
import com.softopers.asaedr.model.UserList;
import com.softopers.asaedr.ui.App;
import com.softopers.asaedr.ui.admin.reporting.DetailReportsEmpDateActivity;
import com.softopers.asaedr.util.ConfigUtils;
import com.softopers.asaedr.util.PrefUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Krunal on 02-08-2015.
 */
public class EmployeeReportsByDateFragment extends Fragment implements AbsListView.OnScrollListener {

    AdminReportTab1Adapter reportTab1Adapter;
    ArrayList<UserList> userLists;
    private ListView mCollectionView;
    private TextView mEmptyView;
    private View mLoadingView;
    private int pageNumber = 0;
    private int mCurrentScrollState;
    private boolean mIsLoadingMore = false;
    private View mApiError;
    private ProgressBar mProgressBarLoadMore;

    public static EmployeeReportsByDateFragment newInstance() {
        EmployeeReportsByDateFragment fragment = new EmployeeReportsByDateFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mCollectionView = (ListView) view.findViewById(R.id.list);

        LayoutInflater mInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout mFooterView = (RelativeLayout) mInflater.inflate(R.layout.load_more_footer, mCollectionView, false);
        mProgressBarLoadMore = (ProgressBar) mFooterView
                .findViewById(R.id.load_more_progressBar);
        mCollectionView.addFooterView(mFooterView);

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

        RequestByIds requestByIds = new RequestByIds();
        requestByIds.setAdminId(getArguments().getString("AdminId"));
        requestByIds.setDate(getArguments().getString("date"));
        requestByIds.setPageNumber(pageNumber);
        Intent intent = new Intent(getActivity(), RestAPIClientService.class);
        intent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.EMPLOYEE_REPORTS_BY_DATE);
        intent.putExtra(App.EMPLOYEE_REPORTS_BY_DATE, requestByIds);
        WakefulIntentService.sendWakefulWork(getActivity(), intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        mCollectionView.setOnScrollListener(this);
        retry();
    }

    void showApiError() {
        mApiError.setVisibility(View.VISIBLE);
        mCollectionView.setVisibility(View.GONE);
        showProgress(false);
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


    public void onEvent(final AdminEmployeeList adminEmployeeList) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.v("status", adminEmployeeList.getStatus());

                showProgress(false);

                if (adminEmployeeList.getStatus().equals("Success")) {
                    if (adminEmployeeList.getUserList().size() > 0) {
                        if (reportTab1Adapter == null || pageNumber == 0) {
                            userLists = adminEmployeeList.getUserList();
                            reportTab1Adapter = new AdminReportTab1Adapter(getActivity(), userLists);
                            mCollectionView.setAdapter(reportTab1Adapter);
                        } else {
                            showLoadMoreProgress(false);
                            mIsLoadingMore = false;
                            userLists.addAll(adminEmployeeList.getUserList());
                            reportTab1Adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    showLoadMoreProgress(false);
                    mIsLoadingMore = false;
                    mCollectionView.setOnScrollListener(null);
                }

                mCollectionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        UserList userList = (UserList) parent.getAdapter().getItem(position);
                        Intent intent = new Intent(getActivity(), DetailReportsEmpDateActivity.class);
                        intent.putExtra("AdminId", PrefUtils.getUser(getActivity()).getEmpId());
                        intent.putExtra("EmpId", userList.getEmpId());
                        intent.putExtra("Date", getArguments().getString("date"));
                        intent.putExtra("EmpName", userList.getEmpName());
                        intent.putExtra("date-emp", "date-emp");
                        startActivity(intent);
                    }
                });
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

            mCollectionView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCollectionView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCollectionView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mCollectionView.setVisibility(show ? View.GONE : View.VISIBLE);
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
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        if (totalItemCount == 0 || reportTab1Adapter == null) {
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