# **RainbowBridge**
A safe JsBridge framework!

----
## Gif
![JsBridge](http://7xswxf.com2.z0.glb.clouddn.com/blog/js.gif)

## **Js invoke Native protocol**

```
rainbow://class:port/method?params
```
params is a json string.

## **Return to the Js data format**

```
var resultData = {
    status: {
        code: 0,//0:Success，1:Failure
        msg: 'request timeout'//Failure to display a message, success can be null
    },
    data: {}//resultData，can be null
};
```
## **Call the Js callback format**

```
javascript:RainbowBridge.onComplete('port','resultData');
```
## **The method`s format can be invoked by Js**

```
public static void ***(WebView webView, JSONObject data, JsCallback callback) {
	 //...
	 JsCallback.invokeJsCallback(callback, true, result, null);
}
```
If the native method is time consuming operation.Such use:

```
public static void ***(WebView webView, JSONObject data, JsCallback callback) {
	 AsyncTaskExecutor.runOnAsyncThread(new Runnable() {
        @Override
        public void run() {
            //IO、decode、sqlite... 
            JsCallback.invokeJsCallback(callback, true, result, null);
        }
    });
}
        
```

