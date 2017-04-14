package com.codepath.skylinepropertyanimationdemo;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Author: agoenka
 * Created At: 4/13/2017
 * Version: ${VERSION}
 */

public class ExplodeAnimationFragment extends Fragment {

    private RecyclerView mRvExplode;

    public static ExplodeAnimationFragment newInstance() {
        Bundle args = new Bundle();
        ExplodeAnimationFragment fragment = new ExplodeAnimationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_explode_animation, container, false);
        mRvExplode = (RecyclerView) view.findViewById(R.id.rvExplode);
        mRvExplode.setLayoutManager(new GridLayoutManager(container.getContext(), 8));
        mRvExplode.setAdapter(new Adapter());
        return view;
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explode, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Animation animation = new AlphaAnimation(1, 0);
            animation.setDuration(100);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.REVERSE);

            holder.itemView.startAnimation(animation);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    explode(v);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 64;
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            holder.itemView.clearAnimation();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void explode(View itemView) {
        final Rect rect = new Rect();
        itemView.getGlobalVisibleRect(rect);

        Transition explode = new Explode();
        explode.setEpicenterCallback(new Transition.EpicenterCallback() {
            @Override
            public Rect onGetEpicenter(Transition transition) {
                return rect;
            }
        });
        explode.excludeTarget(itemView, true);

        Transition fade = new Fade()
                .addTarget(itemView);

        TransitionSet transitionSet = new TransitionSet()
                .addTransition(explode)
                .addTransition(fade)
                .setInterpolator(new AccelerateDecelerateInterpolator());

        TransitionManager.beginDelayedTransition(mRvExplode, transitionSet);

        mRvExplode.setAdapter(null);
    }
}
