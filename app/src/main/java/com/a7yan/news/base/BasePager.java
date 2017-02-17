package com.a7yan.news.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.a7yan.news.MainActivity;
import com.a7yan.news.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 7Yan on 2017/1/24.
 * * 作用：基类，公共类
 * 该类是HomePager,NewsCenterPager,SmartServicePager,GovaffairPager,SettingPager父类
 */

public class BasePager {
    public Context mContext;
    /**
     * 代表不同的子页面
     */
    public View rootView;
    @BindView(R.id.tv_title)
    public TextView tv_title;
    @BindView(R.id.ib_menu)
    public ImageButton ib_menu;
    @BindView(R.id.fl_content)
    public FrameLayout fl_content;
    @BindView(R.id.ib_swich_list_grid)
    public ImageButton ib_swich_list_grid;

    public BasePager(Context context) {
        this.mContext = context;
        /**
         * 初始化视图
         */
        rootView = initView();
    }

    private View initView() {
        View view = View.inflate(mContext, R.layout.basepager, null);
//        使用ButterKnife绑定XML文件
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 当孩子需要请求数据，或者需要动态显示数据的时候重写该方法
     */
    public void initData() {

    }

    /**
     * 控制左侧菜单开启与否
     */
    @OnClick(R.id.ib_menu)
    public void onClick() {
        MainActivity mainActivity = (MainActivity) mContext;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();
    }
}
