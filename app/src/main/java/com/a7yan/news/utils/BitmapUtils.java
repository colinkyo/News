package com.a7yan.news.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

/**
 * 作者：尚硅谷-杨光福 on 2016/8/6 11:12
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：图片三级缓存工具类
 */
public class BitmapUtils {

    /**
     * 网络缓存工具类
     */
    private NetCacheUtils netCacheUtils;

    /**
     * 本地缓存工具类
     */
    private LocaleCacheUtils localeCacheUtils;

    /**
     * 内存缓存工具类
     */
    private MemoryCacheUtils memoryCacheUtils;

    public BitmapUtils(Handler handler){

        memoryCacheUtils = new MemoryCacheUtils();
        localeCacheUtils = new LocaleCacheUtils(memoryCacheUtils);
        netCacheUtils = new NetCacheUtils(handler,localeCacheUtils,memoryCacheUtils);

    }


    /**
     *
     * 三级缓存设计步骤：
       * 从内存中取图片
       * 从本地文件中取图片
            向内存中保持一份
       * 请求网络图片，获取图片，显示到控件上
          * 向内存存一份
          * 向本地文件中存一份
     * @return
     * @param imageUrl
     * @param position
     */

    public Bitmap getBitmap(String imageUrl, int position) {
        //1.从内存中取图片
        if(memoryCacheUtils != null){
            Bitmap bitmap = memoryCacheUtils.getBitmap(imageUrl);
            if(bitmap != null){
                Log.e("TAG","从内存获取图片=="+position);
                return bitmap;
            }
        }
        //2.从本地文件中取图片
        if(localeCacheUtils != null){
            Bitmap bitmap =  localeCacheUtils.getBitmapFromUrl(imageUrl);
            if(bitmap != null){
                Log.e("TAG","从本地获取图片=="+position);
                return bitmap;
            }
        }
        //3.请求网络图片，获取图片，显示到控件上
        if(netCacheUtils != null){
            netCacheUtils.getBitmapFromNet(imageUrl,position);
        }
        return null;
    }
}
