package sample.narenderhttpurl.com.httpurlconnhelper.async;

import android.content.ContentValues;

import sample.narenderhttpurl.com.httpurlconnhelper.HttpConnectionHelper;
import sample.narenderhttpurl.com.httpurlconnhelper.ResponseCallback;
import sample.narenderhttpurl.com.httpurlconnhelper.ResponseLoadingFailure;
import sample.narenderhttpurl.com.httpurlconnhelper.async.RequestType;
import sample.narenderhttpurl.com.httpurlconnhelper.async.BackgroundExecutorService;
import sample.narenderhttpurl.com.httpurlconnhelper.async.BackgroundJob;

/**
 * Created by narendergusain on 12/6/16.
 */
public class ConnectionHelper {


    /**
     *
     * @param requestType
     * @param url
     * @param callback
     */
    public static void request(RequestType requestType, String url, ResponseCallback callback) {
        requestApi(requestType, url, null, null, callback);
    }

    /**
     *
     * @param requestType
     * @param url
     * @param header
     * @param callback
     */
    public static void request(RequestType requestType, String url, ContentValues header, ResponseCallback callback) {
        requestApi(requestType, url, header, null, callback);
    }

    /**
     *
     * @param requestType
     * @param url
     * @param header
     * @param body
     * @param callback
     */
    public static void request(RequestType requestType, String url, ContentValues header, ContentValues body, ResponseCallback callback) {
        requestApi(requestType, url, header, body, callback);
    }

    private static void requestApi(final RequestType requestType, final String url, final ContentValues header, final ContentValues body, final ResponseCallback callback) {
        BackgroundExecutorService.INSTANCE.enqueue(new BackgroundJob<Object>() {
            @Override
            public Object executeInBackground() throws Exception {
                return new HttpConnectionHelper().enqueue(requestType, url, header, body);//api.autocomplete(s, resultType).predictions;
            }

            @Override
            public void onSuccess(Object result) {
                if (result != null) {
                    callback.onSuccess(result);
                } else {
                    callback.onFailure(new ResponseLoadingFailure(url));
                }
            }

            @Override
            public void onFailure(final Throwable error) {
                callback.onFailure(new ResponseLoadingFailure(url, error));
            }
        });
    }
}
