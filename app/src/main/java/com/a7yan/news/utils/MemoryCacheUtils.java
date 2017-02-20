package com.a7yan.news.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 作者：尚硅谷-杨光福 on 2016/8/6 14:45
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：内存缓存工具类
 */
public class MemoryCacheUtils {
    /**
     * 缓存图片的结合
     */
    private LruCache<String,Bitmap> lruCache;
    public  MemoryCacheUtils(){
        //得到最大内存的百分之一用于缓存图片
        int maxSize = (int) (Runtime.getRuntime().maxMemory()/1024/8);
        //设置缓存图片的空间
        lruCache = new LruCache<String,Bitmap>(maxSize){
            /**
             * 计算每张图片的大小
             * @param key
             * @param value
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getRowBytes() * value.getHeight())/1024;
            }
        };
    }

    /**
     * 根据url保存图片到内存中
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl,bitmap);
    }

    /**
     * 根据url获取在内存中缓存的图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmap(String imageUrl){
        return  lruCache.get(imageUrl);
    }
}
