package com.android.davidlin.rentbike.utils.image;

import android.graphics.Bitmap;

/**
 * Created by I326996 on 1/31/2016.
 */
public class DoubleCache implements ImageCache {
    ImageCache mMemoryCache;
    DiskCache mDiskCache;

    public DoubleCache(String cacheDir) {
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(cacheDir);
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap == null) {
            bitmap = mDiskCache.get(url);
        }
        return bitmap;
    }

    @Override
    public void put(String url, Bitmap bmp) {
        mMemoryCache.put(url, bmp);
        mDiskCache.put(url, bmp);
    }
}
