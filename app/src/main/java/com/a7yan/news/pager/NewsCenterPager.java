package com.a7yan.news.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.a7yan.news.MainActivity;
import com.a7yan.news.base.BasePager;
import com.a7yan.news.base.DetailBasePager;
import com.a7yan.news.detailpager.InteracDetailPager;
import com.a7yan.news.detailpager.NewsDetailPager;
import com.a7yan.news.detailpager.PhotosDetailPager;
import com.a7yan.news.detailpager.TopicDetailPager;
import com.a7yan.news.domain.NewsCenterPagerBean;
import com.a7yan.news.fragment.LeftmenuFragment;
import com.a7yan.news.utils.CacheUtils;
import com.a7yan.news.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.id.list;

/**
 * Created by 7Yan on 2017/1/24.
 */

public class NewsCenterPager extends BasePager {

    private TextView textView;
    private List<NewsCenterPagerBean.NewsCenterPagerData> data;
//    左侧菜单对应的页面
    private List<DetailBasePager> detailBasePagers;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        Log.d("HomePager", "新闻中心数据被初始化了....");
//        显示左上角菜单键
        ib_menu.setVisibility(View.VISIBLE);

        tv_title.setText("新闻中心");
        textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setText("新闻中心");
//        把内容添加到fl_content去
        fl_content.addView(textView);
//        联网请求
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
                .execute(new MyStringCallback());
//        Toast.makeText(mContext, Constants.API_URL, Toast.LENGTH_SHORT).show();
    }
//    切换到不同的页面
    public void swichPager(int position)
    {
//        设置标题
        tv_title.setText(data.get(position).getTitle());
//        设置内容
        DetailBasePager detailBasePager = detailBasePagers.get(position);
        View view = detailBasePager.rootView;
//        初始化数据，也可以屏蔽预加载
        detailBasePager.initData();
//        把之前的视图移除
        fl_content.removeAllViews();
//        添加视图
        fl_content.addView(view);

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

    //    解析和显示数据
    private void processData(String json) {
        NewsCenterPagerBean bean = parsedJson(json);
        //Log.d("NewsCenterPager", bean.getData().get(0).getChildren().get(3).getTitle());
//        得到数据后传给左侧菜单
        data = bean.getData();
//        先得到左侧菜单
        MainActivity mainActivity = (MainActivity) mContext;
        LeftmenuFragment leftmenuFragment =mainActivity.getLeftmenuFragment();
//        创建四个页面一定要先与传递数据
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsDetailPager(mContext,data.get(0)));
        detailBasePagers.add(new TopicDetailPager(mContext));
        detailBasePagers.add(new PhotosDetailPager(mContext));
        detailBasePagers.add(new InteracDetailPager(mContext));

//        把数据传递给左侧菜单
        leftmenuFragment.setData(data);

    }

    private NewsCenterPagerBean parsedJson(String json) {
        //Group group = JSON.parseObject(jsonString, Group.class);
//        使用fastjson解析数据 *********************************
//        return JSON.parseObject(json,NewsCenterPagerBean.class);

//        使用系统的api解析json数据 *********************************
        //创建Bean对象
        NewsCenterPagerBean bean = new NewsCenterPagerBean();
        try {
            org.json.JSONObject jsonObject= new org.json.JSONObject(json);
//            得到第一层数据
            int retcode = jsonObject.optInt("retcode");
            bean.setRetcode(retcode);
//            得到数组
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            if(jsonArray != null && jsonArray.length() > 0){
//             变成列表-创建列表
                List<NewsCenterPagerBean.NewsCenterPagerData> data = new ArrayList<>();
//              把列表设置到Bean中,但data并没有数据
                bean.setData(data);
                for (int i=0; i < jsonArray.length();i++)
                {
                    org.json.JSONObject item = (org.json.JSONObject) jsonArray.get(i);
                    if(item != null)
                    {
                        NewsCenterPagerBean.NewsCenterPagerData centerPagerData = new NewsCenterPagerBean.NewsCenterPagerData();
//                        添加到data集合中，此时data就已经有了数据
                        data.add(centerPagerData);

                        centerPagerData.setId(item.optInt("id"));
                        centerPagerData.setTitle(item.optString("title"));
                        centerPagerData.setType(item.optInt("type"));
                        centerPagerData.setUrl(item.optString("url"));
                        centerPagerData.setUrl1(item.optString("url1"));
                        centerPagerData.setDayurl(item.optString("dayurl"));
                        centerPagerData.setExcurl(item.optString("excurl"));
                        centerPagerData.setWeekurl(item.optString("weekurl"));
//                        得到Json数组
                        JSONArray jsonArray1 = item.optJSONArray("children");
                        if(jsonArray1 != null && jsonArray1.length() > 0)
                        {
                            List<NewsCenterPagerBean.NewsCenterPagerData.ChildrenData> children = new ArrayList<>();
//                          把children列表设置到centerPagerData中,但children并没有数据
                            centerPagerData.setChildren(children);

                            for(int j = 0 ; j < jsonArray1.length(); j++)
                            {
                                org.json.JSONObject item1= (org.json.JSONObject) jsonArray1.get(j);
                                if(item1 != null)
                                {
                                    NewsCenterPagerBean.NewsCenterPagerData.ChildrenData childrenData = new NewsCenterPagerBean.NewsCenterPagerData.ChildrenData();
//                                  此时children列表有数据啦
                                    children.add(childrenData);

                                    childrenData.setId(item1.optInt("id"));
                                    childrenData.setTitle(item1.optString("title"));
                                    childrenData.setUrl(item1.optString("url"));
                                    childrenData.setType(item1.optInt("type"));
                                }
                            }
                        }
//                        添加到data集合中，此时data就已经有了数据
//                        data.add(centerPagerData);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean;
    }
}
