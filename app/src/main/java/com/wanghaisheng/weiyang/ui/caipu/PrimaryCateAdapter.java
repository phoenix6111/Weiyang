package com.wanghaisheng.weiyang.ui.caipu;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.wanghaisheng.template_lib.component.baseadapter.ViewHolder;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.CommonAdapter;
import com.wanghaisheng.weiyang.R;

import java.util.List;


/**
 * Created by sheng on 2016/7/4.
 */
public class PrimaryCateAdapter extends CommonAdapter<String> {

    private int selectedPosition = 0;
    private SidebarItemSelectedListener selectedListener;

    public void setSelectedListener(SidebarItemSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public PrimaryCateAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, String cate, final int position) {
        RelativeLayout container = holder.getView(R.id.rl_container);
        if(selectedPosition == position) {
            container.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            View indicator = holder.getView(R.id.sidebar_indicator);
            indicator.setVisibility(View.VISIBLE);
        } else {
            container.setBackgroundColor(mContext.getResources().getColor(R.color.primary_content_bg));
            View indicator = holder.getView(R.id.sidebar_indicator);
            indicator.setVisibility(View.GONE);
        }
        holder.setText(R.id.tv_primary_cate,cate);

        holder.setOnClickListener(R.id.rl_container, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                selectedListener.onItemSelected(position);
            }
        });
    }


}
