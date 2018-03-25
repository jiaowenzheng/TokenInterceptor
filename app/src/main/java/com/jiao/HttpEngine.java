package com.jiao;

import com.jiao.interceptor.HeaderInterceptor;
import com.jiao.interceptor.RequestAfterTokenInterceptor;
import com.jiao.interceptor.TokenInterceptor;

import okhttp3.OkHttpClient;

/**
 *
 * http 请求引擎
 *
 * @author jiaowenzheng
 *
 * Created by jiaowenzheng on 2018/3/25.
 */

public class HttpEngine {

    /**token 名称*/
    public static final String AUTHORIZATION_KEY= "Authorization";

    //token过期状态码
    public static final int ERROR_UNAUTHORIZED = 401; //未授权的请求
    //刷新token 失败错误码
    public static final int ERROR_REFRESH_TOKEN_FAILED = 101;

    private static volatile HttpEngine mInstance;

    private OkHttpClient mOkhttpClient;

    private HttpEngine(){

        mOkhttpClient = new OkHttpClient.Builder()
                //Header 拦截器
                .addInterceptor(new HeaderInterceptor())
                //token 请求成功后，后处理拦截器
                .addInterceptor(new RequestAfterTokenInterceptor())
                //刷新token 拦截器
                .addInterceptor(new TokenInterceptor())
                //注意，以上拦截器的顺序一定不能乱。
                .build();
    }

    /**
     * 实例
     *
     * @return
     */
    public static HttpEngine getInstance(){
        if (mInstance == null){
            synchronized (HttpEngine.class){
                if (mInstance == null){
                    mInstance = new HttpEngine();
                }
            }
        }

        return mInstance;
    }

}
