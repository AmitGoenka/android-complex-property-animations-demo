package com.codepath.skylineanimations;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SkylineActivity extends AppCompatActivity implements StarsAdapter.StarsListener {

    private RelativeLayout rlSkyLayout;
    private ImageView ivSun;
    private ImageView ivCloud1;
    private ImageView ivCloud2;
    private ImageView ivBird;
    private ImageView ivWheel;
    private RecyclerView rvStars;

    private int ANIMATION_DURATION = 3000;
    private String BG_START_COLOR = "#66ccff";
    private String BG_END_COLOR = "#006699";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skyline);
        // Lookup views
        setupViews();
        // Animate the sun in sky
        animateSun();
        // Animate the wheel with rotation
        animateWheel();
        // Move clouds around
        moveClouds();
        // Animate bird
        animateBird();
        // Darken sky between day and night
        darkenSky();
        // Transition action bar
        animateActionBar();
        // Setup Activity Window Animations
        setupWindowAnimations();
    }

    private void setupViews() {
        ivWheel = (ImageView) findViewById(R.id.ivWheel);
        ivSun = (ImageView) findViewById(R.id.ivSun);
        rlSkyLayout = (RelativeLayout) findViewById(R.id.rlSkyLayout);
        ivCloud1 = (ImageView) findViewById(R.id.ivCloud1);
        ivCloud2 = (ImageView) findViewById(R.id.ivCloud2);
        ivBird = (ImageView) findViewById(R.id.ivBird);
        rvStars = (RecyclerView) findViewById(R.id.rvStars);

        rvStars.setLayoutManager(new GridLayoutManager(this, 16));
        rvStars.setAdapter(new StarsAdapter(this, 32, this));

        ivWheel.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SkylineActivity.this, ArtActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SkylineActivity.this);
                startActivity(intent, options.toBundle());
            }
        });
    }

    public void animateSun() {
        //load the sun movement animation
        Animator sun = AnimatorInflater.loadAnimator(this, R.animator.sun_swing);
        //set the view as target
        sun.setTarget(ivSun);
        //start the animation
        sun.start();
    }

    public void animateWheel() {
        //load the wheel animation
        AnimatorSet wheelSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.wheel_spin);
        //set the view as target
        wheelSet.setTarget(ivWheel);
        //start the animation
        wheelSet.start();
    }

    public void moveClouds() {
        //move clouds
        ObjectAnimator cloud1Anim = ObjectAnimator.ofFloat(ivCloud1, View.X, -350);
        cloud1Anim.setDuration(ANIMATION_DURATION);
        cloud1Anim.setRepeatCount(ValueAnimator.INFINITE);
        cloud1Anim.setRepeatMode(ValueAnimator.REVERSE);
        cloud1Anim.start();

        // other cloud
        ObjectAnimator cloud2Anim = ObjectAnimator.ofFloat(ivCloud2, View.X, -300);
        cloud2Anim.setDuration(ANIMATION_DURATION);
        cloud2Anim.setRepeatCount(ValueAnimator.INFINITE);
        cloud2Anim.setRepeatMode(ValueAnimator.REVERSE);
        cloud2Anim.start();

        AnimatorSet set = new AnimatorSet();
        set.playTogether(cloud1Anim, cloud2Anim);
        set.start();
    }

    private void animateBird() {
        ivBird.setX(-250f);
        // Get screen width
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // Animate bird off screen to the right
        ivBird.animate()
                .x(metrics.widthPixels + 50)
                .setStartDelay(1000)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateBird();
                    }
                })
                .start();
    }

    public void darkenSky() {
        //darken sky
        ObjectAnimator skyAnim = ObjectAnimator.ofInt(rlSkyLayout, "backgroundColor", Color.parseColor(BG_START_COLOR), Color.parseColor(BG_END_COLOR));
        skyAnim.setDuration(ANIMATION_DURATION);
        skyAnim.setRepeatCount(ValueAnimator.INFINITE);
        skyAnim.setRepeatMode(ValueAnimator.REVERSE);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();
    }

    private void animateActionBar() {
        int colorFrom = Color.parseColor(BG_START_COLOR);
        int colorTo = Color.parseColor(BG_END_COLOR);
        ValueAnimator actionBarColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        actionBarColorAnim.setDuration(ANIMATION_DURATION); // milliseconds
        actionBarColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        actionBarColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        actionBarColorAnim.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable((int) animator.getAnimatedValue()));
                        }
                    }
                });
        actionBarColorAnim.start();
    }

    @Override
    public void onClickStars(View v) {
        final Rect rect = new Rect();
        v.getGlobalVisibleRect(rect);

        // Get Explode Transition
        Transition explode = new Explode();
        // For Explode Transition, use the clicked view bounds as the epi-center
        explode.setEpicenterCallback(new Transition.EpicenterCallback() {
            @Override
            public Rect onGetEpicenter(Transition transition) {
                return rect;
            }
        });
        // Keep the current view from exploding
        explode.excludeTarget(v, true);

        Transition fade = new Fade(Fade.OUT).addTarget(v);

        TransitionSet set = new TransitionSet()
                .addTransition(explode)
                .addTransition(fade)
                .setDuration(2000)
                .setInterpolator(new AccelerateInterpolator());

        TransitionManager.beginDelayedTransition(rlSkyLayout, set);
        rvStars.setAdapter(null);
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setDuration(500);
        getWindow().setExitTransition(slide);
    }
}