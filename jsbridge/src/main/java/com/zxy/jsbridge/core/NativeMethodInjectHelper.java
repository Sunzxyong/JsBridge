package com.zxy.jsbridge.core;


import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.webkit.WebView;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengxiaoyong on 16/4/19.
 * 符合注入的方法的格式:
 * public static void ***(WebView webView, JSONObject data, JsCallback callback){
 * //...
 * }
 */
public class NativeMethodInjectHelper {
    private volatile static NativeMethodInjectHelper sInstance;
    private ArrayMap<String, ArrayMap<String, Method>> mArrayMap;
    private List<Class<?>> mInjectClasses;

    public static NativeMethodInjectHelper getInstance() {
        NativeMethodInjectHelper instance = sInstance;
        if (instance == null) {
            synchronized (NativeMethodInjectHelper.class) {
                instance = sInstance;
                if (instance == null) {
                    instance = new NativeMethodInjectHelper();
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    private NativeMethodInjectHelper() {
        mArrayMap = new ArrayMap<>();
        mInjectClasses = new ArrayList<>();
    }

    public NativeMethodInjectHelper clazz(Class<?> clazz) {
        if (clazz == null)
            throw new NullPointerException("NativeMethodInjectHelper:The clazz can not be null!");
        mInjectClasses.add(clazz);
        return this;
    }

    public void inject() {
        int size = mInjectClasses.size();
        if (size != 0) {
            mArrayMap.clear();
            for (int i = 0; i < size; i++) {
                putMethod(mInjectClasses.get(i));
            }
            mInjectClasses.clear();
        }
    }

    public Method findMethod(String className, String methodName) {
        if (TextUtils.isEmpty(className) || TextUtils.isEmpty(methodName))
            return null;
        if (mArrayMap.containsKey(className)) {
            ArrayMap<String, Method> arrayMap = mArrayMap.get(className);
            if (arrayMap == null)
                return null;
            if (arrayMap.containsKey(methodName)) {
                return arrayMap.get(methodName);
            }
        }
        return null;
    }

    private void putMethod(Class<?> clazz) {
        if (clazz == null)
            return;
        ArrayMap<String, Method> arrayMap = new ArrayMap<>();
        Method method;
        Method[] methods = clazz.getDeclaredMethods();
        int length = methods.length;
        for (int i = 0; i < length; i++) {
            method = methods[i];
            int methodModifiers = method.getModifiers();
            if ((methodModifiers & Modifier.PUBLIC) != 0 && (methodModifiers & Modifier.STATIC) != 0 && method.getReturnType() == void.class) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes != null && parameterTypes.length == 3) {
                    if (WebView.class == parameterTypes[0] && JSONObject.class == parameterTypes[1] && JsCallback.class == parameterTypes[2]) {
                        arrayMap.put(method.getName(), method);
                    }
                }
            }
        }
        mArrayMap.put(clazz.getSimpleName(), arrayMap);
    }
}
