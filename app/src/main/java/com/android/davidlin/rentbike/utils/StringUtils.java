package com.android.davidlin.rentbike.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Judge strings' type
 * Created by David Lin on 2015/11/11.
 */
public class StringUtils {

    /**
     * 判断手机格式是否正确
     *
     * @param mobiles 手机号码
     * @return 如果是手机号码则返回true, 否则返回false
     */
    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();

    }

    /**
     * 判断email格式是否正确
     *
     * @param email 邮箱
     * @return 如果是邮箱则返回true, 否则返回false
     */
    public static boolean isEmail(String email) {

        String str =
                "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)" +
                        "|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();

    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context 调用此方法的上下文环境
     * @param uri     需要转换的Uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
