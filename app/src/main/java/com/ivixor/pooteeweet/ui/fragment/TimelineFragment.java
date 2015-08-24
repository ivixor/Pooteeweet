package com.ivixor.pooteeweet.ui.fragment;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ivixor.pooteeweet.R;
import com.ivixor.pooteeweet.ui.TimelineAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivixor on 24.08.2015.
 */
public class TimelineFragment extends BaseFragment {

    public static final String TAG = TimelineFragment.class.getSimpleName();
    public static final String TITLE = "Timeline";

    private RecyclerView timelineRecyclerView;
    private TimelineAdapter adapter;

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public String getFragmentTitle() {
        return TITLE;
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_timeline;
    }

    @Override
    protected void prepareViews(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_timeline);
        adapter = new TimelineAdapter(populateData());
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void customizeToolbar(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setTitle(getFragmentTitle());
        }
    }

    @Override
    protected void customizeViews(View rootView) { }

    private List<String> populateData() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add("data" + (i + 1));
        }
        return items;
    }
}
