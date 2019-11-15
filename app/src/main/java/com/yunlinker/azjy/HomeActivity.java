package com.yunlinker.azjy;

import com.yunlinker.util.PageReport;

/**
 * Created by lemon on 2017/10/8.
 */

public class HomeActivity extends MainActivity {

    @Override
    protected void addWebView() {
        super.addWebView();
        mwebView.setAlpha(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PageReport.getInstance().loadPage(this, "finish");
    }
}
