package com.wanghaisheng.weiyang.ui.caipu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;

import com.wanghaisheng.template_lib.component.baseadapter.ViewHolder;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.MultiItemCommonAdapter;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.MultiItemTypeSupport;
import com.wanghaisheng.template_lib.component.fresco.LoadingProgressDrawable;
import com.wanghaisheng.template_lib.component.fresco.MySimpleDraweeView;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.navigator.Navigator;

import java.util.List;


/**
 * Created by sheng on 2016/6/1.
 */
public class MeishiAdapter extends MultiItemCommonAdapter<BaseBean> {
    private static final int HEADER = 0X100;
    private static final int NORMAL = 0X101;
    private Context mContext;

    public MeishiAdapter(Context context, List<BaseBean> datas) {
        super(context, datas, new MultiItemTypeSupport<BaseBean>(){

            @Override
            public int getLayoutId(int itemType) {
                if(HEADER == itemType) {
                    return R.layout.common_list_search_header;
                } else {
                    return R.layout.item_meishi_meishilist;
                }
            }

            @Override
            public int getItemViewType(int position, BaseBean article) {
                if(position==0) {
                    return HEADER;
                } else {
                    return NORMAL;
                }
            }
        });

        this.mContext = context;
    }

    @Override
    public void convert(ViewHolder holder, final BaseBean baseBean, int position) {

        switch (holder.getLayoutId()) {
            case R.layout.common_list_search_header:
                EditText searchText = holder.getView(R.id.et_meishi_search);
                searchText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigator.openMeishiSearchActivity(mContext);
                    }
                });
                searchText.setFocusable(false);

                holder.setOnClickListener(R.id.iv_search, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigator.openMeishiSearchActivity(mContext);
                    }
                });
                break;
            case R.layout.item_meishi_meishilist:
                final MeishiBean content = (MeishiBean) baseBean;
                holder.setText(R.id.tv_meishi_title, content.getTitle());
                holder.setText(R.id.tv_author,content.getAuthor());
                MySimpleDraweeView image = holder.getView(R.id.mdv_meishi_img);
                image.setPlaceholderDrawable(new ColorDrawable(ContextCompat.getColor(mContext,R.color.white)))
                        .setProgressBar(new LoadingProgressDrawable(AppContext.context()))
                    .setDraweeViewUrl(content.getImageUrlList().get(0));

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigator.openArticleBeanDetailActivity(mContext,content);
                    }
                });
                break;

        }
    }
}
