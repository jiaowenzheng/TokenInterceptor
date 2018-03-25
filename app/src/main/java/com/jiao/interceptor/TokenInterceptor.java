package com.jiao.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.jiao.HttpEngine;
import com.jiao.util.PreferenceUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * token 刷新拦截器
 *
 * @author jiaowenzheng
 *
 * Created by jiaowenzheng on 2018/2/11.
 */

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        //判断是response code 是否是401，如果不是401,则直接返回response
        if (response.code() != HttpEngine.ERROR_UNAUTHORIZED) {
            return response;
        }

        //如果刷新token接口失败则返回 token刷新失败 response
        //这个tokenUrl 是刷新token 的url
        String tokenUrl = "appv2/token";
        if (response.request().url().toString().endsWith(tokenUrl)){
            Log.i("interceptor","TokenInterceptor refresh token response code 401");
            return createRefreshTokenFailedResponse(response);
        }

        //同步
        PreferenceUtils.getTokenLock().lock();

        if (PreferenceUtils.isTokenRefreshBusy()) {
            Log.i("interceptor","TokenInterceptor Token refresh busy.");
            PreferenceUtils.getTokenLock().unlock();
            return response;
        }


        try {
            //刷新token 逻辑处理
             String token = refreshToken();
             if (!TextUtils.isEmpty(token)){
                 PreferenceUtils.setAuthorization(token);
                 Log.i("interceptor"," TokenInterceptor setAuthorization ");
             }else{
                 //跳转登录页面
                 login();
                 //创建刷新token 失败的 Response
                 response = createRefreshTokenFailedResponse(response);
             }
        } finally {
            Log.i("interceptor"," TokenInterceptor finally unlock ");
            PreferenceUtils.getTokenLock().unlock();
        }

        return response;
    }

    /**
     *
     * 登录
     *
     */
    public void login(){
        //实现方法
    }


    /**
     * 创建刷新token刷新失败 Response
     *
     * @param response
     * @return
     */
    public Response createRefreshTokenFailedResponse(Response response){
        return response.newBuilder()
                .code(HttpEngine.ERROR_REFRESH_TOKEN_FAILED)
                .build();
    }


    /**
     * 刷新token
     *
     * @return
     */
    private String refreshToken() throws IOException{

        //根据自己接口实现刷新token,请求必须是同步的，返回请求回来的token,如果出异常则返回null。

        return null;
    }
}
