package com.wanghaisheng.weiyang.datasource.repository.converter;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;
import com.wanghaisheng.weiyang.common.ModuleConstants;
import com.wanghaisheng.weiyang.database.MeishiBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sheng on 2016/6/17.
 */
public class JianshuContentParser implements IContentParser {

    @Override
    public List<BaseBean> parseArticleBeanList(String sourceHtml) {
        Document doc = Jsoup.parse(sourceHtml);
        Elements primaryElements = doc.getElementsByClass("have-img");

        List<BaseBean> articleBeanList = new ArrayList<>();
        if(primaryElements != null && primaryElements.size()>0) {
            for(Element elem : primaryElements) {
                MeishiBean articleBean = new MeishiBean();
                articleBean.setModuleName(ModuleConstants.MODULE_IDENTITY_JIANSHU);

                Elements titleElems = elem.select(".title a");
                if(titleElems.size()>0) {
                    Element titleElem = titleElems.first();
                    String title = titleElem.text();
                    if(title.contains("简书")) {
                        continue;
                    }
                    articleBean.setTitle(title);
                    articleBean.setArticleUrl(titleElem.attr("href"));
                }

                Elements imgElems = elem.select(".wrap-img img");
                if(imgElems.size()>0) {
                    List<String> imgList = new ArrayList<>();
                    imgList.add(imgElems.first().attr("src"));
                    articleBean.setImageUrlList(imgList);
                    articleBean.setImageUrls(articleBean.getImageUrlList().get(0));
                }

                Elements authorElems = elem.getElementsByClass("author-name");
                if(authorElems.size()>0) {
                    Element authorElem = authorElems.first();
                    articleBean.setAuthor(authorElem.text());
                }

                Elements timeElems = elem.getElementsByClass("time");
                if(timeElems.size()>0) {
                    articleBean.setPublishDate(timeElems.first().attr("data-shared-at"));
                }

                articleBeanList.add(articleBean);
            }
        }

        return articleBeanList;
    }

    @Override
    public String parseArticleBeanDetail(String sourceHtml) {

        Document doc = Jsoup.parse(sourceHtml);

        StringBuilder builder = new StringBuilder();
        builder.append(getHeader());
        Element titleElem = doc.getElementsByClass("title").first();
        if(titleElem  != null) {
            builder.append(titleElem.outerHtml());
        }

        Element articleInfo = doc.getElementsByClass("article-info").first();
        if(articleInfo != null) {
            builder.append(articleInfo.outerHtml());
        }

        Element content = doc.getElementsByClass("content").first();
        if(content != null) {
            builder.append(content.outerHtml());
        }

        builder.append(getFooter());

        return builder.toString();

    }

    private String getHeader() {

        return "<!DOCTYPE html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0,user-scalable=no\">\n" +
                "    <meta http-equiv=\"Cache-Control\" content=\"no-siteapp\"/>\n" +
                "    <link rel=\"stylesheet\" media=\"all\" href=\"jianshu_detail.css\"/>\n" +
                "<script src=\"jquery.js\"></script><script src=\"disable_a.js\"></script>"+
                "</head>\n" +
                "\n" +
                "<body class=\"wechat-web reader-day-mode reader-font2\">\n" +
                "<div class=\"container\">\n";


    }

    private String getFooter() {
        return "</div>\n" +
                "</body>\n" +
                "</html>";
    }


}
