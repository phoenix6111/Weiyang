package com.wanghaisheng.weiyang.datasource.repository.sohu;

import java.util.List;

/**
 * Author: sheng on 2016/8/9 18:25
 * Email: 1392100700@qq.com
 */
public class SohuResponseData {

    private List<PageBean> page;

    public List<PageBean> getPage() {
        return page;
    }

    public void setPage(List<PageBean> page) {
        this.page = page;
    }

    public static class PageBean {
        private String title;
        private String image;
        private String wapImg;
        private String url;
        private String source;
        private String text;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getWapImg() {
            return wapImg;
        }

        public void setWapImg(String wapImg) {
            this.wapImg = wapImg;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
