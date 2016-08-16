package com.wanghaisheng.weiyang.datasource.repository.converter;

import android.text.TextUtils;

import com.wanghaisheng.weiyang.common.ModuleConstants;
import com.wanghaisheng.weiyang.database.MeishiBean;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: sheng on 2016/8/7 16:41
 * Email: 1392100700@qq.com
 */
public class DouguoContentParser implements IContentParser {
    @Override
    public List<BaseBean> parseArticleBeanList(String sourceHtml) {

        Document doc = Jsoup.parse(sourceHtml);
        List<BaseBean> meishiBeen = new ArrayList<>();

        Elements primaryElements = doc.getElementsByClass("cp_box");
        if(primaryElements.size()>0) {
            for(Element elem : primaryElements) {
                MeishiBean meishiBean = new MeishiBean();
                meishiBean.setModuleName(ModuleConstants.MODULE_IDENTITY_DOUGUO);

                Elements imgElems = elem.select(".cp_pic img");
                if(imgElems.size()>0) {
                    Element imgElem = imgElems.first();
                    meishiBean.setImageUrls(imgElem.attr("src"));
                    List<String> imgList = new ArrayList<>();
                    imgList.add(meishiBean.getImageUrls());
                    meishiBean.setImageUrlList(imgList);
                }

                Elements urlElems = elem.getElementsByClass("cp_name");
                if(urlElems.size()>0) {
                    Element urlElem = urlElems.first();
                    String url = urlElem.attr("href");
                    meishiBean.setArticleId(parseArticleId(url));
                    meishiBean.setTitle(urlElem.text());
                }

                meishiBeen.add(meishiBean);
            }
        } else {
            Elements primaryElems = doc.getElementsByClass("scig");
            if(primaryElems.size()>0) {
                for(Element elem : primaryElems) {
                    MeishiBean meishiBean = new MeishiBean();
                    meishiBean.setModuleName(ModuleConstants.MODULE_IDENTITY_DOUGUO);

                    Elements imgElems = elem.select(".scoic .wh1410");
                    if(imgElems.size()>0) {
                        Element imgElem = imgElems.first();

                        String articleUrl = imgElem.attr("href");
                        meishiBean.setArticleId(parseArticleId(articleUrl));
                        meishiBean.setTitle(imgElem.attr("title"));

                        String imgUrl = imgElem.attr("style");
                        imgUrl = parseSearchUrl(imgUrl);
                        meishiBean.setImageUrls(imgUrl);
                        List<String> imgList = new ArrayList<>();
                        imgList.add(meishiBean.getImageUrls());
                        meishiBean.setImageUrlList(imgList);
                    }

                    meishiBeen.add(meishiBean);
                }
            }
        }

        return meishiBeen;

    }

    private String parseSearchUrl(String sourceStr) {
        if(TextUtils.isEmpty(sourceStr)) {
            return "";
        }

        String str = sourceStr.substring(sourceStr.lastIndexOf("("),sourceStr.lastIndexOf(")"));
        return str.substring(1,str.length());
    }

    private String parseArticleId(String url) {
        if(TextUtils.isEmpty(url)) {
            return "";
        }

        String[] arrs = url.split("\\/");
        String endStr = arrs[arrs.length-1];
        return endStr.split("\\.")[0];

    }

    @Override
    public String parseArticleBeanDetail(String sourceHtml) {

        StringBuilder builder = new StringBuilder();
        builder.append(getHeader());

        Document doc = Jsoup.parse(sourceHtml);

        Elements detailElems = doc.getElementsByClass("recipe-details");
        if(detailElems.size()>0) {
            Element detailElem = detailElems.first();
            Elements adsElems = detailElem.getElementsByClass("newdl");
            if(adsElems.size()>0) {
                adsElems.remove();
            }

            Elements tipsElem = detailElem.getElementsByClass("tips");
            if(tipsElem.size()>1) {
                tipsElem.get(1).remove();
            }

            builder.append(detailElem.outerHtml());
        }

        builder.append(getFooter());

        return builder.toString();

    }

    private String getHeader() {
        return "<html>\n" +
                "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\">\n" +
                "    <meta http-equiv=\"x-dns-prefetch-control\" content=\"on\">\n" +
                "    <link href=\"douguo.css\" charset=\"UTF-8\" rel=\"stylesheet\" type=\"text/css\">\n" +
                "    </head><body>";
    }

    private String getFooter() {
        return "</body></html>";
    }
}
