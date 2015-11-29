/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.softopers.asaedr.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.softopers.asaedr.R;
import com.softopers.asaedr.model.RequestByIds;
import com.softopers.asaedr.model.ResponseLogout;
import com.softopers.asaedr.ui.admin.employee.EmployeesActivity;
import com.softopers.asaedr.ui.admin.reporting.ReportsActivity;
import com.softopers.asaedr.ui.user.MainActivity;
import com.softopers.asaedr.util.LUtils;
import com.softopers.asaedr.util.PrefUtils;
import com.softopers.asaedr.util.RecentTasksStyler;
import com.softopers.asaedr.util.UIUtils;
import com.softopers.asaedr.webapi.RestAPIClientService;
import com.softopers.asaedr.widget.ScrimInsetsScrollView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends ActionBarActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    // symbols for navdrawer items (indices must correspond to array below). This is
    // not a list of items that are necessarily *present* in the Nav Drawer; rather,
    // it's a list of all possible items.
    protected static final int NAVDRAWER_ITEM_REPORTING = 0;
    protected static final int NAVDRAWER_ITEM_MESSAGES = 1;
    protected static final int NAVDRAWER_ITEM_REPORTS = 2;
    protected static final int NAVDRAWER_ITEM_EMPLOYEES = 3;
    protected static final int NAVDRAWER_ITEM_CHANGE_PASSWORD = 4;
    protected static final int NAVDRAWER_ITEM_LOGOUT = 5;
    protected static final int NAVDRAWER_ITEM_CONTACT_US = 6;
    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_SEPARATOR = -2;
    protected static final int NAVDRAWER_ITEM_SEPARATOR_SPECIAL = -3;
    private static final String TAG = BaseActivity.class.getSimpleName();
    // titles for navdrawer items (indices must correspond to the above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_item_reporting,
            R.string.navdrawer_item_messages,
            R.string.navdrawer_item_reports,
            R.string.navdrawer_item_employees,
            R.string.navdrawer_item_settings,
            R.string.navdrawer_item_logout,
            R.string.navdrawer_item_contact_us,


    };
    // icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[]{
            R.drawable.ic_drawer_add_report, // Reporting
            R.drawable.ic_drawer_messages, // Messages
            R.drawable.ic_drawer_reports, // Reports
            R.drawable.ic_drawer_employees, // Employees
            R.drawable.ic_drawer_settings, // Profile
            R.drawable.ic_drawer_logout, // Logout
            R.drawable.ic_drawer_contact_us, // Contact Us
    };
    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;
    // fade in and fade out durations for the main content when switching between
    // different Activities of the app through the Nav Drawer
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;
    boolean doubleBackToExitPressedOnce = false;
    // Navigation drawer:
    private DrawerLayout mDrawerLayout;
    private Handler mHandler;
    // When set, these components will be shown/hidden in sync with the action bar
    // to implement the "quick recall" effect (the Action Bar and the header views disappear
    // when you scroll down a list, and reappear quickly when you scroll up).
    private ArrayList<View> mHideableHeaderViews = new ArrayList<>();
    // list of navdrawer items that were actually added to the navdrawer, in order
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<>();
    // views that correspond to each navdrawer item, null if not yet created
    private View[] mNavDrawerItemViews = null;
    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;
    // A Runnable that we should execute when the navigation drawer finishes its closing animation
    private Runnable mDeferredOnDrawerClosedRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecentTasksStyler.styleRecentTasksEntry(this);

        // Check if the user has been LoggedIn; if not, show it.
        if (!PrefUtils.isLoggedIn(this)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mHandler = new Handler();

        // Enable or disable each Activity depending on the form factor. This is necessary
        // because this app uses many implicit intents where we don't name the exact Activity
        // in the Intent, so there should only be one enabled Activity that handles each
        // Intent in the app.
        UIUtils.enableDisableActivitiesByFormFactor(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        LUtils.getInstance(this);
        getResources().getColor(R.color.theme_primary_dark);
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    /**
     * Sets up the navigation drawer as appropriate. Note that the nav drawer will be
     * different depending on whether the attendee indicated that they are attending the
     * event on-site vs. attending remotely.
     */
    private void setupNavDrawer() {
        // What nav drawer item should be selected?
        int selfItem = getSelfNavDrawerItem();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.theme_primary_dark));
        ScrimInsetsScrollView navDrawer = (ScrimInsetsScrollView)
                mDrawerLayout.findViewById(R.id.navdrawer);
        if (selfItem == NAVDRAWER_ITEM_INVALID) {
            // do not show a nav drawer
            if (navDrawer != null) {
                ((ViewGroup) navDrawer.getParent()).removeView(navDrawer);
            }
            mDrawerLayout = null;
            return;
        }

        if (navDrawer != null) {
            final View chosenAccountContentView = findViewById(R.id.chosen_account_content_view);
            final View chosenAccountView = findViewById(R.id.chosen_account_view);
            final int navDrawerChosenAccountHeight = getResources().getDimensionPixelSize(
                    R.dimen.navdrawer_chosen_account_height);
            navDrawer.setOnInsetsCallback(new ScrimInsetsScrollView.OnInsetsCallback() {
                @Override
                public void onInsetsChanged(Rect insets) {
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
                            chosenAccountContentView.getLayoutParams();
                    lp.topMargin = insets.top;
                    chosenAccountContentView.setLayoutParams(lp);

                    ViewGroup.LayoutParams lp2 = chosenAccountView.getLayoutParams();
                    lp2.height = navDrawerChosenAccountHeight + insets.top;
                    chosenAccountView.setLayoutParams(lp2);
                }
            });
        }

        if (mActionBarToolbar != null) {
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            });
        }

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                // run deferred action, if we have one
                if (mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }
                onNavDrawerStateChanged();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                onNavDrawerStateChanged();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                onNavDrawerStateChanged();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                onNavDrawerSlide();
            }
        });

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        // populate the nav drawer with the correct items
        populateNavDrawer();

        // When the user runs the app for the first time, we want to land them with the
        // navigation drawer open. But just the first time.
        if (!PrefUtils.isWelcomeDone(this)) {
            // first run of the app starts with the nav drawer open
            PrefUtils.markWelcomeDone(this);
            mDrawerLayout.openDrawer(Gravity.START);
        }

        if (PrefUtils.isLoggedIn(this)) {
            if (PrefUtils.getEmail(this) != null) {
                TextView profile_email_text = (TextView) findViewById(R.id.profile_email_text);
                profile_email_text.setText(PrefUtils.getEmail(this));
            }
            if (PrefUtils.getUser(getApplicationContext()).getEmpName() != null) {
                TextView profile_name_text = (TextView) findViewById(R.id.profile_name_text);
                profile_name_text.setText(PrefUtils.getUser(getApplicationContext()).getEmpName());
            }

            ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
            if (!PrefUtils.getUser(getApplicationContext()).getImage().equals("")) {
                Picasso.with(getApplicationContext())
                        .load("http://app.allsportsacademy.in/Image/" + PrefUtils.getUser(getApplicationContext()).getImage())
                        .placeholder(R.drawable.person_image_empty)
                        .into(profile_image);
            } else {
                profile_image.setImageResource(R.drawable.person_image_empty);
            }

        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    // Subclasses can override this for custom behavior
    protected void onNavDrawerStateChanged() {
    }

    protected void onNavDrawerSlide() {
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
    }

    /**
     * Populates the navigation drawer with the appropriate items.
     */
    private void populateNavDrawer() {
        mNavDrawerItems.clear();
        // decide which items will appear in the nav drawer
        if (PrefUtils.getUserRole(getApplicationContext()).equals(App.USER)) {
            mNavDrawerItems.add(NAVDRAWER_ITEM_REPORTING);
            mNavDrawerItems.add(NAVDRAWER_ITEM_MESSAGES);
        } else {
            mNavDrawerItems.add(NAVDRAWER_ITEM_REPORTS);
            mNavDrawerItems.add(NAVDRAWER_ITEM_MESSAGES);
            mNavDrawerItems.add(NAVDRAWER_ITEM_EMPLOYEES);
        }
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);
        mNavDrawerItems.add(NAVDRAWER_ITEM_CHANGE_PASSWORD);
        mNavDrawerItems.add(NAVDRAWER_ITEM_LOGOUT);
//        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);
//        mNavDrawerItems.add(NAVDRAWER_ITEM_CONTACT_US);
        createNavDrawerItems();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else if (getSelfNavDrawerItem() != NAVDRAWER_ITEM_INVALID) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }

    private void createNavDrawerItems() {
        ViewGroup mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }
    }

    /**
     * Sets up the given navdrawer item's appearance to the selected state. Note: this could
     * also be accomplished (perhaps more cleanly) with state-based layouts.
     */
    private void setSelectedNavDrawerItem(int itemId) {
        if (mNavDrawerItemViews != null) {
            for (int i = 0; i < mNavDrawerItemViews.length; i++) {
                if (i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    formatNavDrawerItem(mNavDrawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        populateNavDrawer();
        invalidateOptionsMenu();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else {
            Log.w(TAG, "No view with ID main_content to fade in.");
        }
    }

    private void goToNavDrawerItem(int item) {
        Intent intent;
        switch (item) {
            case NAVDRAWER_ITEM_REPORTING:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_MESSAGES:
                intent = new Intent(this, MessageListActivity.class);
                if (PrefUtils.getUser(getApplicationContext()).getIsAdmin()) {
                    intent.putExtra("privilages", PrefUtils.getUser(getApplicationContext()).getPrivilage());
                    intent.putExtra("admin", "admin");
                } else {
                    intent.putExtra("user", "user");
                }
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_REPORTS:
                intent = new Intent(this, ReportsActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_EMPLOYEES:
                intent = new Intent(this, EmployeesActivity.class);
                intent.putExtra("employees", PrefUtils.getUser(getApplicationContext()).getPrivilage());
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_CHANGE_PASSWORD:
                intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_LOGOUT:

                if (!EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().register(this);

                Intent employeeRegisterIntent = new Intent(getApplicationContext(), RestAPIClientService.class);
                RequestByIds requestByIds = new RequestByIds();
                requestByIds.setEmpId(PrefUtils.getUser(getApplicationContext()).getEmpId());
                employeeRegisterIntent.putExtra(RestAPIClientService.Operation.class.getName(), RestAPIClientService.Operation.LOGOUT);
                employeeRegisterIntent.putExtra(App.LOGOUT, requestByIds);
                WakefulIntentService.sendWakefulWork(getApplicationContext(), employeeRegisterIntent);

                PrefUtils.clearPref(getApplicationContext());
                PrefUtils.markTosAccepted(getApplicationContext());
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_CONTACT_US:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void onEvent(final ResponseLogout e) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Log.v(TAG, e.getStatus());

                if (EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().unregister(this);

                if (e.getStatus().equals("Success")) {
                    Toast.makeText(getApplicationContext(), "Logged out Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }

        if (isSpecialItem(itemId)) {
            goToNavDrawerItem(itemId);
        } else {
            // launch the target Activity after a short delay, to allow the close animation to play
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToNavDrawerItem(itemId);
                }
            }, NAVDRAWER_LAUNCH_DELAY);

            // change the active item on the list so the user can see the item changed
            setSelectedNavDrawerItem(itemId);
            // fade out the main content
            View mainContent = findViewById(R.id.main_content);
            if (mainContent != null) {
                mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
            }
        }

        mDrawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Verifies the proper version of Google Play Services exists on the device.
//        PlayServicesUtils.checkGooglePlaySevices(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
        boolean selected = getSelfNavDrawerItem() == itemId;
        int layoutToInflate;
        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else if (itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else {
            layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (isSeparator(itemId)) {
            // we are done
            UIUtils.setAccessibilityIgnore(view);
            return view;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID.length ?
                NAVDRAWER_ICON_RES_ID[itemId] : 0;
        int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID.length ?
                NAVDRAWER_TITLE_RES_ID[itemId] : 0;

        // set icon and text
        iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        if (iconId > 0) {
            iconView.setImageResource(iconId);
        }
        titleView.setText(getString(titleId));

        formatNavDrawerItem(view, itemId, selected);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });

        return view;
    }

    private boolean isSpecialItem(int itemId) {
        return itemId == NAVDRAWER_ITEM_CHANGE_PASSWORD;
    }

    private boolean isSeparator(int itemId) {
        return itemId == NAVDRAWER_ITEM_SEPARATOR || itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL;
    }

    private void formatNavDrawerItem(View view, int itemId, boolean selected) {
        if (isSeparator(itemId)) {
            // not applicable
            return;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        if (selected) {
            view.setBackgroundResource(R.drawable.selected_navdrawer_item_background);
        }

        // configure its appearance according to whether or not it's selected
        titleView.setTextColor(selected ?
                getResources().getColor(R.color.navdrawer_text_color_selected) :
                getResources().getColor(R.color.navdrawer_text_color));
        iconView.setColorFilter(selected ?
                getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                getResources().getColor(R.color.navdrawer_icon_tint));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }

    protected void registerHideableHeaderView(View hideableHeaderView) {
        if (!mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.add(hideableHeaderView);
        }
    }
}
