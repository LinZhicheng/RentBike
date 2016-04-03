package com.android.davidlin.rentbike.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by I326996 on 1/31/2016.
 */
public class DiskCache implements ImageCache {
    static String cacheDir;

    public DiskCache(String cacheDir) {
        DiskCache.cacheDir = cacheDir;
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(cacheDir + "/" + url.hashCode());
        } catch (Exception e) {
            Log.e("DiskCache get", e.getMessage());
            bitmap = null;
        }
        return bitmap;
    }

    @Override
    public void put(String url, Bitmap bmp) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(cacheDir + "/" + url.hashCode());
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (IOException e) {
            Log.e("DiskCache put", e.getMessage());
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.e("DiskCache put", e.getMessage());
                }
            }
        }
    }
}
