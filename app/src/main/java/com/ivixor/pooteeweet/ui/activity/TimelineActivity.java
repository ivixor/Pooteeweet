package com.ivixor.pooteeweet.ui.activity;

import android.support.v7.widget.Toolbar;

import com.ivixor.pooteeweet.R;
import com.ivixor.pooteeweet.ui.fragment.TimelineFragment;


public class TimelineActivity extends BaseActivity {

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_timeline;
    }

    @Override
    protected void additionalCustomization() {
        replaceFragmentBackStack(new TimelineFragment());
    }
}
