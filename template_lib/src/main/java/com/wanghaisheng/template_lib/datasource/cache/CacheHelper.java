package com.wanghaisheng.template_lib.datasource.cache;

import com.wanghaisheng.template_lib.utils.SecurityHelper;


/**
 * Created by sheng on 2016/6/12.
 */
public class CacheHelper {

    public static final int SHORT_CACHE_TIME = 2 * ICache.TIME_HOUR;
    public static final int MIDULE_CACHE_TIME = 6 * ICache.TIME_HOUR;
    public static final int Long_CACHE_TIME = 12 * ICache.TIME_HOUR;
    public static final int MODULE_CACHE_TIME = 6 * ICache.TIME_HOUR;
    public static final int OKHTTP_CACHE_TIME = 2 * 60;
    public static final int OKHTTP_CACHE_SIZE = 50 * 1024 * 1024;
    public static final String OKHTTP_CACHE_PATH = "";


    /**
     * 生成cache的key
     * @param cachePrefix 缓存前缀
     * @param page 查询的页码或offset
     * @param args 其它参数，都是string类型的，比如channel，tag等
     * @return
     */
    public static String getCacheKey(String cachePrefix, String page, String... args ) {
        StringBuilder cacheKey = new StringBuilder().append(cachePrefix)
                .append("_").append(page);
        for(String arg : args) {
            cacheKey.append("_").append(arg);
        }

        return SecurityHelper.getMD5(cacheKey.toString());
    }

    /**************************获取  cachekey****************************/
    public static String getCacheKey(String baseUrl, String category, String tag, int page) {
        StringBuilder cacheKey = new StringBuilder().append(baseUrl)
                .append("_").append(category)
                .append("_").append(tag)
                .append("_").append(page);

        return SecurityHelper.getMD5(cacheKey.toString());
    }
}
