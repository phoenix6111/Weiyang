package com.wanghaisheng.weiyang.datasource.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: sheng on 2016/8/15 10:11
 * Email: 1392100700@qq.com
 */
public class MapPoiDetailBeanResult {

    private String status;

    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private BaseBean base;

        private DiningBean dining;
        private ReviewBean review;
        /**
         * 星级 : 4.5
         */

        private ScoreBean score;
        private String share_url;

        private List<PicBean> pic;

        public BaseBean getBase() {
            return base;
        }

        public void setBase(BaseBean base) {
            this.base = base;
        }

        public DiningBean getDining() {
            return dining;
        }

        public void setDining(DiningBean dining) {
            this.dining = dining;
        }

        public ReviewBean getReview() {
            return review;
        }

        public void setReview(ReviewBean review) {
            this.review = review;
        }

        public ScoreBean getScore() {
            return score;
        }

        public void setScore(ScoreBean score) {
            this.score = score;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public List<PicBean> getPic() {
            return pic;
        }

        public void setPic(List<PicBean> pic) {
            this.pic = pic;
        }

        public static class BaseBean {
            private String address;
            private String telephone;
            private String new_keytype;
            private String poiid;
            private String checked;
            private String name;
            @SerializedName("y")
            private String latitude;
            @SerializedName("x")
            private String longitude;
            private String classify;
            private String tag;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getNew_keytype() {
                return new_keytype;
            }

            public void setNew_keytype(String new_keytype) {
                this.new_keytype = new_keytype;
            }

            public String getPoiid() {
                return poiid;
            }

            public void setPoiid(String poiid) {
                this.poiid = poiid;
            }

            public String getChecked() {
                return checked;
            }

            public void setChecked(String checked) {
                this.checked = checked;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getClassify() {
                return classify;
            }

            public void setClassify(String classify) {
                this.classify = classify;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }
        }

        public static class DiningBean {
            private String src_star;
            private String tag_special;
            private String opentime2;
            private int price;
            private int wifi;
            private String intro;

            private List<MenuInfoBean> menu_info;

            public String getSrc_star() {
                return src_star;
            }

            public void setSrc_star(String src_star) {
                this.src_star = src_star;
            }

            public String getTag_special() {
                return tag_special;
            }

            public void setTag_special(String tag_special) {
                this.tag_special = tag_special;
            }

            public String getOpentime2() {
                return opentime2;
            }

            public void setOpentime2(String opentime2) {
                this.opentime2 = opentime2;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getWifi() {
                return wifi;
            }

            public void setWifi(int wifi) {
                this.wifi = wifi;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public List<MenuInfoBean> getMenu_info() {
                return menu_info;
            }

            public void setMenu_info(List<MenuInfoBean> menu_info) {
                this.menu_info = menu_info;
            }

            public static class MenuInfoBean {
                private String price_u;
                private String name;
                private Object price;

                private List<PicInfoBean> pic_info;

                public String getPrice_u() {
                    return price_u;
                }

                public void setPrice_u(String price_u) {
                    this.price_u = price_u;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Object getPrice() {
                    return price;
                }

                public void setPrice(Object price) {
                    this.price = price;
                }

                public List<PicInfoBean> getPic_info() {
                    return pic_info;
                }

                public void setPic_info(List<PicInfoBean> pic_info) {
                    this.pic_info = pic_info;
                }

                public static class PicInfoBean {
                    private int srcheight;
                    private String title;
                    private String url;
                    private int iscover;
                    private int srcwidth;

                    public int getSrcheight() {
                        return srcheight;
                    }

                    public void setSrcheight(int srcheight) {
                        this.srcheight = srcheight;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public int getIscover() {
                        return iscover;
                    }

                    public void setIscover(int iscover) {
                        this.iscover = iscover;
                    }

                    public int getSrcwidth() {
                        return srcwidth;
                    }

                    public void setSrcwidth(int srcwidth) {
                        this.srcwidth = srcwidth;
                    }
                }
            }
        }

        public static class ReviewBean {

            private List<CommentBean> comment;

            public List<CommentBean> getComment() {
                return comment;
            }

            public void setComment(List<CommentBean> comment) {
                this.comment = comment;
            }

            public static class CommentBean {
                private String review_weburl;
                private String author;
                private String review;
                private String time;
                private String score;
                private String review_wapurl;
                private String src_cn;

                public String getReview_weburl() {
                    return review_weburl;
                }

                public void setReview_weburl(String review_weburl) {
                    this.review_weburl = review_weburl;
                }

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
                }

                public String getReview() {
                    return review;
                }

                public void setReview(String review) {
                    this.review = review;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getScore() {
                    return score;
                }

                public void setScore(String score) {
                    this.score = score;
                }

                public String getReview_wapurl() {
                    return review_wapurl;
                }

                public void setReview_wapurl(String review_wapurl) {
                    this.review_wapurl = review_wapurl;
                }

                public String getSrc_cn() {
                    return src_cn;
                }

                public void setSrc_cn(String src_cn) {
                    this.src_cn = src_cn;
                }
            }
        }

        public static class ScoreBean {
            @SerializedName("星级")
            private String score;

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }
        }

        public static class PicBean {
            private String url;
            private String title;
            private int iscover;

            public int getIscover() {
                return iscover;
            }

            public void setIscover(int iscover) {
                this.iscover = iscover;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
