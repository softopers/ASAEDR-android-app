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

package com.softopers.asaedr.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.softopers.asaedr.R;

/**
 * An assortment of UI helpers.
 */
public class UIUtils {
    public static final String TARGET_FORM_FACTOR_ACTIVITY_METADATA =
            "com.google.samples.apps.iosched.meta.TARGET_FORM_FACTOR";
    public static final String TARGET_FORM_FACTOR_HANDSET = "handset";
    public static final String TARGET_FORM_FACTOR_TABLET = "tablet";
    private static final String TAG = UIUtils.class.getSimpleName();
    private static final int[] RES_IDS_ACTION_BAR_SIZE = {R.attr.actionBarSize};

    public static boolean isTablet(Context context) {
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static void enableDisableActivitiesByFormFactor(Context context) {
        final PackageManager pm = context.getPackageManager();
        boolean isTablet = isTablet(context);

        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);
            if (pi == null) {
                Log.e(TAG, "No package info found for our own package.");
                return;
            }

            final ActivityInfo[] activityInfos = pi.activities;
            for (ActivityInfo info : activityInfos) {
                String targetDevice = null;
                if (info.metaData != null) {
                    targetDevice = info.metaData.getString(TARGET_FORM_FACTOR_ACTIVITY_METADATA);
                }
                boolean tabletActivity = TARGET_FORM_FACTOR_TABLET.equals(targetDevice);
                boolean handsetActivity = TARGET_FORM_FACTOR_HANDSET.equals(targetDevice);

                boolean enable = !(handsetActivity && isTablet)
                        && !(tabletActivity && !isTablet);

                String className = info.name;
                pm.setComponentEnabledSetting(
                        new ComponentName(context, Class.forName(className)),
                        enable
                                ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "No package info found for our own package.", e);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Activity not found within package.", e);
        }
    }

    public static void setAccessibilityIgnore(View view) {
        view.setClickable(false);
        view.setFocusable(false);
        view.setContentDescription("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }

    /**
     * Calculates the Action Bar height in pixels.
     */
    public static int calculateActionBarSize(Context context) {
        if (context == null) {
            return 0;
        }

        Resources.Theme curTheme = context.getTheme();
        if (curTheme == null) {
            return 0;
        }

        TypedArray att = curTheme.obtainStyledAttributes(RES_IDS_ACTION_BAR_SIZE);
        if (att == null) {
            return 0;
        }

        float size = att.getDimension(0, 0);
        att.recycle();
        return (int) size;
    }
}
