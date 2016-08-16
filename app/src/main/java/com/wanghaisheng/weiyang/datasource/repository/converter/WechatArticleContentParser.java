package com.wanghaisheng.weiyang.datasource.repository.converter;

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
 * Created by sheng on 2016/6/17.
 */
public class WechatArticleContentParser implements IContentParser {
    @Override
    public List<BaseBean> parseArticleBeanList(String sourceHtml) {
        Document doc = Jsoup.parse(sourceHtml);

        Elements rootElems = doc.getElementsByTag("li");
        int length = rootElems.size();
        List<BaseBean> list = new ArrayList<>();
        for(int i=0; i<length; i++) {
            Element rootElem = rootElems.get(i);
            MeishiBean article = new MeishiBean();
            article.setModuleName(ModuleConstants.MODULE_IDENTITY_WECHATARTICLE);

            Elements hrefElems = rootElem.getElementsByClass("news_lst_tab2");
            if(hrefElems.size()>0) {
                String hrefUrl = hrefElems.first().attr("href");
                article.setArticleUrl(hrefUrl);
            }

            Elements imgElems = rootElem.select(".news_lst_thumb2 img");
            if(imgElems.size()>0) {
                String imgUrl = imgElems.first().attr("src");
                article.setImageUrls(imgUrl);
                List<String> imageLists = new ArrayList<>();
                imageLists.add(imgUrl);
                article.setImageUrlList(imageLists);
            }

            Elements titleElems = rootElem.select(".news_txt_box2 h3");
            if(titleElems.size()>0) {
                String title = titleElems.first().text();
                article.setTitle(title);
            }

            Elements belongElems = rootElem.select(".wb-id span");
            if(belongElems.size()>0) {
                String belong = belongElems.first().text();
                article.setAuthor(belong);
            }

            list.add(article);
        }

        return list;
    }

    @Override
    public String parseArticleBeanDetail(String sourceHtml) {
        return null;
    }
}
