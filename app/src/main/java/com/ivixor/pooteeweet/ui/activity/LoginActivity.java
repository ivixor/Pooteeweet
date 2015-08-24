package com.ivixor.pooteeweet.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ivixor.pooteeweet.R;
import com.ivixor.pooteeweet.ui.fragment.LoginFragment;

/**
 * Created by ivixor on 24.08.2015.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void additionalCustomization() {
        replaceFragmentBackStack(new LoginFragment());
    }
}
