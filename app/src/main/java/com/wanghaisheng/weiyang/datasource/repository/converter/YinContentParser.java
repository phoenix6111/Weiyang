package com.wanghaisheng.weiyang.datasource.repository.converter;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;


/**
 * Created by sheng on 2016/6/20.
 */
public class YinContentParser implements IContentParser {

    @Override
    public List<BaseBean> parseArticleBeanList(String sourceHtml) {
        return null;
    }

    @Override
    public String parseArticleBeanDetail(String sourceHtml) {
        StringBuilder builder = new StringBuilder();
        builder.append(getHeader());

        Document doc = Jsoup.parse(sourceHtml);

        Element contentElem = doc.getElementsByClass("m-article").first();

        if(contentElem != null) {
            builder.append(contentElem.outerHtml());
        }

        builder.append(getFooter());

        return builder.toString();
    }

    public String getHeader() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "    <meta content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0\" name=\"viewport\" />\n" +
                "    <link  href=\"yin.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "    </head><body spm-data=\"256\"><section class=\"wxjx-article\">";
    }

    private String getFooter() {
        return "</section></body></html>";
    }
}
