package com.ivixor.pooteeweet.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivixor.pooteeweet.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivixor on 24.08.2015.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineItemHolder> {

    private List<String> timelineItems;

    public TimelineAdapter(List<String> data) {
        timelineItems = new ArrayList<>();
        timelineItems.addAll(data);
    }

    @Override
    public TimelineItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_timeline_item, parent, false);

        return new TimelineItemHolder(view);
    }

    @Override
    public void onBindViewHolder(TimelineItemHolder holder, int position) {
        holder.title.setText(timelineItems.get(position));
    }

    @Override
    public int getItemCount() {
        return timelineItems.size();
    }

    class TimelineItemHolder extends RecyclerView.ViewHolder {

        TextView title;

        public TimelineItemHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textView_title);
        }
    }
}
