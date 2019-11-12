package com.yunlinker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mob.MobSDK;
import com.mob.commons.SHARESDK;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;
import com.qiyukf.unicorn.api.YSFOptions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yunlinker.azjy.R;
import com.yunlinker.baseclass.BaseActivity;
import com.yunlinker.image.GlideApp;
import com.yunlinker.manager.ActivityManager;
import com.yunlinker.azjy.MainActivity;
import com.yunlinker.upimage.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;

import java.io.File;

import static com.yunlinker.config.WebConfig.QQID;
import static com.yunlinker.config.WebConfig.QQSECRET;
import static com.yunlinker.config.WebConfig.QYSECRET;
import static com.yunlinker.config.WebConfig.UMKEY;
import static com.yunlinker.config.WebConfig.WXID;
import static com.yunlinker.config.WebConfig.WXSECRET;
import static com.yunlinker.config.WebConfig.saveKey;

/**
 * Created by lemon on 2017/8/21.
 */

public class myApp extends Application {
 private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "0925bbcc452bebcb6bc27509c2975243");
        initImagePicker();
        UMConfigure.setLogEnabled(true);
        UMConfigure.setEncryptEnabled(false);
        initUmeng();
        UMShareAPI.get(this);
        MobSDK.init(this);
//        initQiyu();
    }

    private void initImagePicker() {
        //初始化图片选择器
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    private void initUmeng() {

        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(getApplicationContext(),UMKEY,"OnlineApp"));

        final PushAgent mPushAgent = PushAgent.getInstance(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //注册推送服务，每次调用register方法都会回调该接口
                mPushAgent.register(new IUmengRegisterCallback() {

                    @Override
                    public void onSuccess(String deviceToken) {
                        //注册成功会返回device token
                        Log.e("deviceToken", deviceToken);
                        if(deviceToken!=null) {
                            SharedPreferences sp = getApplicationContext().getSharedPreferences(saveKey, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("deviceToken", deviceToken);
                            editor.apply();
                        }
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.e("deviceToken", s+"s1"+s1);
                    }
                });
            }
        }).start();
    }

    {
        //设置Log开关
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin(WXID, WXSECRET);
        PlatformConfig.setQQZone(QQID, QQSECRET);
       // PlatformConfig.setTwitter("2QCb1sVy6qXUJEVrslzVNfKzX","2VAMzLpBzNE34mZ2RVq40laKTOBCl1LQNjWTxhXreUuIQvrE2M");
    }



    static public boolean checkUpdate = false;


    private void initQiyu() {
        //----qiyu---
        Unicorn.init(this, QYSECRET, options(), new UnicornImageLoader(){
            @Nullable
            @Override
            public Bitmap loadImageSync(String uri, int width, int height) {
                return null;
            }
            @Override
            public void loadImage(String uri, int width, int height, final ImageLoaderListener listener) {
                if(!TextUtils.isEmpty(uri)){}
                    GlideApp.with(getApplicationContext()).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            listener.onLoadComplete(resource);
                        }
                    });


            }
        });

        Unicorn.addUnreadCountChangeListener(new UnreadCountChangeListener() {
            @Override
            public void onUnreadCountChange(int count) {
                BaseActivity a = ActivityManager.getInstance().getFirst();
                if(a!=null) {
                    a.getWebView().loadUrl("javascript:qiyuMsgCount&&qiyuMsgCount("+count+")");
                }
            }
        }, true);
    }

    //----qiyu---如果返回值为null，则全部使用默认参数。
    public static YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        options.statusBarNotificationConfig.notificationEntrance = MainActivity.class;
        options.savePowerConfig = new SavePowerConfig();
        options.uiCustomization = new UICustomization();
        options.uiCustomization.leftAvatar = "";
        return options;
    }




}
