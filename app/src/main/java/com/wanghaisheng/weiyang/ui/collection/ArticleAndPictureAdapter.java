package com.wanghaisheng.weiyang.ui.collection;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.wanghaisheng.template_lib.component.baseadapter.ViewHolder;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.MultiItemCommonAdapter;
import com.wanghaisheng.template_lib.component.baseadapter.recyclerview.MultiItemTypeSupport;
import com.wanghaisheng.template_lib.component.fresco.LoadingProgressDrawable;
import com.wanghaisheng.template_lib.component.fresco.MySimpleDraweeView;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.template_lib.utils.ListUtils;
import com.wanghaisheng.weiyang.AppContext;
import com.wanghaisheng.weiyang.R;
import com.wanghaisheng.weiyang.common.ModuleConstants;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.navigator.Navigator;

import java.util.List;


/**
 * Created by sheng on 2016/6/25.
 */
public class ArticleAndPictureAdapter extends MultiItemCommonAdapter<BaseBean> {

    public static final int ONE_IMAGE_ARTICLE_ITEM_TYPE = 0x002;//只有一张图片的 article item type，
    public static final int THREE_IMAGE_ARTICLE_ITEM_TYPE = 0x003;//有三张图片的 article item type，
    public static final int ONE_IMAGE_PICTURE_ITEM_TYPE = 0x004;//只有一张图片的 picture item type
    public static final int TITLE_DESC_ITEM_TYPE = 0x005;//只有一张图片的 包含title和desc的 item type

    private Context mContext;
    private ItemDeleteListener deleteListener;

    public ArticleAndPictureAdapter(Context context, List<BaseBean> datas,ItemDeleteListener deleteListener) {
        super(context, datas, new MultiItemTypeSupport<BaseBean>(){
            @Override
            public int getLayoutId(int itemType) {//根据 item type 不同返回不同的布局
                int tempType = 0;
                switch (itemType) {
                    case ONE_IMAGE_ARTICLE_ITEM_TYPE:
                        tempType = R.layout.item_common_one_image_article_layout;
                        break;
                    case THREE_IMAGE_ARTICLE_ITEM_TYPE:
                        tempType = R.layout.item_common_three_image_article_layout;
                        break;
                    case TITLE_DESC_ITEM_TYPE:
                        tempType = R.layout.item_common_title_desc;
                        break;
                }

                return tempType;
            }

            @Override
            public int getItemViewType(int position, BaseBean bean) {

                if(bean instanceof MeishiBean) { //根据 bean中的 module_type和 image的数量，设置不同的 item_type
                    MeishiBean articleBean = (MeishiBean) bean;
                    if(ModuleConstants.MODULE_IDENTITY_SOHU.equals(articleBean.getModuleName())
                            &&articleBean.getZhuanti().equals("meishi_tag_tandian")) {
                        return TITLE_DESC_ITEM_TYPE;
                    } else if(articleBean.getImageUrlList().size()==1) {
                        return ONE_IMAGE_ARTICLE_ITEM_TYPE;
                    } else {
                        return THREE_IMAGE_ARTICLE_ITEM_TYPE;
                    }
                } else {
                    return 0;
                }
            }
        });

        this.mContext = context;
        this.deleteListener = deleteListener;
    }

    @Override
    public void convert(final ViewHolder holder, final BaseBean bean, final int position) {

        //删除时监听
        holder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.itemDeleted(holder.getAdapterPosition());
            }
        });

        switch (holder.getLayoutId()) {
            case R.layout.item_common_one_image_article_layout:
                setupOneImageArticleLayout(holder,bean,position);
                break;
            case R.layout.item_common_three_image_article_layout:
                setupThreeImageArticleLayout(holder,bean,position);
                break;
            case R.layout.item_common_title_desc:
                setupTitleDescPictureLayout(holder,bean,position);
                break;
        }
    }

    private void setupOneImageArticleLayout(final ViewHolder holder,final BaseBean bean,final int position) {

        final MeishiBean articleBean = (MeishiBean) bean;
        holder.setText(R.id.tv_title,articleBean.getTitle());
        holder.setText(R.id.tv_author,articleBean.getAuthor());
        holder.setText(R.id.tv_tag,articleBean.getModuleTitle());

        MySimpleDraweeView draweeView = holder.getView(R.id.mdv_img);
        draweeView.setAutoPlayAnimations(true)
                .setProgressBar(new LoadingProgressDrawable(AppContext.context()))
                .setDraweeViewUrl(articleBean.getImageUrlList().get(0));

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d(articleBean);
                if (ModuleConstants.MODULE_IDENTITY_WECHATARTICLE.equals(articleBean.getModuleName())) {
                    Navigator.openWechatArticleDetail(mContext,articleBean);
                    return;
                }

                Navigator.openArticleBeanDetailActivity(mContext,bean);
            }
        });
    }

    private void setupThreeImageArticleLayout(final ViewHolder holder,final BaseBean bean,final int position) {
        final MeishiBean articleBean = (MeishiBean) bean;
        holder.setText(R.id.tv_title,articleBean.getTitle());
        holder.setText(R.id.tv_tag,articleBean.getAuthor());

        List<String> imgList = articleBean.getImageUrlList();
        MySimpleDraweeView draweeView1 = holder.getView(R.id.mdv_img1);
        draweeView1.setProgressBar(new LoadingProgressDrawable(AppContext.context()))
                .setDraweeViewUrl(imgList.get(0));
        MySimpleDraweeView draweeView2 = holder.getView(R.id.mdv_img2);
        draweeView2.setProgressBar(new LoadingProgressDrawable(AppContext.context()))
                .setDraweeViewUrl(imgList.get(1));
        MySimpleDraweeView draweeView3 = holder.getView(R.id.mdv_img3);
        draweeView3.setProgressBar(new LoadingProgressDrawable(AppContext.context()))
                .setDraweeViewUrl(imgList.get(2));

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("module  "+articleBean.getModuleName());
                LogUtils.d(articleBean);
                if (ModuleConstants.MODULE_IDENTITY_WECHATARTICLE.equals(articleBean.getModuleName())) {
                    Navigator.openWechatArticleDetail(mContext,articleBean);
                    return;
                }

                Navigator.openArticleBeanDetailActivity(mContext,bean);
            }
        });
    }

    private void setupTitleDescPictureLayout(final ViewHolder holder, final BaseBean bean, final int position) {

        final MeishiBean pictureBean = (MeishiBean) bean;
        if(!TextUtils.isEmpty(pictureBean.getTitle())) {
            holder.setText(R.id.tv_title,pictureBean.getTitle());
        }

        holder.setText(R.id.tv_desc,pictureBean.getDesc());
        holder.setText(R.id.tv_author,pictureBean.getAuthor());

        if(!ListUtils.isEmpty(pictureBean.getImageUrlList())) {
            final MySimpleDraweeView simpleDraweeView = holder.getView(R.id.mdv_img);
            simpleDraweeView
                    .setProgressBar(new LoadingProgressDrawable(AppContext.context()))
                    .setDraweeViewUrl(pictureBean.getImageUrlList().get(0));
        }

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigator.openArticleBeanDetailActivity(mContext,bean);
            }
        });

    }


}