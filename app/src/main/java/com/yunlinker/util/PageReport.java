package com.yunlinker.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mobstat.StatService;

public class PageReport {
    private static PageReport instance = new PageReport();

    private String currentPage = "";

    private PageReport() {

    }

    public static PageReport getInstance() {
        if (instance == null) {
            instance = new PageReport();
        }
        return instance;
    }

    public void loadPage(Context context, String page) {
        if (!TextUtils.isEmpty(currentPage)) {
            StatService.onPageEnd(context, currentPage);
            Log.i("kenshin", "onPageEnd(" + currentPage + ")");
        }

        if (!page.equals("finish")) {
            StatService.onPageStart(context, page);
            Log.i("kenshin", "onPageStart(" + page + ")");
            currentPage = page;
        }
    }

    public static String getPageNameFromUrl(String url) {
        String pageName = "";
        if (!TextUtils.isEmpty(url)) {
            if (url.equals("finish")) {
                return "finish";
            }
            pageName = url.substring(url.lastIndexOf('/') + 1, url.indexOf(".html"));
        }

        return pageName;
    }
}
