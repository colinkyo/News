package com.a7yan.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 7Yan on 2017/2/7.
 */

public class HorizontalScrollViewPager extends ViewPager {
    private float startX;
    private float startY;
    public HorizontalScrollViewPager(Context context) {
        this(context,null);
    }

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
//                 一定要先拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = ev.getX();
                startY = ev.getY();
                /*startX = ev.getRawX();
                startY = ev.getRawY();*/
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = ev.getX();
                float  endY = ev.getY();
                /*endX = ev.getRawX();
                endY = ev.getRawY();*/
                //2.计算偏移量
                float distanceX = endX - startX;
                float distanceY = endY - startY;
                //3.判断滑动方向
                if(Math.abs(distanceX) > Math.abs(distanceY)) {
                    //水平方向滑动
//                    1.如果第0个位置，并且滑动方向是从左到右滑动
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                    2.如果是页签页面的最后一个位置，并且滑动方向是从右向左滑动
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                    3.其他中间部分
//                    getParent().requestDisallowInterceptTouchEvent(true);
                    if(getCurrentItem()==0 && distanceX > 0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if(getCurrentItem() == (getAdapter().getCount()-1) && distanceX <0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else{
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else {
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
