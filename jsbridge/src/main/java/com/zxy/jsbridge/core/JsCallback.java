package com.zxy.jsbridge.core;

import android.text.TextUtils;
import android.webkit.WebView;

import com.zxy.jsbridge.async.AsyncTaskExecutor;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * native结果数据返回格式:
 * var resultData = {
 * status: {
 * code: 0,//0成功，1失败
 * msg: '请求超时'//失败时候的提示，成功可为空
 * },
 * data: {}//数据
 * };
 * <p/>
 * Created by zhengxiaoyong on 16/4/18.
 */
public class JsCallback {
    private static final String CALLBACK_JS_FORMAT = "javascript:JsBridge.onComplete(%s,%s);";
    private WeakReference<WebView> mWebViewWeakRef;
    private String mPort;

    private JsCallback(WebView webView, String port) {
        this.mWebViewWeakRef = new WeakReference<>(webView);
        this.mPort = port;
    }

    public static JsCallback newInstance(WebView webView, String port) {
        return new JsCallback(webView, port);
    }

    public void call(boolean isInvokeSuccess, JSONObject resultData, String statusMsg) throws JsCallbackException {
        final WebView webView = mWebViewWeakRef.get();
        if (webView == null)
            throw new JsCallbackException("The WebView related to the JsCallback has been recycled!");
        JSONObject resultObj = new JSONObject();
        JSONObject status = new JSONObject();
        try {
            status.put("code", isInvokeSuccess ? 0 : 1);
            if (!TextUtils.isEmpty(statusMsg)) {
                status.put("msg", statusMsg);
            } else {
                status.put("msg", "");
            }
            resultObj.put("status", status);
            if (resultData != null) {
                resultObj.put("data", resultData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String callbackJs = String.format(Locale.getDefault(), CALLBACK_JS_FORMAT, mPort, resultObj.toString());
        if (AsyncTaskExecutor.isMainThread()) {
            webView.loadUrl(callbackJs);
        } else {
            AsyncTaskExecutor.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(callbackJs);
                }
            });
        }
    }

    public static void invokeJsCallback(JsCallback callback, boolean isInvokeSuccess, JSONObject resultData, String statusMsg) {
        if (callback == null)
            return;
        try {
            callback.call(isInvokeSuccess, resultData, statusMsg);
        } catch (JsCallbackException e) {
            e.printStackTrace();
        }
    }

    private static class JsCallbackException extends Exception {
        public JsCallbackException(String detailMessage) {
            super(detailMessage);
        }
    }

}
