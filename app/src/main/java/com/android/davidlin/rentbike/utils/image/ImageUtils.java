package com.android.davidlin.rentbike.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;

import com.avos.avoscloud.AVFile;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Deal image
 * Created by David Lin on 2015/11/8.
 */
public class ImageUtils {

    /**
     * 将Bitmap转换为二进制流并生成AVFile以待上传
     * 同时将缩略图显示在UI中
     *
     * @param path 外置存储中原始图片的路径
     * @param img  需要显示图片的{@link ImageView}
     * @return 返回生成的AVFile对象
     */
    public static AVFile BitmapToAVFile(String path, ImageView img) {
        Bitmap temp = BitmapFactory.decodeFile(path);
        if (temp == null) return null;
        Matrix m = new Matrix();
        m.setScale(0.2f, 0.2f);
        int width = temp.getWidth(), height = temp.getHeight();
        int length = width < height ? width : height;
        int startX = width < height ? 0 : (width - length) / 2;
        int startY = width < height ? (height - length) / 2 : 0;

        Bitmap display = Bitmap.createBitmap(temp, startX, startY, length, length, m, true);
        img.setImageBitmap(display);

        m.setScale(0.5f, 0.5f);
        Bitmap upload = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), m, true);
        ByteArrayOutputStream baos;
        AVFile file;
        try {
            baos = new ByteArrayOutputStream();
            upload.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            File f = new File(path);
            file = new AVFile(f.getName(), baos.toByteArray());
            return file;
        } catch (Exception e) {
            Log.e("BitmapToAVFile", e.getMessage());
        }
        return null;
    }
}
