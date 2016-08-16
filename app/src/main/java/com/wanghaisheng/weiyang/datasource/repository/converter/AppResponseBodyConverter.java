package com.wanghaisheng.weiyang.datasource.repository.converter;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by sheng on 2016/6/17.
 */
public class AppResponseBodyConverter<T> implements Converter<ResponseBody,T> {
    private final Type type;
    private final IContentParser contentParser;//html内容解析类，策略模式

    public AppResponseBodyConverter(Type type, IContentParser contentParser) {
        this.type = type;
        this.contentParser = contentParser;
    }

    @Override
    public T convert(ResponseBody res) throws IOException {

        String resStr = res.string();

        if(String.class == type) {
            return (T) contentParser.parseArticleBeanDetail(resStr);
        }

        return (T) contentParser.parseArticleBeanList(resStr);
    }
}
