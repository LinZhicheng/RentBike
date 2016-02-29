package com.android.davidlin.rentbike.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Include methods using http
 * Created by David Lin on 2015/11/3.
 */
public class HttpUtils {

    private final static String TAG = "HttpUtils";

    /**
     * 使用HttpURLConnection获取图片
     *
     * @param url 图片地址
     * @return 返回Bitmap
     */
    public static Bitmap loadThumbnail(String url) {
        Bitmap bitmap;
        try {
            URL imgUrl = new URL(url);
            // 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            // 将得到的数据转化成InputStream
            InputStream is = urlConn.getInputStream();

            // 将InputStream转换成Bitmap
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

}
