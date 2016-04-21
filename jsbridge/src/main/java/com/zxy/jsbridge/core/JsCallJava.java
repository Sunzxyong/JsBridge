package com.zxy.jsbridge.core;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zhengxiaoyong on 16/4/19.
 */
public class JsCallJava {
    private static final String JS_BRIDGE_PROTOCOL_SCHEMA = "rainbow";
    private String mClassName;
    private String mMethodName;
    private String mPort;
    private JSONObject mParams;

    private JsCallJava() {
    }

    public static JsCallJava newInstance() {
        return new JsCallJava();
    }

    /**
     * @param webView WebView
     * @param message rainbow://class:port/method?params
     */
    public void call(WebView webView, String message) {
        if (webView == null || TextUtils.isEmpty(message))
            return;
        parseMessage(message);
        invokeNativeMethod(webView);
    }

    private void parseMessage(String message) {
        if (!message.startsWith(JS_BRIDGE_PROTOCOL_SCHEMA))
            return;
        Uri uri = Uri.parse(message);
        mClassName = uri.getHost();
        String path = uri.getPath();
        if (!TextUtils.isEmpty(path)) {
            mMethodName = path.replace("/", "");
        } else {
            mMethodName = "";
        }
        mPort = String.valueOf(uri.getPort());
        try {
            mParams = new JSONObject(uri.getQuery());
        } catch (JSONException e) {
            e.printStackTrace();
            mParams = new JSONObject();
        }
    }

    private void invokeNativeMethod(WebView webView) {
        Method method = NativeMethodInjectHelper.getInstance().findMethod(mClassName, mMethodName);
        String statusMsg;
        JsCallback jsCallback = JsCallback.newInstance(webView, mPort);
        if (method == null) {
            statusMsg = "Method (" + mMethodName + ") in this class (" + mClassName + ") not found!";
            JsCallback.invokeJsCallback(jsCallback, false, null, statusMsg);
            return;
        }
        Object[] objects = new Object[3];
        objects[0] = webView;
        objects[1] = mParams;
        objects[2] = jsCallback;
        try {
            method.invoke(null, objects);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
