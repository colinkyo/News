package com.a7yan.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 作者：尚硅谷-杨光福 on 2016/8/6 11:51
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：本地缓存工具类
 */
public class LocaleCacheUtils {
    private final MemoryCacheUtils memoryCacheUtils;

    public LocaleCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 根据图片url保持图片
     *
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {


        try {
            //1.对应图片路径加密-文件名称
            //ll;ls;lllsklkihkllll
            String fileName = MD5Encoder.encode(imageUrl);
            //2.创建文件
            File file = new File(Environment.getExternalStorageDirectory() + "/beijingnews", fileName);

            File parentFile = file.getParentFile();//mnt/sdcard/beijingnews
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            //3.保存sdcard
            FileOutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Bitmap getBitmapFromUrl(String imageUrl) {
        //1.对应图片路径加密-文件名称
        //ll;ls;lllsklkihkllll
        String fileName = null;
        try {
            fileName = MD5Encoder.encode(imageUrl);
            //2.创建文件
            File file = new File(Environment.getExternalStorageDirectory() + "/beijingnews", fileName);

            if (file.exists()) {

                FileInputStream fis = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                if(bitmap != null){
                    memoryCacheUtils.putBitmap(imageUrl,bitmap);
                }
                fis.close();
                return bitmap;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
