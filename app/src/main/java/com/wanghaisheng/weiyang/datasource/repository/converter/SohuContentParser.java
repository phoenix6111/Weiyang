package com.wanghaisheng.weiyang.datasource.repository.converter;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuFirstResponseData;
import com.wanghaisheng.weiyang.datasource.repository.sohu.SohuResponseBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Author: sheng on 2016/8/8 15:47
 * Email: 1392100700@qq.com
 */
public class SohuContentParser implements IContentParser {
    @Override
    public List<BaseBean> parseArticleBeanList(String sourceHtml) {
        return null;
    }

    public SohuFirstResponseData parseFirstPageBean(String str) {
        /*str = str.replace("var list=","");
        Gson gson = new Gson();
        LogUtils.d(str);
        return gson.fromJson(str,SohuFirstResponseData.class);*/

        str = str.replace("var list=","");
        if(str.startsWith("[")) {
            str = "{\"count\":0,\"page\":"+str+"}";
        }
        Gson gson = new Gson();
        return gson.fromJson(str,SohuFirstResponseData.class);
//        LogUtils.d(datas.getPage());

    }

    public List<SohuResponseBean> parseOtherPageBean(String str) {
        str = str.replace("var list=","");
        if(str.startsWith("[")) {
            str = "{\"count\":0,\"page\":"+str+"}";
        }
        Gson gson = new Gson();
        LogUtils.d(str);
        SohuFirstResponseData datas  = gson.fromJson(str,SohuFirstResponseData.class);
        LogUtils.d(datas.getPage());
        return datas.getPage();

    }

    public static String parseArticleBeanDetail2(String sourceHtml) {

        Document doc = Jsoup.parse(sourceHtml);
        StringBuilder builder = new StringBuilder();
        builder.append(getHeader());

        Elements contents = doc.getElementsByClass("article-wrapper");
        if(contents.size()>0) {
            builder.append(contents.get(0).outerHtml());
        }

        builder.append(getFooter());

        return builder.toString();
    }

    @Override
    public String parseArticleBeanDetail(String sourceHtml) {

        Document doc = Jsoup.parse(sourceHtml);
        StringBuilder builder = new StringBuilder();
        builder.append(getHeader());

        Elements contents = doc.getElementsByClass("article-wrapper");
        if(contents.size()>0) {
            builder.append(contents.get(0).outerHtml());
        }

        builder.append(getFooter());

        return builder.toString();
    }

    private static String getHeader() {
        return "<html><head><meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0\">\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "    <meta http-equiv=\"Cache-Control\" content=\"no-transform, no-cache\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,user-scalable=0,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0\">\n" +
                "    <link rel=\"stylesheet\" href=\"sohu.css\" media=\"all\">\n" +
                "</head><body class=\"body-text\">";
    }

    private static String getFooter() {
        return "<script src=\"sohu.js\"></script></body></html>";
    }
}
