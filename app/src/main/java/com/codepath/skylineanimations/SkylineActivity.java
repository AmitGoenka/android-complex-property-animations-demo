package com.codepath.skylineanimations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClickStars(View v) {
        rvStars.setAdapter(null);
    }
}