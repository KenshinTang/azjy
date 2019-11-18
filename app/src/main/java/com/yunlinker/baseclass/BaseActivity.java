package com.yunlinker.baseclass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.yunlinker.auth.WebPermissionManager;
import com.yunlinker.azjy.JSInspect;
import com.yunlinker.manager.ActivityResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.yunlinker.config.WebConfig.AssestRoot;
import static com.yunlinker.config.WebConfig.UMSHARE_CALLBACK_CODE;


/**
 * Created by lemon on 2017/7/26.
 */

public class BaseActivity extends Activity {
    private static final int REQUEST_CODE_SCAN = 0x0000;
    public static  String tempcode;

    public String loadUrl;

    protected BaseWebView mwebView;
    private ImageView splash;
    public BaseWebView getWebView() {
        return mwebView;
    }

  //  public RelativeLayout rootlay;
    public LinearLayout rootlay;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        //FullScreen.fullScreen(this);
        rootlay = new LinearLayout(this);
        setContentView(rootlay);
        addWebView();

        try {
            int i = 0;
            PackageInfo info = getPackageManager().getPackageInfo( getPackageName(),  PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                i++;
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                //KeyHash 就是你要的，不用改任何代码  复制粘贴 ;
                Log.e("=========",KeyHash);
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
    }







    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        if(mwebView!=null)
            mwebView.resumeWeb();

    }


    public AlertDialog dialog;
    @Override
    protected void onPause() {
        super.onPause();
        if(dialog!=null) dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mwebView != null) {
            mwebView.destroy();
        }
        MobclickAgent.onPause(this);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==UMSHARE_CALLBACK_CODE) {
            UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
        } else {
            ActivityResult.getInstance().resultBack(BaseActivity.this, requestCode, resultCode, data);
        }
        if(requestCode==REQUEST_CODE_SCAN&&resultCode==RESULT_OK) {
            if (data != null) {
                String result = data.getStringExtra("codedContent");
                mwebView.setInsCode("scanf", tempcode);
                mwebView.setValue("scanf", result);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WebPermissionManager.getInstance(BaseActivity.this).authBack(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        mwebView.setValue("0", "back");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean backV = true;
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
            default:
                backV = super.onKeyDown(keyCode, event);
                break;
        }
        return  backV;
    }



    //配置activity视图
    private void configAcView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //rootlay = new RelativeLayout(this);

    }


    //添加webview
    private JSInspect inp = null;
    //获取inspect对象
    public JSInspect jsInp() {
        return inp;
    }
    //添加视图
    protected void addWebView() {

        mwebView = new BaseWebView(this);
        LinearLayout.LayoutParams webLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mwebView.setLayoutParams(webLayoutParams);
        rootlay.addView(mwebView);

        mwebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        mwebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mwebView.setVerticalScrollBarEnabled(false); //垂直不显示
        inp = new JSInspect(mwebView, BaseActivity.this);
        mwebView.addJavascriptInterface(inp, "WeiLai");
        mwebView.setWebViewClient(new BaseClient(mwebView, BaseActivity.this));

//        if(BaseTools.isApkInDebug(this))
            mwebView.setWebContentsDebuggingEnabled(true);

        WebSettings setting = mwebView.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setDefaultTextEncodingName("utf-8");
        setting.setDomStorageEnabled(true);
        setting.setAllowFileAccess(true);
        setting.setAllowContentAccess(true);
        setting.setAllowFileAccessFromFileURLs(true);
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);


        loadUrl = getIntent().getStringExtra("sendUrl");
        if (!TextUtils.isEmpty(loadUrl)) {
            if(!loadUrl.startsWith("http") || !loadUrl.startsWith("file"))
             loadUrl = AssestRoot+loadUrl;
            mwebView.loadUrl(loadUrl);
        } else {
            //首页
            mwebView.loadUrl(AssestRoot+"login.html");
        }

        setKeyBoard();
    }

    private int screenHeight=0;
    private void setKeyBoard() {
        //---------------安卓键盘处理-----------------
        mwebView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mwebView.getWindowVisibleDisplayFrame(r);
                final int visibleHeight = r.height();
                if(screenHeight==0) {
                    screenHeight = visibleHeight;
                } else {
                    if(visibleHeight>100 && screenHeight!=visibleHeight) {
                        //有可能为打开键盘
                        mwebView.post(new Runnable() {
                            @Override
                            public void run() {
                                float ratio = (screenHeight-visibleHeight)/(float)screenHeight;
                                mwebView.loadUrl("javascript:window.keyBoardEvent&&keyBoardEvent(1,"+ratio+");");
                            }
                        });
                    } else {
                        //有可能为关闭键盘
                        mwebView.post(new Runnable() {
                            @Override
                            public void run() {
                                mwebView.loadUrl("javascript:window.keyBoardEvent&&keyBoardEvent(0);");
                            }
                        });
                    }
                }
            }
        });
    }




}
