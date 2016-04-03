package com.android.davidlin.rentbike.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by I326996 on 1/31/2016.
 */
public class ImageLoader {
    // Image cache
    ImageCache mImageCache;
    // Thread pool
    //ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    // Cache injection implementation
    public void setImageCache(ImageCache cache) {
        mImageCache = cache;
    }

    public void displayImage(String imageUrl, ImageView imageView) {
        Bitmap bitmap = null;
        if (mImageCache != null) {
            bitmap = mImageCache.get(imageUrl);
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        // No cache, request to download the picture
        submitLoadRequest(imageUrl, imageView);
    }

    private void submitLoadRequest(final String imageUrl, final ImageView imageView) {
        imageView.setTag(imageUrl.hashCode());
        AsyncTask<String, Integer, Bitmap> task = new AsyncTask<String, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                return downloadImage(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(imageUrl.hashCode())) {
                    imageView.setImageBitmap(bitmap);
                }
                if (mImageCache != null) {
                    mImageCache.put(imageUrl, bitmap);
                }
            }
        };
        task.execute(imageUrl);
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = downloadImage(imageUrl);
//                if (bitmap == null) {
//                    return;
//                }
//                if (imageView.getTag().equals(imageUrl.hashCode())) {
//                    imageView.setImageBitmap(bitmap);
//                }
//                if (mImageCache != null) {
//                    mImageCache.put(imageUrl, bitmap);
//                }
//            }
//        });
    }

    public Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
