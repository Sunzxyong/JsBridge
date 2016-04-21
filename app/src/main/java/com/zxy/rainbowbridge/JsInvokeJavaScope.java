package com.zxy.rainbowbridge;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import com.zxy.jsbridge.async.AsyncTaskExecutor;
import com.zxy.jsbridge.core.JsCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhengxiaoyong on 16/4/19.
 */
public class JsInvokeJavaScope {

    public static void showToast(WebView webView, JSONObject data, JsCallback callback) {
        Toast.makeText(webView.getContext(), data.toString(), Toast.LENGTH_SHORT).show();
        JsCallback.invokeJsCallback(callback, true, null, null);
    }

    public static void getIMSI(final WebView webView, JSONObject data, final JsCallback callback) {
        TelephonyManager telephonyManager = ((TelephonyManager) webView.getContext().getSystemService(Context.TELEPHONY_SERVICE));
        String imsi = telephonyManager.getSubscriberId();
        if (TextUtils.isEmpty(imsi)) {
            imsi = telephonyManager.getDeviceId();
        }
        JSONObject result = new JSONObject();
        try {
            result.put("imsi", imsi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsCallback.invokeJsCallback(callback, true, result, null);
    }

    public static void getAppName(final WebView webView, JSONObject data, final JsCallback callback) {
        String appName;
        try {
            PackageManager packageManager = webView.getContext().getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(webView.getContext().getApplicationContext().getPackageName(), 0);
            appName = packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (Exception e) {
            e.printStackTrace();
            appName = "";
        }
        JSONObject result = new JSONObject();
        try {
            result.put("result", appName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsCallback.invokeJsCallback(callback, true, result, null);
    }

    public static void getOsSdk(WebView webView, JSONObject data, JsCallback callback) {
        JSONObject result = new JSONObject();
        try {
            result.put("os_sdk", Build.VERSION.SDK_INT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsCallback.invokeJsCallback(callback, true, result, null);
    }

    public static void finish(WebView webView, JSONObject data, JsCallback callback) {
        if (webView.getContext() instanceof Activity) {
            ((Activity) webView.getContext()).finish();
        }
    }

    public static void delayExecuteTask(WebView webView, JSONObject data, final JsCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                JSONObject result = new JSONObject();
                try {
                    result.put("result", "延迟3s执行native方法");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsCallback.invokeJsCallback(callback, true, result, null);
            }
        }, 3000);
    }

    public static void performTimeConsumeTask(WebView webView, JSONObject data, final JsCallback callback) {
        AsyncTaskExecutor.runOnAsyncThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JSONObject result = new JSONObject();
                try {
                    result.put("result", "执行耗时操作后的返回");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsCallback.invokeJsCallback(callback, true, result, null);
            }
        });
    }

}
