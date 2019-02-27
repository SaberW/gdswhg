package com.force.librarybase.utils;

import android.content.Context;
import android.text.TextUtils;

import com.force.librarybase.utils.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * Created by Lifeix on 2015/7/14.
 */
public class PingUrlUtils {

    private static final String HTTPS_PROTOCOL = "https";
    private static final String HTTP_PROTOCOL = "http";

    private PingUrlUtils() {
        throw new AssertionError();
    }


    /**
     * 初始化IP配置
     *
     * @param context
     */
    public static void checkDomain(final Context context, final String url, final CheckDomainListener checkDomainListener) {

        if (context == null) {
            return;
        }

        Thread checkDomainThread = new Thread() {//异步线程执行
            public void run() {
                connect(url, checkDomainListener);
            }
        };
        checkDomainThread.setPriority(Thread.MAX_PRIORITY);
        checkDomainThread.start();
    }


    private static void connect(String url, CheckDomainListener checkDomainListener) {

        if (TextUtils.isEmpty(url)) {
            return;
        }
        url = reviseUrl(url, false);
        String host;
        if (url.startsWith("http")) {
            try {
                host = new URL(url).getHost();
            } catch (MalformedURLException e) {
                return;
            }
        } else {
            host = url;
        }

        Socket connect = new Socket();
        try {
            connect.connect(new InetSocketAddress(host, 80), 5000);
            if (connect.isConnected()) {
                checkDomainListener.onSuccess(url);
            } else {
                checkDomainListener.onFailed(url);
            }
        } catch (IOException e) {
            checkDomainListener.onFailed(url);
            Logger.d("connect IOException:" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String reviseUrl(String url, boolean isHttps) {

        if (TextUtils.isEmpty(url)) {
            return "";
        }

        if (url.startsWith("http")) {
            return url;
        }

        StringBuilder sb = new StringBuilder();

        if (isHttps) {
            sb.append(HTTPS_PROTOCOL);
        } else {
            sb.append(HTTP_PROTOCOL);
        }

        sb.append("://");
        sb.append(url);

        return sb.toString();
    }


    public interface CheckDomainListener {

        void onSuccess(String url);

        void onFailed(String url);
    }

}
