package com.jiao.interceptor;


import com.jiao.HttpEngine;
import com.jiao.util.PreferenceUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;



/**
 * 添加公共请求header
 *
 * @author jiaowenzheng
 * @time 2017-01-17 15:48
 */
public final class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();

        Request request = original.newBuilder()
                //通过Interceptor来定义静态请求头
                //增加 Authorization
                .addHeader(HttpEngine.AUTHORIZATION_KEY, PreferenceUtils.getAuthorization())
                .build();

        return chain.proceed(request);
    }
}
