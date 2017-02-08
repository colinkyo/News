package com.a7yan.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.a7yan.news.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 7Yan on 2017/2/8.
 */

public class RefreshListView extends ListView {

    private ImageView iv_red_arrow;
    private ProgressBar pb_status;
    private TextView tv_status;
    private TextView tv_time;

    /**
     * 下拉刷新控件的高
     */
    private int headerViewHeight;

    /**
     * 整个header(下拉刷新控件和顶部轮播图)
     */
    private LinearLayout headerView;
    /**
     * 下拉刷新控件
     */
    private View ll_pull_dwon;
    /**
     * 顶部轮播图部分
     */
    private View topnewsView;
    /**
     * ListView在Y轴的坐标
     */
    private int listViewOnScreenY = -1;

    /**
     * 下拉刷新状态
     **/
    private static final int PULL_DOWN_REFRESH = 1;


    /**
     * 手松刷新状态
     **/
    private static final int RELEASE_REFRESH = 2;


    /**
     * 正在刷新状态
     **/
    private static final int REFRESHING = 3;

    /**
     * 当前状态
     */
    private int currentState = PULL_DOWN_REFRESH;

    private Animation upAnimation;
    private Animation downAnimation;

    private float startY = 0;
    /**
     * 上拉加载更多的布局
     */
    private View footView;
    /**
     * 上拉加载更多的控件的高
     */
    private int footViewHeight;
    /**
     * 是否加载更多
     */
    private boolean isLoadMore = false;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);

    }

    private void initFooterView(Context context) {
        footView = View.inflate(context, R.layout.refresh_footer, null);
        footView.measure(0, 0);
        footViewHeight = footView.getMeasuredHeight();
        /**
         *
         View.setPadding(0,-控件高，0,0）；//完成隐藏
         View.setPadding(0,0，0,0）；//完成显示
         View.setPadding(0,控件高，0,0）；//两倍完全显示
         */
        footView.setPadding(0,-footViewHeight,0,0);

        addFooterView(footView);


        //监听滑动到ListView的最后一个可见的item
        setOnScrollListener(new MyOnScrollListener());
    }

    class MyOnScrollListener implements OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //静止，或者惯性滚动，并且是最后一个可见的时候
            if(scrollState ==OnScrollListener.SCROLL_STATE_IDLE || scrollState ==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                //到最后一个可见
                if(getLastVisiblePosition() ==getAdapter().getCount()-1&& !isLoadMore){

                    //显示加载更多控件
                    footView.setPadding(10,10,10,10);

                    //设置状态
                    isLoadMore = true;

                    //回调接口
                    if(mOnRefreshListener != null){
                        mOnRefreshListener.onLoadMore();
                    }

                }
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }


    /**
     * 初始化动画
     */
    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);
        ll_pull_dwon = headerView.findViewById(R.id.ll_pull_donw);
        iv_red_arrow = (ImageView) headerView.findViewById(R.id.iv_red_arrow);
        pb_status = (ProgressBar) headerView.findViewById(R.id.pb_status);
        tv_status = (TextView) headerView.findViewById(R.id.tv_status);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);


        ll_pull_dwon.measure(0, 0);//测量-掉要测量
        headerViewHeight = ll_pull_dwon.getMeasuredHeight();

//        View.setPadding(0,-控件高，0,0）；//完成隐藏
//        View.setPadding(0,0，0,0）；//完成显示
//       View.setPadding(0,控件高，0,0）；//两倍完全显示
        ll_pull_dwon.setPadding(0, -headerViewHeight, 0, 0);

        addHeaderView(headerView);//以头的方法添加到ListView中

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.记录起始坐标
                startY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (startY == 0) {
                    startY = ev.getY();
                }

                //判断顶部轮播图是否完全显示
                boolean isDisplayTopNews = isDisplayTopNews();//完全显示就是下拉刷新
                if (!isDisplayTopNews) {
                    //上拉刷新
                    break;
                }
                //2.记录结束坐标
                float endY = ev.getY();
                //3.计算偏移量
                float distanceY = endY - startY;


                if (distanceY > 0) {
                    //向下滑动
                    // int paddingTop = - 控件的高 + distanceY；
                    // View.setPadding(0,paddingTop，0,0）//动态显示下拉刷新控件
                    int paddingTop = (int) (-headerViewHeight + distanceY);
                    if (paddingTop < 0 && currentState != PULL_DOWN_REFRESH) {
                        //下拉刷新
                        currentState = PULL_DOWN_REFRESH;
                        refreshStatus();
                        //更新状态
                    } else if (paddingTop > 0 && currentState != RELEASE_REFRESH) {
                        //手势刷新
                        currentState = RELEASE_REFRESH;
                        //更新状态
                        refreshStatus();
                    }
                    ll_pull_dwon.setPadding(0, paddingTop, 0, 0);

                }
                break;

            case MotionEvent.ACTION_UP:
                startY = 0;
                if (currentState == PULL_DOWN_REFRESH) {
                    //View.setPadding(0,-控件高，0,0）；//完成隐藏
                    ll_pull_dwon.setPadding(0, -headerViewHeight, 0, 0);
                } else if (currentState == RELEASE_REFRESH) {

                    currentState = REFRESHING;

                    //View.setPadding(0,0，0,0）；//完成显示
                    ll_pull_dwon.setPadding(0, 0, 0, 0);
                    //状态要更新
                    refreshStatus();

                    //回调接口
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onPullDownlRefresh();
                    }
                }
                break;

            default:
                break;

        }
        return super.onTouchEvent(ev);
    }

    private void refreshStatus() {
        switch (currentState) {
            case PULL_DOWN_REFRESH://下拉刷新
                tv_status.setText("下拉刷新...");
                iv_red_arrow.startAnimation(downAnimation);
                break;
            case RELEASE_REFRESH://手松刷新
                tv_status.setText("手松刷新...");
                iv_red_arrow.startAnimation(upAnimation);
                break;
            case REFRESHING://正在刷新
                pb_status.setVisibility(VISIBLE);
                iv_red_arrow.setVisibility(GONE);
                iv_red_arrow.clearAnimation();
                tv_status.setText("正在刷新...");
                break;
        }
    }

    /**
     * 判断顶部轮播图是否完全显示
     * 当ListView在Y轴的坐标小于或者等于顶部轮播图部分在Y轴的坐标的时候
     *
     * @return
     */
    private boolean isDisplayTopNews() {

        if(topnewsView != null){
            //得到Listveiw在屏幕上Y轴的坐标
            int[] location = new int[2];
            if (listViewOnScreenY == -1) {
                this.getLocationOnScreen(location);
                listViewOnScreenY = location[1];
            }

            //得到顶部轮播图部分在Y轴的坐标
            topnewsView.getLocationOnScreen(location);
            int topnewsViewOnScreenY = location[1];

//        if(listViewOnScreenY <= topnewsViewOnScreenY){
//            return true;
//        }else{
//            return false;
//        }


            return listViewOnScreenY <= topnewsViewOnScreenY;
        }else{
            return true;
        }

    }

    /**
     * 顶部轮播图部分
     *
     * @param topnewsView
     */
    public void addTopNews(View topnewsView) {
        this.topnewsView = topnewsView;

        if (topnewsView != null && headerView != null) {

            headerView.addView(topnewsView);//添加顶部轮回
        }

    }

    /**
     * 把下拉刷新状态恢复成初始状态
     *
     * @param success
     */
    public void onRefreshFinish(boolean success) {

        if(isLoadMore){
            //加载更多
            isLoadMore = false;
            footView.setPadding(0,-footViewHeight,0,0);
        }else{
            //下拉刷新
            iv_red_arrow.clearAnimation();
            iv_red_arrow.setVisibility(VISIBLE);
            pb_status.setVisibility(GONE);
            tv_status.setText("下拉刷新...");
            ll_pull_dwon.setPadding(0, -headerViewHeight, 0, 0);
            currentState = PULL_DOWN_REFRESH;
            if (success) {
                //更新时间
                tv_time.setText("更新时间:" + getSystemTime());
            }
        }


    }

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }


    /**
     * 监听视图刷新监听者
     **/
    public interface OnRefreshListener {
        /**
         * 当下拉刷新的时候回调这个方法
         */
        public void onPullDownlRefresh();

        /**
         当加载更多的时候回调这方法
         */
        public void onLoadMore();
    }

    private OnRefreshListener mOnRefreshListener;


    /**
     * 设置视图刷新的监听
     * by afu
     */
    public void setOnRefreshListener(OnRefreshListener l) {
        this.mOnRefreshListener = l;
    }
}
