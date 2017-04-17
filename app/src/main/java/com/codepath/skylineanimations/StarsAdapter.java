package com.codepath.skylineanimations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class StarsAdapter extends RecyclerView.Adapter<StarsAdapter.ViewHolder> {

    private Context mContext;
    private int mCount;
    private StarsListener listener;

    interface StarsListener {
        void onClickStars(View v);
    }

    StarsAdapter(final Context context, int count, final StarsListener listener) {
        this.mContext = context;
        this.mCount = count;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_star, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickStars(v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

}