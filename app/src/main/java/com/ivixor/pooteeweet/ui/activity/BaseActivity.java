package com.ivixor.pooteeweet.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.ivixor.pooteeweet.R;
import com.ivixor.pooteeweet.ui.fragment.BaseFragment;

/**
 * Created by ivixor on 24.08.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        getToolbarActionBar();

        additionalCustomization();
    }

    protected abstract int getFragmentContainerId();

    protected abstract int getContentViewId();

    protected abstract void additionalCustomization();

    public ActionBar getToolbarActionBar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setNavigationIcon(R.mipmap.ic_launcher);
                setSupportActionBar(toolbar);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }
        return getSupportActionBar();
    }

    protected void replaceFragmentBackStack(BaseFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(getFragmentContainerId(), fragment)
                //.addToBackStack(fragment.getFragmentTag())
                .commit();
    }

    protected void addFragmentBackStack(BaseFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(getFragmentContainerId(), fragment)
                .addToBackStack(fragment.getFragmentTag())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        getSupportFragmentManager().popBackStack();
        overridePendingTransition(R.anim.stay_activity, R.anim.slide_activity_to_bottom);
    }
}
