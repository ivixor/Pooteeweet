package com.ivixor.pooteeweet.ui.fragment.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by ivixor on 24.08.2015.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (getTitleView() != null) {
            builder.setCustomTitle(getTitleView());
        } else if (!TextUtils.isEmpty(getTitle())) {
            builder.setTitle(getTitle());
        }

        if (getRootView() != null) {
            builder.setView(getRootView());
        }

        return builder.create();
    }

    protected abstract View getTitleView();

    protected abstract String getTitle();

    protected abstract View getRootView();

    public abstract String getDialogTag();
}
