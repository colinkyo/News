package com.a7yan.news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：尚硅谷-杨光福 on 2016/8/6 11:16
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：网络缓存工具类
 */
public class NetCacheUtils {

    /**
     * 请求图片成功
     */
    public static final int SUCCESS = 1;
    /**
     * 请求图片失败
     */
    public static final int FAIL = 2;
    private final Handler handler;
    /**
     * 本地缓存工具类
     */
    private final LocaleCacheUtils localeCacheUtils;
    /**
     * 内存缓存工具类
     */
    private final MemoryCacheUtils memoryCacheUtils;
    private ExecutorService service;

    public NetCacheUtils(Handler handler, LocaleCacheUtils localeCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.handler = handler;
        service = Executors.newFixedThreadPool(10);
        this.localeCacheUtils = localeCacheUtils;
        this.memoryCacheUtils = memoryCacheUtils;

    }

    public void getBitmapFromNet(String imageUrl, int position) {
//        new Thread(new MyRunnable(imageUrl, position)).start();
        service.execute(new MyRunnable(imageUrl, position));
    }


    class MyRunnable implements Runnable {
        private final String imageUrl;
        private final int position;

        public MyRunnable(String imageUrl, int position) {
            this.imageUrl = imageUrl;
            this.position = position;
        }

        @Override
        public void run() {
            //联网请求图片
            try {
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) new URL(imageUrl).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.setRequestMethod("GET");//注意，一定要大些
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.connect();//连接
                int code = conn.getResponseCode();//得到响应码
                if (code == 200) {//连接成功

                    InputStream is = conn.getInputStream();//得到输入流
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    //在内存中保持一份
                    memoryCacheUtils.putBitmap(imageUrl, bitmap);

                    //在本地中保持一份
                    localeCacheUtils.putBitmap(imageUrl, bitmap);


                    is.close();

                    //发消息到主线程
                    Message msg = Message.obtain();
                    msg.obj = bitmap;
                    msg.arg1 = position;
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = FAIL;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        }
    }
}