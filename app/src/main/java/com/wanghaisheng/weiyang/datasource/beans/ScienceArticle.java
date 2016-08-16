package com.wanghaisheng.weiyang.datasource.beans;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "SCIENCE_ARTICLE".
 */
public class ScienceArticle implements java.io.Serializable {

    private static final long serialVersionUID = 3465714249275541176L;
    private Long id;
    private String article_time;
    private String img_link;
    private String article_intro;
    private String article_author;
    private String article_title;
    private String article_hot_num;
    private String article_author_link;
    private String article_author_dec;
    private String article_link;
    private Boolean article_video;
    private Boolean isCollected;

    public ScienceArticle() {
    }

    public ScienceArticle(Long id) {
        this.id = id;
    }

    public ScienceArticle(Long id, String article_time, String img_link, String article_intro, String article_author, String article_title, String article_hot_num, String article_author_link, String article_author_dec, String article_link, Boolean article_video, Boolean isCollected) {
        this.id = id;
        this.article_time = article_time;
        this.img_link = img_link;
        this.article_intro = article_intro;
        this.article_author = article_author;
        this.article_title = article_title;
        this.article_hot_num = article_hot_num;
        this.article_author_link = article_author_link;
        this.article_author_dec = article_author_dec;
        this.article_link = article_link;
        this.article_video = article_video;
        this.isCollected = isCollected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticle_time() {
        return article_time;
    }

    public void setArticle_time(String article_time) {
        this.article_time = article_time;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }

    public String getArticle_intro() {
        return article_intro;
    }

    public void setArticle_intro(String article_intro) {
        this.article_intro = article_intro;
    }

    public String getArticle_author() {
        return article_author;
    }

    public void setArticle_author(String article_author) {
        this.article_author = article_author;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_hot_num() {
        return article_hot_num;
    }

    public void setArticle_hot_num(String article_hot_num) {
        this.article_hot_num = article_hot_num;
    }

    public String getArticle_author_link() {
        return article_author_link;
    }

    public void setArticle_author_link(String article_author_link) {
        this.article_author_link = article_author_link;
    }

    public String getArticle_author_dec() {
        return article_author_dec;
    }

    public void setArticle_author_dec(String article_author_dec) {
        this.article_author_dec = article_author_dec;
    }

    public String getArticle_link() {
        return article_link;
    }

    public void setArticle_link(String article_link) {
        this.article_link = article_link;
    }

    public Boolean getArticle_video() {
        return article_video;
    }

    public void setArticle_video(Boolean article_video) {
        this.article_video = article_video;
    }

    public Boolean getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(Boolean isCollected) {
        this.isCollected = isCollected;
    }

}
