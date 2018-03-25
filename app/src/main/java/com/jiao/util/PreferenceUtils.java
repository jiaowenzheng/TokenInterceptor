package com.jiao.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.jiao.HttpEngine;

import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * sharepreferences 用于用户信息的数据存储
 *
 * @author jiaowenzheng
 *
 * @time 2017/9/13
 *
 */
public class PreferenceUtils
{
    private final static int MODE = Context.MODE_PRIVATE;
    private static final String FILE_NAME = "jiao";
    private static SharedPreferences mSharedPreferences;

    /**定义一个重入锁*/
    private static ReentrantLock tokenLock = new ReentrantLock();
    /**token 最后刷新时间*/
    private static int lastTokenRefreshTime = 0;
    /** token 刷新时间间隔*/
    private static final int tokenRefreshFrozeTime = 10;


    /** token */
    public static void setAuthorization(String token){
        putString(HttpEngine.AUTHORIZATION_KEY, token);

        setTokenLastRefreshTime();
    }

    private static void setTokenLastRefreshTime() {
        lastTokenRefreshTime = (int)(System.currentTimeMillis()/1000);
    }

    public static int getLastTokenRefreshTime() {
        return lastTokenRefreshTime;
    }

    public static boolean isTokenRefreshBusy() {
        int curTime = (int)(System.currentTimeMillis()/1000);
        if (getLastTokenRefreshTime()>0) {
            if (curTime-getLastTokenRefreshTime() < tokenRefreshFrozeTime) {
                return true;
            }
        }
        return false;
    }


    public static String getAuthorization(){
        tokenLock.lock();
        try {
            return getString(HttpEngine.AUTHORIZATION_KEY, "");
        }finally {
            tokenLock.unlock();
        }
    }

    public static ReentrantLock getTokenLock() {
        return tokenLock;
    }

    public static void initialize(Context context)
    {
        if (mSharedPreferences == null){
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(FILE_NAME, MODE);
        }
    }

    public static boolean putString(String key, String value)
    {
        return mSharedPreferences.edit().putString(key, value).commit();
    }

    public static boolean putBoolean(String key, boolean value)
    {
        return mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean putInt(String key, int defValue)
    {
        return mSharedPreferences.edit().putInt(key, defValue).commit();
    }

    public static String getString(String key, String defValue)
    {
        return mSharedPreferences.getString(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue)
    {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public static int getInt(String key, int defValue)
    {
        return mSharedPreferences.getInt(key, defValue);
    }

    public static void removeKey(String key)
    {
        mSharedPreferences.edit().remove(key).commit();
    }
    
    public static void clear()
    {
        mSharedPreferences.edit().clear().commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void  putSet(String key, Set<String> set){
        mSharedPreferences.edit().putStringSet(key, set).commit();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getSet(String key, Set<String> def){
        return mSharedPreferences.getStringSet(key,def);
    }



}
