/*
 * Copyright (C) 2021 The Android Open Source Project
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

package com.android.settingslib.collapsingtoolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.settingslib.transition.SettingsTransitionHelper;

/**
 * A base Activity for Settings-specific page transition. Activities extending it will get
 * Settings transition applied.
 */
public abstract class SettingsTransitionActivity extends FragmentActivity {
    private static final String TAG = "SettingsTransitionActivity";
    private static final int DEFAULT_REQUEST = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (BuildCompat.isAtLeastS()) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            SettingsTransitionHelper.applyForwardTransition(this);
            SettingsTransitionHelper.applyBackwardTransition(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void startActivity(Intent intent) {
        if (!BuildCompat.isAtLeastS()) {
            super.startActivity(intent);
            return;
        }
        final Toolbar toolbar = getToolbar();
        if (toolbar == null) {
            Log.w(TAG, "Toolbar is null. Cannot apply settings transition!");
            super.startActivity(intent);
            return;
        }
        super.startActivity(intent, getActivityOptionsBundle(toolbar));

    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        if (!BuildCompat.isAtLeastS()) {
            super.startActivity(intent, options);
            return;
        }
        final Toolbar toolbar = getToolbar();
        if (toolbar == null) {
            Log.w(TAG, "Toolbar is null. Cannot apply settings transition!");
            super.startActivity(intent, options);
            return;
        }
        if (options != null) {
            super.startActivity(intent, getMergedBundleForTransition(options));
            return;
        }
        super.startActivity(intent, getActivityOptionsBundle(toolbar));
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (!BuildCompat.isAtLeastS() || requestCode == DEFAULT_REQUEST) {
            super.startActivityForResult(intent, requestCode);
            return;
        }

        final Toolbar toolbar = getToolbar();
        if (toolbar == null) {
            Log.w(TAG, "Toolbar is null. Cannot apply settings transition!");
            super.startActivityForResult(intent, requestCode);
            return;
        }
        super.startActivityForResult(intent, requestCode, getActivityOptionsBundle(toolbar));
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (!BuildCompat.isAtLeastS() || requestCode == DEFAULT_REQUEST) {
            super.startActivityForResult(intent, requestCode, options);
            return;
        }

        final Toolbar toolbar = getToolbar();
        if (toolbar == null) {
            Log.w(TAG, "Toolbar is null. Cannot apply settings transition!");
            super.startActivityForResult(intent, requestCode, options);
            return;
        }
        if (options != null) {
            super.startActivityForResult(intent, requestCode,
                    getMergedBundleForTransition(options));
            return;
        }
        super.startActivityForResult(intent, requestCode, getActivityOptionsBundle(toolbar));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            // Make the up button behave the same as the back button.
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Subclasses should implement this method and return their {@link Toolbar}.
     */
    public abstract Toolbar getToolbar();

    private Bundle getActivityOptionsBundle(Toolbar toolbar) {
        return ActivityOptions.makeSceneTransitionAnimation(this, toolbar,
                "shared_element_view").toBundle();
    }

    private Bundle getMergedBundleForTransition(@NonNull Bundle options) {
        final Toolbar toolbar = getToolbar();
        final Bundle mergedBundle = new Bundle();
        mergedBundle.putAll(options);
        final Bundle activityOptionsBundle = getActivityOptionsBundle(toolbar);
        if (activityOptionsBundle != null) {
            mergedBundle.putAll(activityOptionsBundle);
        }
        return mergedBundle;
    }
}
