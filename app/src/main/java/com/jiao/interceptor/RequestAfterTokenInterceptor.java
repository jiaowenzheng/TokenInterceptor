package com.jiao.interceptor;

import android.util.Log;

import com.jiao.HttpEngine;
import com.jiao.util.PreferenceUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 18/3/2.
 */

public class RequestAfterTokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        //判断是response code 是否是401，如果不是401,则直接返回response
        if (response.code() != HttpEngine.ERROR_UNAUTHORIZED) {
            return response;
        }

        Log.i("interceptor"," RequestAfterTokenInterceptor token ");
        //使用新的Token，创建新的请求
        Request newRequest = chain.request().newBuilder()
                .header(HttpEngine.AUTHORIZATION_KEY, PreferenceUtils.getAuthorization())
                .build();

        return chain.proceed(newRequest);
    }
}
