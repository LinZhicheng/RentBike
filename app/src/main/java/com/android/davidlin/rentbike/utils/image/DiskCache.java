package com.android.davidlin.rentbike.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by I326996 on 1/31/2016.
 */
public class DiskCache implements ImageCache {
    static String cacheDir;

    public DiskCache() {
        cacheDir = Environment.getDataDirectory().getAbsolutePath() + "/RentBike/cache";
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(cacheDir + "/" + url);
        } catch (Exception e) {
            bitmap = null;
        }
        return bitmap;
    }

    @Override
    public void put(String url, Bitmap bmp) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(cacheDir + "/" + url);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
