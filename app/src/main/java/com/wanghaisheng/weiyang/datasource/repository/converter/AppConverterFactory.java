package com.wanghaisheng.weiyang.datasource.repository.converter;

import com.wanghaisheng.template_lib.datasource.beans.BaseBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by sheng on 2016/6/17.
 */
public final class AppConverterFactory extends Converter.Factory{
    private final IContentParser contentParser;

    /**
     * 根据模块名初始化不同的实体对象
     * @param contentParser 数据解析对象
     * @return
     */
    public static AppConverterFactory create(IContentParser contentParser) {
        return new AppConverterFactory(contentParser);
    }

    private AppConverterFactory(IContentParser contentParser) {
        this.contentParser = contentParser;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        //如果是该converter能处理的泛型类，则处理，否则返回null,给后面的converter处理
        if(type == String.class) {
            return new AppResponseBodyConverter<>(type,contentParser);
        }

        if (type instanceof ParameterizedType)//支持返回值是List<BaseBean> 泛型
        {
            Type rawType = ((ParameterizedType) type).getRawType();
            Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (rawType == List.class && actualType == BaseBean.class)
            {
                return new AppResponseBodyConverter<>(type,contentParser);
            }
        }

        return null;
    }
}
