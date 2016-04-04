package com.android.davidlin.rentbike.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.android.davidlin.rentbike.RentBike;

public class LargeImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = new ImageView(this);
        String url = getIntent().getStringExtra("imageUrl");
        RentBike.mImageLoader.displayImage(url, imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setContentView(imageView);
    }
}
