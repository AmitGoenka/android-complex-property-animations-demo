package com.codepath.skylineanimations;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

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
        animateStars(holder.itemView);
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

    private void animateStars(final View v) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.2f, 1);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.2f, 1);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(v, scaleX, scaleY);
        scaleAnimator.setInterpolator(new LinearInterpolator());
        scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnimator.setDuration(100);

        Keyframe kfStart = Keyframe.ofFloat(0f, 0f);
        Keyframe kfMiddle = Keyframe.ofFloat(0.5f, 0.1f);
        Keyframe kfLast = Keyframe.ofFloat(1f, 1f);
        PropertyValuesHolder alphaValues = PropertyValuesHolder.ofKeyframe(View.ALPHA, kfStart, kfMiddle, kfLast);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofPropertyValuesHolder(v, alphaValues);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimator.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimator.setDuration(3000);

        final Drawable starDrawable = ((ImageButton) v).getDrawable();
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.WHITE, Color.parseColor("#f9f37b"));
        colorAnimator.setInterpolator(new LinearInterpolator());
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.setDuration(100);
        colorAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        if (starDrawable != null) {
                            Drawable changedDrawable = starDrawable.mutate();
                            changedDrawable = DrawableCompat.wrap(changedDrawable);
                            DrawableCompat.setTint(changedDrawable, (Integer) animator.getAnimatedValue());
                        }
                    }
                });

        AnimatorSet starAnim = new AnimatorSet();
        starAnim.playTogether(alphaAnimator, scaleAnimator, colorAnimator);
        starAnim.start();
    }

}