package com.softopers.asaedr.ui.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.adapter.EmployeeTemplateListAdapter;
import com.softopers.asaedr.model.RequestByIds;
import com.softopers.asaedr.model.ResponseResult;
import com.softopers.asaedr.model.TemplateDetail;
import com.softopers.asaedr.model.TemplateList;
import com.softopers.asaedr.model.TemplateMaster;
import com.softopers.asaedr.ui.App;
import com.softopers.asaedr.util.ConfigUtils;
import com.softopers.asaedr.util.PrefUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Krunal on 02-08-2015.
 */
public class TemplateFragment extends Fragment implements AbsListView.OnScrollListener {

    EmployeeTemplateListAdapter employeeTemplateListAdapter;
    ArrayList<TemplateDetail> templateDetails;
    private SwipeMenuListView mCollectionView;
    private TextView mEmptyView;
    private View mLoadingView;
    private int pageNumber = 0;
    private int mCurrentScrollState;
    private boolean mIsLoadingMore = false;
    private View mApiError;
    private ProgressBar mProgressBarLoadMore;

    public static TemplateFragment newInstance() {
        TemplateFragment fragment = new TemplateFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mCollectionView = (SwipeMenuListView) view.findViewById(R.id.list);

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
        requestByIds.setEmpId(PrefUtils.getUser(getActivity()).getEmpId());
        requestByIds.setPageNumber(pageNumber);
        Intent intent = new Intent(getActivity(), RestAPIClientService.class);
        intent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.TEMPLATE_LIST);
        intent.putExtra(App.TEMPLATE_LIST, requestByIds);
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


    public void onEvent(final TemplateList templateList) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.v("status", templateList.getStatus());

                showProgress(false);

                if (EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().unregister(this);

                if (templateList.getStatus().equals("Success")) {
                    if (templateList.getTemplateDetail().size() > 0) {
                        if (employeeTemplateListAdapter == null || pageNumber == 0) {
                            templateDetails = templateList.getTemplateDetail();
                            employeeTemplateListAdapter = new EmployeeTemplateListAdapter(TemplateFragment.this, templateDetails);
                            mCollectionView.setAdapter(employeeTemplateListAdapter);
                        } else {
                            showLoadMoreProgress(false);
                            mIsLoadingMore = false;
                            templateDetails.addAll(templateList.getTemplateDetail());
                            employeeTemplateListAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    showLoadMoreProgress(false);
                    mIsLoadingMore = false;
                    mCollectionView.setOnScrollListener(null);
                }

                SwipeMenuCreator creator = new SwipeMenuCreator() {

                    @Override
                    public void create(SwipeMenu menu) {


                        SwipeMenuItem view = new SwipeMenuItem(
                                getActivity());
                        view.setBackground(new ColorDrawable(Color.rgb(0x3F,
                                0x51, 0xB5)));
                        view.setWidth(dp2px(75));
                        view.setIcon(android.R.drawable.ic_menu_view);
//                        markAsServed.setTitle("DELETE");
                        menu.addMenuItem(view);

                        SwipeMenuItem delete = new SwipeMenuItem(
                                getActivity());
                        delete.setBackground(new ColorDrawable(Color.rgb(0xE9,
                                0x1E, 0x63)));
                        delete.setWidth(dp2px(75));
                        delete.setIcon(android.R.drawable.ic_menu_delete);
//                        markAsServed.setTitle("DELETE");
                        menu.addMenuItem(delete);
                    }
                };

                mCollectionView.setMenuCreator(creator);
                mCollectionView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

                    @Override
                    public void onSwipeStart(int position) {

                    }

                    @Override
                    public void onSwipeEnd(int position) {

                    }
                });

                mCollectionView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                        final TemplateDetail templateDetail = employeeTemplateListAdapter.getItem(position);
                        switch (index) {
                            case 0:
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle(templateDetail.getTemplateName());
                                builder.setIcon(R.mipmap.ic_launcher);
                                builder.setMessage(templateDetail.getTemplateContent())
                                        .setPositiveButton("Use Template", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent returnIntent = new Intent();
                                                returnIntent.putExtra("result", getArguments().getString("data") + " " + templateDetail.getTemplateContent());
                                                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                                                getActivity().finish();
                                            }
                                        })
                                        .setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // User declined.
                                                dialogInterface.dismiss();
                                            }
                                        });
                                builder.setCancelable(false);
                                builder.show();
                                break;
                            case 1:
                                if (templateDetail.getIsPersonal()) {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                    builder1.setTitle(templateDetail.getTemplateName());
                                    builder1.setIcon(R.mipmap.ic_launcher);
                                    builder1.setMessage(templateDetail.getTemplateContent())
                                            .setPositiveButton("Delete Template?", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    delete(templateDetail);
                                                    templateList.getTemplateDetail().remove(position);
                                                }
                                            })
                                            .setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    // User declined.
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                    builder1.setCancelable(false);
                                    builder1.show();
                                } else {
                                    Toast.makeText(getActivity(), "You can't delete default Templates.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                        return false;
                    }
                });


                mCollectionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TemplateDetail templateDetail = (TemplateDetail) parent.getAdapter().getItem(position);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", getArguments().getString("data") + " " + templateDetail.getTemplateContent());
                        getActivity().setResult(Activity.RESULT_OK, returnIntent);
                        getActivity().finish();
                    }
                });

            }
        });
    }

    private void delete(TemplateDetail templateDetail) {

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        TemplateMaster templateMaster = new TemplateMaster(new TemplateMaster(templateDetail.getTemplateId(), PrefUtils.getUser(getActivity()).getEmpId()));
        Intent intent = new Intent(getActivity(), RestAPIClientService.class);
        intent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.DELETE_TEMPLATE);
        intent.putExtra(App.DELETE_TEMPLATE, templateMaster);
        WakefulIntentService.sendWakefulWork(getActivity(), intent);
    }

    public void onEvent(final ResponseResult e) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                if (EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().unregister(this);

                if (e.getStatus().equals("Success")) {
                    employeeTemplateListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
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
        if (totalItemCount == 0 || employeeTemplateListAdapter == null) {
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