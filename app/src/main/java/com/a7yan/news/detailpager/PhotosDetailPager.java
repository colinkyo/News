package com.a7yan.news.detailpager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.a7yan.news.R;
import com.a7yan.news.base.DetailBasePager;
import com.a7yan.news.pager.NewsCenterPager;
import com.a7yan.news.utils.CacheUtils;
import com.a7yan.news.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by 7Yan on 2017/1/25.
 */

public class PhotosDetailPager extends DetailBasePager {
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.gridview)
    GridView gridview;

    public PhotosDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        Log.d("PhotosDetailPager", "图组详情页面的视图被初始化了");
        View view = View.inflate(mContext, R.layout.photos_detail_pager, null);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.d("PhotosDetailPager", "图组详情页面数据被初始化了");
        String savejson = CacheUtils.getString(mContext,Constants.NEWSCENTER_URL);
        if(!TextUtils.isEmpty(savejson)){
            processData(savejson);
        }
        getDataFromNet();
    }
    private void getDataFromNet() {
        OkHttpUtils
                .get()
                .url(Constants.NEWSCENTER_URL)
                .id(100)
                .build()
                .execute(new PhotosDetailPager.MyStringCallback());
//        Toast.makeText(mContext, Constants.API_URL, Toast.LENGTH_SHORT).show();
    }

    class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
            Log.d("MyStringCallback", "开始联网。。。");
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            Log.d("MyStringCallback", "结束联网。。成功失败都会执行。");
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            super.inProgress(progress, total, id);
            Log.d("MyStringCallback", "联网中。。。");
        }

        @Override
        public void onError(Call call, Exception e, int i) {
            e.printStackTrace();
            Log.d("MyStringCallback", "联网失败:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d("MyStringCallback", "联网成功。。。" + response);
//            根据id,处理不同的联网请求
            switch (id) {
                case 100:
//                    保存数据
                    CacheUtils.putString(mContext,Constants.NEWSCENTER_URL,response);
//                    解析数据,json格式
                    processData(response);
                    break;
            }
        }
    }

    private void processData(String response) {

    }
}
