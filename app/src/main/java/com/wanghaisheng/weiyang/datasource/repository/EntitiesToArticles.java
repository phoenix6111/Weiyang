package com.wanghaisheng.weiyang.datasource.repository;

import android.text.TextUtils;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.common.ModuleConstants;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiBeanResult;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBean;
import com.wanghaisheng.weiyang.datasource.beans.MapPoiDetailBeanResult;
import com.wanghaisheng.weiyang.datasource.beans.ScienceArticle;
import com.wanghaisheng.weiyang.datasource.beans.YinBeanResult;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuResponseBean;
import com.wanghaisheng.weiyang.datasource.repository.ttyy.TTYYBeanResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by sheng on 2016/6/12.
 * 类型转换类
 */
public class EntitiesToArticles {

    public static List<BaseBean> yinBeanResultToBaseBeanList(List<YinBeanResult.DataBean> dataBeanList) {
        List<BaseBean> result = new ArrayList<>();
        for(YinBeanResult.DataBean dataBean : dataBeanList) {
            MeishiBean meishiBean = new MeishiBean();
            meishiBean.setModuleName(ModuleConstants.MODULE_IDENTITY_YIN);

            meishiBean.setArticleId(dataBean.getArticleID()+"");
            meishiBean.setTitle(dataBean.getTitle());
            meishiBean.setDesc(dataBean.getDesc());
            meishiBean.setPublishDate(dataBean.getPublishDateStr());
            meishiBean.setAuthor(dataBean.getWxAlias());
            meishiBean.setArticleUrl(dataBean.getArticleUrl());
            String picList = dataBean.getPicList();
            if(!TextUtils.isEmpty(picList)) {
                String[] picDatas = picList.split("\\$");
                List<String> resultPics = new ArrayList<>(Arrays.asList(picDatas));
                meishiBean.setImageUrlList(resultPics);
            } else {
                meishiBean.setImageUrlList(new ArrayList<String>(Arrays.asList(new String[]{dataBean.getSmallPic()})));
            }
            meishiBean.setImageUrls(tagListToString(meishiBean.getImageUrlList()));
            result.add(meishiBean);
        }

        return result;
    }

    public static List<BaseBean> scienceArticlesToArticleBeans(List<ScienceArticle> scienceArticles) {
        List<BaseBean> articleBeanList = new ArrayList<>();
        for(ScienceArticle article : scienceArticles) {
            MeishiBean articleBean = new MeishiBean();
            articleBean.setModuleName(ModuleConstants.MODULE_IDENTITY_SCIENCE);

            articleBean.setTitle(article.getArticle_title());
            articleBean.setPublishDate(article.getArticle_time());
            articleBean.setDesc(article.getArticle_intro());
            articleBean.setAuthor(article.getArticle_author());
            articleBean.setArticleUrl(article.getArticle_link());
            articleBean.setIsVideo(article.getArticle_video());
            if(!TextUtils.isEmpty(article.getImg_link())) {
                List<String> imageLists = new ArrayList<>();
                imageLists.add(article.getImg_link());
                articleBean.setImageUrlList(imageLists);
                articleBean.setImageUrls(tagListToString(articleBean.getImageUrlList()));
            }

            articleBeanList.add(articleBean);
        }

        return articleBeanList;
    }

    public static List<BaseBean> ttyyArticlesToArticleBeans(TTYYBeanResult beanResult) {
        List<BaseBean> articleBeanList = new ArrayList<>();
        for(TTYYBeanResult.DataBean article : beanResult.getDataList()) {
            MeishiBean articleBean = new MeishiBean();
            articleBean.setModuleName(ModuleConstants.MODULE_IDENTITY_TTYY);

            articleBean.setTitle(article.getNews_title());
            articleBean.setPublishDate(article.getNews_addtime());
            articleBean.setDesc(article.getNews_content());
            articleBean.setAuthor(article.getNews_zuozhe());
            if(!TextUtils.isEmpty(article.getNews_pic())) {
                List<String> imageLists = new ArrayList<>();
                imageLists.add(article.getNews_pic());
                articleBean.setImageUrlList(imageLists);
                articleBean.setImageUrls(tagListToString(articleBean.getImageUrlList()));
            }

            articleBeanList.add(articleBean);
        }

        return articleBeanList;
    }

    public static List<BaseBean> sohuArticlesToArticleBeans(List<SohuResponseBean> beanList) {

        List<BaseBean> baseBeen = new ArrayList<BaseBean>();
        for (SohuResponseBean pageBean : beanList) {
            MeishiBean meishiBean = new MeishiBean();
            meishiBean.setModuleName(ModuleConstants.MODULE_IDENTITY_SOHU);

            if(TextUtils.isEmpty(pageBean.getWapImg())) {
                continue;
            }
            List<String> imgLists = new ArrayList<>();
            imgLists.add(pageBean.getWapImg());
            meishiBean.setImageUrlList(imgLists);
            meishiBean.setImageUrls(pageBean.getWapImg());
            meishiBean.setTitle(pageBean.getTitle());
            meishiBean.setArticleUrl(pageBean.getUrl());
            meishiBean.setDesc(pageBean.getText().replaceAll("\\s*", ""));
            meishiBean.setAuthor(pageBean.getSource());
            meishiBean.setArticleId(parseSohuId(meishiBean.getArticleUrl()));

            baseBeen.add(meishiBean);
        }

        return baseBeen;
    }

    public static String parseSohuId(String url) {
        if(TextUtils.isEmpty(url)) {
            return "";
        }

        String[] arrs = url.split("\\/");
        String endStr = arrs[arrs.length-1];
        String id = endStr.split("\\.")[0];

        return id.substring(1);
    }

    /**
     * 将AMapPoiListResult 转化成List<MapPoiBean>
     * @param mapPoiBeanResult
     * @return
     */
    public static List<MapPoiBean> parseAMapPoiListResultToAMapPoiList(MapPoiBeanResult mapPoiBeanResult) {
        List<MapPoiBean> results = new ArrayList<>();


        for(MapPoiBeanResult.PoiListBean poiListBean : mapPoiBeanResult.getPoi_list()) {
            MapPoiBean mapPoiBean = new MapPoiBean();
            mapPoiBean.setPoiId(poiListBean.getId());
            mapPoiBean.setName(poiListBean.getName());
            mapPoiBean.setDisp_name(poiListBean.getDisp_name());
            mapPoiBean.setRating(poiListBean.getRating());
            mapPoiBean.setTel(poiListBean.getTel());
            mapPoiBean.setAreacode(poiListBean.getAreacode());
            mapPoiBean.setCityname(poiListBean.getCityname());
            mapPoiBean.setLatitude(Double.parseDouble(poiListBean.getLatitude()));
            mapPoiBean.setLongitude(Double.parseDouble(poiListBean.getLongitude()));
            mapPoiBean.setAddress(poiListBean.getAddress());
            mapPoiBean.setDistance(poiListBean.getDistance()+"");

            for(MapPoiBeanResult.PoiListBean.DomainListBean domainListBean : poiListBean.getDomain_list()) {
                if("price".equals(domainListBean.getName())) {
                    mapPoiBean.setPrice(domainListBean.getValue());
                }

                if("tag".equals(domainListBean.getName())) {
                    mapPoiBean.setTag(domainListBean.getValue());
                }

                if("pic_info".equals(domainListBean.getName())) {
                    mapPoiBean.setPic_info(domainListBean.getValue());
                }
            }

            results.add(mapPoiBean);
        }

        return results;

    }

    public static MapPoiDetailBean parseMapPoiDetailResultToMapPoiDetailBean(MapPoiDetailBeanResult mapPoiDetailBeanResult,String distance) {
        MapPoiDetailBean detailBean = new MapPoiDetailBean();

        MapPoiDetailBeanResult.DataBean dataBean = mapPoiDetailBeanResult.getData();
        MapPoiDetailBeanResult.DataBean.BaseBean baseBean = dataBean.getBase();
        detailBean.setAddress(baseBean.getAddress());
        detailBean.setTel(baseBean.getTelephone());
        detailBean.setPoiId(baseBean.getPoiid());
        detailBean.setName(baseBean.getName());
        detailBean.setLongitude(Double.parseDouble(baseBean.getLongitude()));
        detailBean.setLatitude(Double.parseDouble(baseBean.getLatitude()));
        detailBean.setClassify(baseBean.getClassify());
        detailBean.setDistance(distance);

        MapPoiDetailBeanResult.DataBean.DiningBean diningBean = mapPoiDetailBeanResult.getData().getDining();
        detailBean.setScore(Float.parseFloat(diningBean.getSrc_star()));
        detailBean.setTags(diningBean.getTag_special());
        detailBean.setPrice(diningBean.getPrice()+"");
        detailBean.setIntro(diningBean.getIntro());
        detailBean.setOpentime(diningBean.getOpentime2());

        List<MapPoiDetailBeanResult.DataBean.PicBean> picBeans = mapPoiDetailBeanResult.getData().getPic();
        if(picBeans.size() == 1) {
            detailBean.setCoverImg(picBeans.get(0).getUrl());
        } else {
            for(MapPoiDetailBeanResult.DataBean.PicBean picBean : picBeans) {
                if(picBean.getIscover() == 1) {
                    detailBean.setCoverImg(picBean.getUrl());
                }
            }
            if(TextUtils.isEmpty(detailBean.getCoverImg())) {
                detailBean.setCoverImg(picBeans.get(0).getUrl());
            }
        }

        List<MapPoiDetailBeanResult.DataBean.ReviewBean.CommentBean> comments = mapPoiDetailBeanResult.getData().getReview().getComment();
        List<MapPoiDetailBean.CommentBean> commentList = new ArrayList<>();
        for(MapPoiDetailBeanResult.DataBean.ReviewBean.CommentBean commentBean : comments) {
            MapPoiDetailBean.CommentBean comment = new MapPoiDetailBean.CommentBean();
            comment.setScore(commentBean.getScore());
            comment.setAuthor(commentBean.getAuthor());
            comment.setContent(commentBean.getReview());
            comment.setTime(commentBean.getTime());
            comment.setSrc_cn(commentBean.getSrc_cn());
            comment.setSrc_wapurl(commentBean.getReview_wapurl());

            commentList.add(comment);
        }

        detailBean.setList(commentList);
        return detailBean;
    }


    /**
     * 将List 类型的tag 转为string，以在数据库中存储
     * @param tagList
     * @return
     */
    public static String tagListToString(List<String> tagList) {
        StringBuilder builder = new StringBuilder();
        boolean needSplit = false;
        for(String tag : tagList) {
            if(needSplit) {
                builder.append("|");
            }
            builder.append(tag);
            needSplit = true;
        }

        return builder.toString();
    }

}
