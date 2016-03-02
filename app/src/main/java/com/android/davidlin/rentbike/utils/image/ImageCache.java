package com.android.davidlin.rentbike.utils.image;

import android.graphics.Bitmap;

public interface ImageCache {
    Bitmap get(String url);

    void put(String url, Bitmap bmp);
}