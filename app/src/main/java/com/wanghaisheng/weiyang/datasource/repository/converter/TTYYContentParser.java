package com.wanghaisheng.weiyang.datasource.repository.converter;

import com.wanghaisheng.weiyang.database.MeishiBean;

/**
 * Author: sheng on 2016/8/9 10:42
 * Email: 1392100700@qq.com
 */
public class TTYYContentParser {

    public static String parseArticleBeanDetail(MeishiBean meishiBean) {

        String title = "<div class=\"content_head\" id=\"marginb\"><p class=\"title font_36 jianju\">"+meishiBean.getTitle()+
                "</p><p class=\"article_content_info font_20 mar\"><span class=\"article_zuozhe color\"><a href=\"\">"+meishiBean.getAuthor()+
                "<i class=\"renzheng_icon\" ></i></a></span><span class=\"up_time color\">"+meishiBean.getPublishDate()+"</span></p></div>";

        String content = "<div class=\"content yyuan\">"+meishiBean.getDesc()+"</div>";

        return getHeader()+title+content+getFooter();
    }

    public static String getHeader() {
        return "<html lang=\"zh-CN\"><head><meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\">\n" +
                "    <link type=\"text/css\" rel=\"stylesheet\" href=\"ttyy.css\">\n" +
                "    <script src=\"ttyy.js\"></script><body>\n" +
                "<div class=\"container-fluid\">\n" +
                "    <div class=\"mian_content\">";
    }

    public static String getFooter() {
        return "</div></div></body></html>";
    }

}
