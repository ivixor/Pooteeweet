package com.ivixor.pooteeweet.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ivixor.pooteeweet.R;
import com.ivixor.pooteeweet.ui.activity.BaseActivity;

/**
 * Created by ivixor on 24.08.2015.
 */
public abstract class BaseFragment extends Fragment {

    public abstract String getFragmentTag();

    public abstract String getFragmentTitle();

    protected abstract int getRootLayoutId();

    protected abstract void prepareViews(View rootView);

    protected abstract void customizeToolbar(ActionBar actionBar);

    protected abstract void customizeViews(View rootView);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(getRootLayoutId(), null, false);
        prepareViews(rootView);

        BaseActivity activity = (BaseActivity) getActivity();
        if (activity.getSupportActionBar() != null) {
            customizeToolbar(activity.getToolbarActionBar());
        }
        customizeViews(rootView);

        return rootView;
    }

    protected void replaceFragmentBackStack(BaseFragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(fragment.getFragmentTag())
                .commit();
    }

    protected void replaceFragmentBackStack(BaseFragment fragment, int containerId) {
        getFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.getFragmentTag())
                .commit();
    }
}
