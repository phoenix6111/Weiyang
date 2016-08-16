package com.wanghaisheng.weiyang.datasource.repository.converter;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by sheng on 2016/6/17.
 */
public class SohuResponseBodyConverter<T> implements Converter<ResponseBody,T> {
    private final Type type;
    private final SohuContentParser contentParser;//html内容解析类，策略模式

    public SohuResponseBodyConverter(Type type, SohuContentParser contentParser) {
        this.type = type;
        this.contentParser = contentParser;
    }

    @Override
    public T convert(ResponseBody res) throws IOException {

        String resStr = new String(res.bytes(),"gbk");

        if(String.class == type) {
            return (T) contentParser.parseArticleBeanDetail(resStr);
        }

        return (T) contentParser.parseFirstPageBean(resStr);

    }
}
