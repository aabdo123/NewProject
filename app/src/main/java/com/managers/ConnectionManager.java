package com.managers;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

/**
 * Created by malikabuqaoud on 1/5/17.
 */

public class ConnectionManager {

    static final int DEFAULT_TIMEOUT = 120000;
    private static boolean isURLEncodingEnabled = false;

    public static HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization",
                PreferencesManager.getInstance().getStringValue(SharesPrefConstants.TOKEN_TYPE) + " " +
                        PreferencesManager.getInstance().getStringValue(SharesPrefConstants.ACCESS_TOKEN));
//        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }

    public static SSLContext getSslContext() {

        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};

        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext;
    }

    public static void doRequest(Context context, final String type, String apiRoute, RequestParams params, BaseJsonHttpResponseHandler<Object> responseHandler) {
        switch (type) {
            case AppConstant.GET:
                get(apiRoute, responseHandler);
                break;

            case AppConstant.GET_PARAMS:
                getWithParameters(apiRoute, params, responseHandler);
                break;

            case AppConstant.POST:
                postWithContext(context, apiRoute, params, responseHandler);
                break;

            case AppConstant.POST_WITH_CONTEXT:
                post(apiRoute, params, responseHandler);
                break;

            case AppConstant.POST_TIMEOUT:
                postTimeout(context, apiRoute, params, responseHandler);
                break;

            case AppConstant.FILES:
                files(context, apiRoute, params, responseHandler);
                break;
        }
    }

    public static void doRequestText(final String type, String apiRoute, RequestParams params, TextHttpResponseHandler responseHandler) {
        switch (type) {
            case AppConstant.GET:
            case AppConstant.GET_TEXT:
                getText(apiRoute, responseHandler);
                break;

            case AppConstant.POST:
            case AppConstant.POST_TEXT:
                postText(apiRoute, params, responseHandler);
                break;
        }
    }

    public static void doRequestJson(final String type, String apiRoute, RequestParams params, JsonHttpResponseHandler responseHandler) {
        switch (type) {
            case AppConstant.GET_JSON:
                getJSON(apiRoute, responseHandler);
                break;


            case AppConstant.POST_JSON:
                postJSON(apiRoute, params, responseHandler);
                break;
        }
    }

    public static void get(String apiRoute, BaseJsonHttpResponseHandler<Object> responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.get(apiRoute, responseHandler);
        // intermediate certificate authority
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.setTimeout(DEFAULT_TIMEOUT);
    }

    public static void getWithParameters(String apiRoute, RequestParams params, BaseJsonHttpResponseHandler<Object> responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.get(apiRoute, params, responseHandler);
        // intermediate certificate authority
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.setTimeout(DEFAULT_TIMEOUT);
    }


    public static void post(String apiRoute, RequestParams params, BaseJsonHttpResponseHandler<Object> responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.post(apiRoute, params, responseHandler);
        // intermediate certificate authority
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.setTimeout(DEFAULT_TIMEOUT);
    }


    public static void postWithContext(Context context, String apiRoute, RequestParams params, BaseJsonHttpResponseHandler<Object> responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.post(context, apiRoute, params, responseHandler);
        // intermediate certificate authority
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.setTimeout(DEFAULT_TIMEOUT);
    }

    public static void postTimeout(Context context, String apiRoute, RequestParams params, BaseJsonHttpResponseHandler<Object> responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.post(context, apiRoute, params, responseHandler);
        // intermediate certificate authority
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.setTimeout(DEFAULT_TIMEOUT);
    }

    public static void getByte(String apiRoute, AsyncHttpResponseHandler responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.get(apiRoute, responseHandler);
    }

    /////////   MULTIPART    /////////

    public static void files(Context context, String apiRoute, RequestParams params, ResponseHandlerInterface responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.post(context, apiRoute, params, responseHandler);
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
    }


    ///////////////
    ///////////////
    public static void getJSON(String apiRoute, JsonHttpResponseHandler responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.get(apiRoute, responseHandler);
        // intermediate certificate authority
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.setTimeout(DEFAULT_TIMEOUT);
    }

    public static void postJSON(String apiRoute, RequestParams params, JsonHttpResponseHandler responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.post(apiRoute, params, responseHandler);
        // intermediate certificate authority
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.setTimeout(DEFAULT_TIMEOUT);
    }


    public static void getText(String apiRoute, TextHttpResponseHandler responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.get(apiRoute, responseHandler);
        // intermediate certificate authority
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.setTimeout(DEFAULT_TIMEOUT);
    }

    public static void postText(String apiRoute, RequestParams params, TextHttpResponseHandler responseHandler) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(isURLEncodingEnabled);
        for (String key : getHeaders().keySet()) {
            client.addHeader(key, getHeaders().get(key));
        }
        client.post(apiRoute, params, responseHandler);
        // intermediate certificate authority
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.setTimeout(DEFAULT_TIMEOUT);
    }

    public static void cancelAllRequests() {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.cancelAllRequests(true);
    }
}