package com.wanghaisheng.weiyang.datasource.beans;

import java.io.Serializable;

/**
 * Author: sheng on 2016/8/15 18:51
 * Email: 1392100700@qq.com
 */
public class MapPoiBean implements Serializable{

    //poiid
    private String poiId;
    //Poi name
    private String name;
    private String disp_name;
    //评分
    private String rating;
    //电话号码
    private String tel;
    //编号
    private String areacode;
    //城市名
    private String cityname;
    //纬度
    private double longitude;
    //经度
    private double latitude;
    //地址：没有加城市名
    private String address;
    //距离搜索点的距离
    private String distance;

    private String adcode;
    //图片
    private String pic_info;
    //tag：如 清真菜，综合酒楼
    private String tag;
    //平均价：人均￥52
    private String price;

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

    public String getDisp_name() {
        return disp_name;
    }

    public void setDisp_name(String disp_name) {
        this.disp_name = disp_name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getPic_info() {
        return pic_info;
    }

    public void setPic_info(String pic_info) {
        this.pic_info = pic_info;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
