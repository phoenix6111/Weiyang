package com.wanghaisheng.weiyang.ui.popwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.wanghaisheng.template_lib.component.baseadapter.ViewHolder;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.CommonAdapter;
import com.wanghaisheng.template_lib.component.fresco.MySimpleDraweeView;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;
import com.wanghaisheng.weiyang.navigator.Navigator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PoiListPopupWindow extends PopupWindow {
    //PoupupWindow的宽和高
    private int mWidth;
    private int mHeight;
    private View mConvertView;

    //ListView中的数据
    private List<MapPoiBean> mDatas = new ArrayList<>();
    private Context mContext;
    protected SwipeToLoadLayout swipeToLoadLayout;
    protected RecyclerView myRecyclerView;
    protected CommonAdapter<MapPoiBean> mAdapter;

    private PopupWindowItemClickListener itemClickListener;

    public void setItemClickListener(PopupWindowItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setDatas(List<MapPoiBean> datas,int currentIndex) {
        if(!ListUtils.isEmpty(datas)) {
            mDatas.clear();
            mDatas.addAll(datas);
            mAdapter.notifyDataSetChanged();
            myRecyclerView.scrollToPosition(currentIndex);
        }
    }

    public PoiListPopupWindow(Context context, List<MapPoiBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
        //设置宽和高
        calculateWidthAndHeight();
        mConvertView = LayoutInflater.from(mContext).inflate(R.layout.popup_window_layout,null);
        setContentView(mConvertView);
        //设置宽度和高度
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initView();

    }

    private void initView() {
        myRecyclerView = (RecyclerView) mConvertView.findViewById(R.id.swipe_target);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mConvertView.getContext()));
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = initAdapter();
        myRecyclerView.setAdapter(mAdapter);

        swipeToLoadLayout = (SwipeToLoadLayout) mConvertView.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMoreData();
            }
        });
        myRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }

                super.onScrollStateChanged(recyclerView,newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void onLoadMoreData() {

    }

    private void onRefreshData() {

    }

    private CommonAdapter<MapPoiBean> initAdapter() {
        return new CommonAdapter<MapPoiBean>(mContext,R.layout.item_popup_window,mDatas) {
            @Override
            public void convert(ViewHolder holder, final MapPoiBean poiItem, int position) {
                holder.setText(R.id.tv_title,poiItem.getName());
                SimpleRatingBar ratingBar = holder.getView(R.id.ratingbar);
                ratingBar.setRating(Float.parseFloat(poiItem.getRating()));
                if(!TextUtils.isEmpty(poiItem.getPrice())) {
                    holder.setText(R.id.tv_price,Html.fromHtml(poiItem.getPrice()).toString());
                }
                holder.setText(R.id.tv_address,poiItem.getAddress());
                if(!TextUtils.isEmpty(poiItem.getTag())) {
                    holder.setText(R.id.tv_tag, Html.fromHtml(poiItem.getTag()).toString());
                }
                holder.setText(R.id.tv_distance,formatDistance(poiItem.getDistance()));

                MySimpleDraweeView draweeView = holder.getView(R.id.mdv_img);
                draweeView.setDraweeViewUrl(poiItem.getPic_info());

                holder.setOnClickListener(R.id.rl_detail, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Navigator.openPoiDetailActivity(mContext,poiItem);
                    }
                });
            }
        };
    }

    private String formatDistance(String distance) {
        float dis = Float.parseFloat(distance);
        if(dis>1000) {
            dis = dis/1000;
            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p= decimalFormat.format(dis);//format 返回的是字符串
            return p+"千米";
        }

        return dis+"米";
    }

    //设置宽和高，宽为屏幕的宽度，高为屏幕高度的0.7倍
    private void calculateWidthAndHeight() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels*0.4f);
    }
}