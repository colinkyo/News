package com.a7yan.news.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;

import com.a7yan.news.activity.GuideActivity;

/**
 * Created by 7yan on 2017/1/23.
 */

public class CacheUtils {
    public static void putBoolean(Context context, String key, boolean values)
    {
        SharedPreferences sp= context.getSharedPreferences("7Yan",context.MODE_PRIVATE);
        sp.edit().putBoolean(key,values).commit();
    }
    public static boolean getBoolean(Context context, String key)
    {
        SharedPreferences sp= context.getSharedPreferences("7Yan",context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }

    public static void putString(Context context, String key, String values)
    {
        SharedPreferences sp= context.getSharedPreferences("7Yan",context.MODE_PRIVATE);
        sp.edit().putString(key,values).commit();
    }

    public static String getString(Context context, String key){
        SharedPreferences sp= context.getSharedPreferences("7Yan",context.MODE_PRIVATE);
        return  sp.getString(key,"");
    }
}
