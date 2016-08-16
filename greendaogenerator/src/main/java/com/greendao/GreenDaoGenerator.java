package com.greendao;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
    public static final int VERSION = 52;
    public static final String GREEN_DAO_CODE_PATH = "../Weiyang/app/src/main/java";

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(VERSION, "com.wanghaisheng.weiyang.database");
        schema.enableKeepSectionsByDefault();

        Entity article = schema.addEntity("MeishiBean");
        article.addIdProperty();
        article.addStringProperty("articleId");//article id
        article.addStringProperty("title");//标题
        article.addStringProperty("desc");//描述
        article.addStringProperty("author");//作者
        article.addStringProperty("authorImg");//作者头像
        article.addStringProperty("authorId");//作者ID
        article.addStringProperty("articleUrl");//详情页url
        article.addStringProperty("sourceUrl");//来源页url
        article.addStringProperty("publishDate");//发布日期
        article.addStringProperty("imageUrls");//图片数组用|符号连接，存储地数据库
        article.addStringProperty("readCount");//阅读数
        article.addLongProperty("likeCount");//点赞数
        article.addLongProperty("shareCount");//分享数
        article.addLongProperty("forwardCount");//转发数
        article.addBooleanProperty("isVideo");//是否是视频
        article.addStringProperty("moduleName");
        article.addStringProperty("moduleTitle");
        article.addStringProperty("zhuanti");
        article.addStringProperty("cZhuanti");
        article.addStringProperty("tag");
        article.addStringProperty("cTag");
        article.addBooleanProperty("isCollected");//是否收藏
        article.implementsSerializable();

        Entity channel = schema.addEntity("Channel");
        channel.addIdProperty();
        channel.addStringProperty("channel_tag");//图片id
        channel.addStringProperty("channel_title");//图片组id
        channel.addStringProperty("channel_json");//图片标题

        File f = new File(GREEN_DAO_CODE_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }

        new DaoGenerator().generateAll(schema, f.getAbsolutePath());
    }
}
