package com.codepath.skylinepropertyanimationdemo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SkylinePropertyAnimationActivity extends AppCompatActivity {

    private RelativeLayout rlSkyLayout;
    private ImageView ivSun;
    private ImageView ivCloud1;
    private ImageView ivCloud2;
    private ImageView ivBird;
    private ImageView ivWheel;
    private ImageView ivWindow;

    private int ANIMATION_DURATION = 3000;
    private String BG_START_COLOR = "#66ccff";
    private String BG_END_COLOR = "#006699";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skyline_property_animation);
        // Lookup views
        setupViews();
        // Animate the wheel with rotation
        animateWheel();
        // Animate the sun in sky
        animateSun();
        // Move clouds around
        moveClouds();
        // Animate bird
        animateBird();
        // Darken sky between day and night
        darkenSky();
        // Transition action bar
        animateActionBar();
    }

    private void setupViews() {
        ivWheel = (ImageView) findViewById(R.id.ivWheel);
        ivSun = (ImageView) findViewById(R.id.ivSun);
        rlSkyLayout = (RelativeLayout) findViewById(R.id.rlSkyLayout);
        ivCloud1 = (ImageView) findViewById(R.id.ivCloud1);
        ivCloud2 = (ImageView) findViewById(R.id.ivCloud2);
        ivBird = (ImageView) findViewById(R.id.ivBird);
        ivWindow = (ImageView) findViewById(R.id.window);
    }

    public void animateWheel() {
        //load the wheel animation
        AnimatorSet wheelSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.wheel_spin);
        //set the view as target
        wheelSet.setTarget(ivWheel);
        //start the animation
        wheelSet.start();
    }

    public void animateSun() {
        //load the sun movement animation
        AnimatorSet sunSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.sun_swing);
        //set the view as target
        sunSet.setTarget(ivSun);
        //start the animation
        sunSet.start();
    }

    public void darkenSky() {
        //darken sky
        ValueAnimator skyAnim = ObjectAnimator.ofInt(rlSkyLayout, "backgroundColor", Color.parseColor(BG_START_COLOR), Color.parseColor(BG_END_COLOR));
        skyAnim.setDuration(ANIMATION_DURATION);
        skyAnim.setRepeatCount(ValueAnimator.INFINITE);
        skyAnim.setRepeatMode(ValueAnimator.REVERSE);
        skyAnim.setEvaluator(new ArgbEvaluator());
        skyAnim.start();
    }

    public void moveClouds() {
        //move clouds
        ObjectAnimator cloudAnim = ObjectAnimator.ofFloat(ivCloud1, View.X, -350);
        cloudAnim.setDuration(ANIMATION_DURATION);
        cloudAnim.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnim.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim.start();
        // other cloud
        ObjectAnimator cloudXAnim2 = ObjectAnimator.ofFloat(ivCloud2, View.X, -300);
        cloudXAnim2.setDuration(ANIMATION_DURATION);
        cloudXAnim2.setRepeatCount(ValueAnimator.INFINITE);
        cloudXAnim2.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator cloudYAnim2 = ObjectAnimator.ofFloat(ivCloud2, View.TRANSLATION_Y, -200);
        cloudYAnim2.setDuration(ANIMATION_DURATION);
        cloudYAnim2.setRepeatCount(ValueAnimator.INFINITE);
        cloudYAnim2.setRepeatMode(ValueAnimator.REVERSE);

        AnimatorSet cloud2Set = new AnimatorSet();
        cloud2Set.playSequentially(cloudXAnim2, cloudYAnim2);
        cloud2Set.start();
    }

    private void animateBird() {
        ivBird.setX(-200f);
        // Get screen width
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // Animate bird off screen to the right
        ivBird.animate().x(metrics.widthPixels + 50).
                setStartDelay(1000).
                setDuration(ANIMATION_DURATION).
                setInterpolator(new AccelerateInterpolator()).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateBird();
                    }
                }).start();
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
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable((int) animator.getAnimatedValue()));
                    }
                });
        actionBarColorAnim.start();
    }

}
