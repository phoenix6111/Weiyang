package com.wanghaisheng.weiyang.datasource.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Author: sheng on 2016/8/16 19:31
 * Email: 1392100700@qq.com
 */
public class MapPoiDetailBean implements Serializable{

    private String poiId;
    private String name;
    //标识图片
    private String coverImg;
    //纬度
    private double longitude;
    //经度
    private double latitude;
    //分数：rating时显示
    private float score;
    //类别：如 西北菜 西餐厅
    private String classify;
    //平均消费
    private String price;
    //距离
    private String distance;
    //地址
    private String address;
    //电话
    private String tel;
    //推荐 菜
    private String tags;
    //营业时间
    private String opentime;
    //餐厅介绍
    private String intro;
    //分享地址
    private String shareUrl;

    private List<CommentBean> list;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public List<CommentBean> getList() {
        return list;
    }

    public void setList(List<CommentBean> list) {
        this.list = list;
    }

    public static class CommentBean {
        private String author;
        private String score;
        private String content;
        private String time;
        private String src_cn;
        private String src_wapurl;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSrc_cn() {
            return src_cn;
        }

        public void setSrc_cn(String src_cn) {
            this.src_cn = src_cn;
        }

        public String getSrc_wapurl() {
            return src_wapurl;
        }

        public void setSrc_wapurl(String src_wapurl) {
            this.src_wapurl = src_wapurl;
        }
    }











}
