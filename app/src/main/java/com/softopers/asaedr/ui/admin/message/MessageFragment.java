package com.softopers.asaedr.ui.admin.message;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.model.AdminEmployeeList;
import com.softopers.asaedr.model.EmpMessageList;
import com.softopers.asaedr.model.MessageRequest;
import com.softopers.asaedr.model.RequestByIds;
import com.softopers.asaedr.model.SendMessage;
import com.softopers.asaedr.model.UserList;
import com.softopers.asaedr.ui.App;
import com.softopers.asaedr.util.ConfigUtils;
import com.softopers.asaedr.util.PrefUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Krunal on 02-08-2015.
 */
public class MessageFragment extends Fragment implements AbsListView.OnScrollListener {

    private static int count = 0;
    CustomAdapter customAdapter;
    ArrayList<UserList> userLists;
    SparseBooleanArray mChecked = new SparseBooleanArray();
    View headerView;
    private ListView mCollectionView;
    private TextView mEmptyView;
    private View mLoadingView;
    private int pageNumber = 0;
    private int mCurrentScrollState;
    private boolean mIsLoadingMore = false;
    private View mApiError;
    private ProgressBar mProgressBarLoadMore;
    private CheckBox checkBox_header;
    private FrameLayout submit_button;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mCollectionView = (ListView) view.findViewById(R.id.list);

        headerView = inflater.inflate(R.layout.custom_list_view_header, mCollectionView, false);

        checkBox_header = (CheckBox) headerView.findViewById(R.id.listitem_message_admin_check);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        submit_button = (FrameLayout) view.findViewById(R.id.submit_button);
        submit_button.setVisibility(View.VISIBLE);
        checkBox_header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                for (int i = 0; i < count; i++) {
                    mChecked.put(i, checkBox_header.isChecked());
                }

                customAdapter.notifyDataSetChanged();

            }
        });

        mCollectionView.addHeaderView(headerView);

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

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        RequestByIds requestByIds = new RequestByIds();
        requestByIds.setAdminId(PrefUtils.getUser(getActivity()).getEmpId());
        requestByIds.setPageNumber(pageNumber);
        Intent intent = new Intent(getActivity(), RestAPIClientService.class);
        intent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.ADMIN_EMPLOYEE_DATA_BY_ADMIN_ID);
        intent.putExtra(App.ADMIN_EMPLOYEE_DATA_BY_ADMIN_ID, requestByIds);
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
                        if (customAdapter == null || pageNumber == 0) {
                            userLists = adminEmployeeList.getUserList();
                            customAdapter = new CustomAdapter(getActivity(), userLists);
                            mCollectionView.setAdapter(customAdapter);
                        } else {
                            showLoadMoreProgress(false);
                            mIsLoadingMore = false;
                            userLists.addAll(adminEmployeeList.getUserList());
                            customAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    showLoadMoreProgress(false);
                    mIsLoadingMore = false;
                    mCollectionView.setOnScrollListener(null);
                }

                submit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageRequest messageRequest = new MessageRequest();
                        SendMessage sendMessage = new SendMessage();

                        if (customAdapter.isAllValuesChecked()) {
                            sendMessage.setSendType(3);
                        } else {
                            ArrayList<EmpMessageList> empMessageLists = new ArrayList<EmpMessageList>();
                            EmpMessageList empMessageList = new EmpMessageList();
                            for (int i = 0; i < userLists.size(); i++) {
                                UserList userList = userLists.get(i);
                                if (userList.isSelected()) {
                                    empMessageList.setEmpId(userList.getEmpId());
                                    empMessageLists.add(empMessageList);
                                }
                            }
                            sendMessage.setEmpMessageList(empMessageLists);
                            sendMessage.setSendType(2);
                        }
                        sendMessage.setAdminId(PrefUtils.getUser(getActivity()).getEmpId());
                        sendMessage.setMessageContent("hello sallu");

                        messageRequest.setSendMessage(sendMessage);

                        Log.v("resp", String.valueOf(messageRequest) + " - - - - - - - - " );
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
    public void onPause() {
        super.onPause();
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
        if (totalItemCount == 0 || customAdapter == null) {
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

    public class CustomAdapter extends ArrayAdapter<UserList> {

        public CustomAdapter(Context context, ArrayList<UserList> userLists) {
            super(context, R.layout.listitem_message_admin, userLists);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final UserList userList = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.listitem_message_admin, parent, false);
                viewHolder.listitem_message_admin_check = (CheckBox) convertView.findViewById(R.id.listitem_message_admin_check);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // Populate the data into the template view using the data object
            viewHolder.listitem_message_admin_check.setText(userList.getEmpName());
            viewHolder.listitem_message_admin_check.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {

                                mChecked.put(position, isChecked);

                                if (isAllValuesChecked()) {

                                    checkBox_header.setChecked(isChecked);
                                }

                                userList.setSelected(true);

                            } else {

                                mChecked.delete(position);

                                checkBox_header.setChecked(isChecked);

                                userList.setSelected(false);
                            }

                        }
                    });

            viewHolder.listitem_message_admin_check.setChecked((mChecked.get(position) ? true : false));

            // Return the completed view to render on screen
            return convertView;
        }

        protected boolean isAllValuesChecked() {

            for (int i = 0; i < count; i++) {
                if (!mChecked.get(i)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public int getCount() {

            /*
             * Length of our listView
             */
            count = userLists.size();
            return count;
        }

        // View lookup cache
        private class ViewHolder {
            CheckBox listitem_message_admin_check;
        }
    }
}