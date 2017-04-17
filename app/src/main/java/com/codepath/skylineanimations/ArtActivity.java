package com.codepath.skylineanimations;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ArtActivity extends AppCompatActivity {

    private CoordinatorLayout clArtLayout;
    private ImageView ivArt;
    private int baseHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art);
        setupViews();
    }

    private void setupViews() {
        clArtLayout = (CoordinatorLayout) findViewById(R.id.clArtLayout);
        ivArt = (ImageView) findViewById(R.id.ivArt);

        ivArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeImageTransform imageTransform = new ChangeImageTransform();
                imageTransform.setDuration(1000);

                ChangeBounds changeBounds = new ChangeBounds();
                changeBounds.setDuration(1000);

                TransitionSet set = new TransitionSet();
                set.setOrdering(TransitionSet.ORDERING_TOGETHER);
                set.addTransition(imageTransform);
                set.addTransition(changeBounds);

                TransitionManager.go(new Scene(clArtLayout), set);

                if (baseHeight == 0) {
                    baseHeight = ivArt.getHeight();
                }

                changeImageScale(ivArt);
                changeImageSize(ivArt, baseHeight);
            }
        });
    }

    private void changeImageSize(View v, int baseHeight) {
        if (v.getHeight() == baseHeight) {
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            v.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = baseHeight;
            v.setLayoutParams(params);
        }
    }

    private void changeImageScale(ImageView v) {
        if (v.getScaleType() == ImageView.ScaleType.CENTER) {
            v.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            v.setScaleType(ImageView.ScaleType.CENTER);
        }
    }

}