package com.yunlinker.azjy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yunlinker.util.PageReport;

import java.net.URL;

public class PayPalActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requestPermission(Manifest.permission.RECORD_AUDIO);
        requestPermission(Manifest.permission.CAMERA);

        setContentView(R.layout.activity_pay_pal);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setUseWideViewPort(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                URL urls = null;
                String id = null;
                Log.e("111111", "shouldOverrideUrlLoading: "+url);
                try {
                    urls = new URL(url);
                    Log.e("1111111111", "shouldOverrideUrlLoading: "+urls.getQuery());
                    id=urls.getQuery();
                    if(url.contains("http://au.kaifaapp.cn/Australia/api/payment/success?")){
                        Intent intent1 = new Intent(PayPalActivity.this,MainActivity.class);
                        intent1.putExtra("sendUrl","paySuccess.html?"+id);
                        startActivity(intent1);
                        finish();
                    }
//                    }else if (url.contains("https://www.paypal.com/webapps/hermes")){
//                        Intent intent2 = new Intent(PayPalActivity.this,MainActivity.class);
//                        intent2.putExtra("sendUrl","mine.html");
//                        startActivity(intent2);
//                        finish();
//                        return true;
//                    }
                    if (url.contains("http://au.kaifaapp.cn/Australia/api/payment/cancel?")){
                        Intent intent3 = new Intent(PayPalActivity.this,MainActivity.class);
                        intent3.putExtra("sendUrl","orderDetail.html?"+id+"&from=paySuccess");
                        startActivity(intent3);
                        finish();
                    }


                } catch (Exception e) {

                }

                return false;

            }

        });

        webView.loadUrl(url);
        Log.d("kenshin", "load url:" + url);
        PageReport.getInstance().loadPage(this, PageReport.getPageNameFromUrl(url));
    }

    private void requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }
    }
}
