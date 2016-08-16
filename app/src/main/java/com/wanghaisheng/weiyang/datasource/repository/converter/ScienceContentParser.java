package com.wanghaisheng.weiyang.datasource.repository.converter;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by sheng on 2016/6/17.
 */
public class ScienceContentParser implements IContentParser {

    @Override
    public List<BaseBean> parseArticleBeanList(String sourceHtml) {

        return null;

    }

    @Override
    public String parseArticleBeanDetail(String sourceHtml) {

        StringBuilder builder = new StringBuilder();
        builder.append(getHeader());

        Document doc = Jsoup.parse(sourceHtml);
        Element tagElem = doc.getElementsByClass("lable-list").first();
        tagElem.addClass("hidden");
        doc.getElementsByClass("title").first().addClass("text-center");
        doc.getElementsByClass("article-author").first().addClass("text-left");
        doc.getElementsByClass("article-time").first().addClass("text-right");

        Element content = doc.getElementsByClass("article").first();
        builder.append(content.outerHtml());
        builder.append(getFooter());

        return builder.toString();

    }

    private String getHeader() {

        return "<html>\n" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1,minimum-scale=1,user-scalable=no\">\n" +
                "    <link rel=\"stylesheet\" href=\"science_article_detail.css\">\n" +
                "</head>\n" +
                "<body class=\"article-container\">\n" +
                "<div class=\"container\">\n" +
                "    <div class=\"col-lg-7 col-md-7 main content\">";

    }

    private String getFooter() {
        return "</div></div></body></html>";
    }

}
