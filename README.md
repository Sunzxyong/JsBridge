# **RainbowBridge**
A safe JsBridge framework!
![](https://img.shields.io/badge/Android%20Arsenal-RainbowBridge-green.svg)
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
resultData = {
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

## License
> The MIT License (MIT)

> Copyright (c) 2015 郑晓勇
>
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
>
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
>
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
