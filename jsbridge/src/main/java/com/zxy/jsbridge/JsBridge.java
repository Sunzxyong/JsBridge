package com.zxy.jsbridge;

import com.zxy.jsbridge.core.NativeMethodInjectHelper;

/**
 * Created by zhengxiaoyong on 16/4/19.
 */
public class JsBridge {
    private volatile static JsBridge sInstance;

    private JsBridge() {
    }

    public static JsBridge getInstance() {
        JsBridge instance = sInstance;
        if (instance == null) {
            synchronized (JsBridge.class) {
                instance = sInstance;
                if (instance == null) {
                    instance = new JsBridge();
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    public NativeMethodInjectHelper clazz(Class<?> clazz) {
        return NativeMethodInjectHelper.getInstance()
                .clazz(clazz);
    }
}
