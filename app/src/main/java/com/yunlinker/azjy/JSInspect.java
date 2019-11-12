package com.yunlinker.azjy;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.ProductDetail;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;
import com.qiyukf.unicorn.api.YSFUserInfo;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.UmengText;
import com.yunlinker.addressbook.AddressBook;
import com.yunlinker.auth.WebPermissionManager;
import com.yunlinker.baseclass.BaseActivity;
import com.yunlinker.baseclass.BaseInspect;
import com.yunlinker.baseclass.BaseWebView;
import com.yunlinker.image.ImageTool;
import com.yunlinker.manager.HttpManager;
import com.yunlinker.map.Location;
import com.yunlinker.myApp;
import com.yunlinker.pay.OnlinePay;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.instagram.Instagram;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.twitter.Twitter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.yunlinker.auth.WebPermissionManager.StoragePermissions;
import static com.yunlinker.config.WebConfig.SCREEN;
import static com.yunlinker.config.WebConfig.saveKey;

/**
 * Created by lemon on 2017/7/27.
 */

public class JSInspect extends BaseInspect {

    public JSInspect(BaseWebView w, BaseActivity a) {
        super(w, a);
    }


    //注册推送
    @JavascriptInterface
    public void registerPush(final String obj) {
        int state = 0;
        String devicetk = "none";
        String tempTk = null;
        String url = null;
        if(obj != null) {
            try {
                JSONObject jb = new JSONObject(obj);
                mweb.setInsCode("registerPush",jb.getString("code"));
                state = jb.getInt("state");
                tempTk = jb.getString("token");
                url = jb.getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences sp = mactivity.getSharedPreferences(saveKey, Context.MODE_PRIVATE);
        if(state==1 || obj == null)
            devicetk = sp.getString("deviceToken", "");
              if(tempTk!=null && url!=null) {
                    String furl = url;
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("deviceType", tempTk.length()>0?"1":"0");
                    params.add("deviceToken", devicetk);
                    params.add("_token", tempTk);
                    params.add("_device","2");
                    OkHttpClient okHttpClient = new OkHttpClient();
                    final Request request1 = new Request.Builder()
                            .post(params.build())
                            .url(furl)
                            .build();
                    Call calle = okHttpClient.newCall(request1);
                    calle.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }
                    });
                }
        if(obj!=null)
            mweb.setValue("registerPush", "1");
    }

    //设置推送
    @JavascriptInterface
    public void setPush(final String obj) {
        String act = "";
        String open = "";
        try {
            JSONObject jb = new JSONObject(obj);
            mweb.setInsCode("setPush",jb.getString("code"));
            act = jb.getString("act");
            open = jb.getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final SharedPreferences sp = mactivity.getSharedPreferences(saveKey, Context.MODE_PRIVATE);
        if(act.equals("get")) {
            String closeStr = sp.getString("closePush", "");
            mweb.setValue("setPush",(TextUtils.equals(closeStr,"1")?"0":"1"));
        } else if(act.equals("set")) {
            final String openStr = open;
            PushAgent push = PushAgent.getInstance(mactivity);
            IUmengCallback pushCb = new IUmengCallback() {
                @Override
                public void onSuccess() {
                    SharedPreferences.Editor editor = sp.edit();
                    String res = openStr.equals("1")?"0":"1";
                    editor.putString("closePush", res);
                    editor.apply();
                    mweb.setValue("setPush",openStr);
                }
                @Override
                public void onFailure(String s, String s1) {
                    mweb.setValue("setPush","0");
                }
            };
            if(Integer.parseInt(open)==1) {
                push.enable(pushCb);
            } else {
                push.disable(pushCb);
            }
        }
    }
    //支付
    private OnlinePay payObj;
    public OnlinePay getPay() {
        return payObj;
    }
    @JavascriptInterface
    public void pay(String obj) {
        try{
            JSONObject jb = new JSONObject(obj);
            mweb.setInsCode("pay",jb.getString("code"));
            if(payObj==null) {
                payObj = new OnlinePay();
                payObj.payResult = new OnlinePay.PayResult() {
                    @Override
                    public void success() {
                        mweb.setValue("pay","{\"code\":1}");
                    }
                    @Override
                    public void error(String msg) {
                        mweb.setValue("pay","{\"code\":0,\"msg\":\""+msg+"\"}");
                    }
                };
            }
            payObj.startPay(jb, mactivity);
        }catch(Exception e){}
    }

    //获取位置信息
    @JavascriptInterface
    public void position(String code) {
        mweb.setInsCode("position",code);
        WebPermissionManager.getInstance(mactivity).CheckPermission(WebPermissionManager.LocationPermissions, new WebPermissionManager.OnPermissionBack() {
            @Override
            public void success() {
                Location.getInstance().position(mactivity, mweb);
            }

            @Override
            public void error() {
                mweb.setValue("position", "{\"code\":0}");
                Toast.makeText(mactivity,"授权失败，请设置权限后重试",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //分享
    @JavascriptInterface
    public void shareUrl(String obj) {
        int platform = 0;
        JSONObject jb = null;

        try{
            jb = new JSONObject(obj);
            mweb.setInsCode("shareUrl",jb.getString("code"));
            platform = Integer.parseInt(jb.getString("platform"));
           if (platform==1){
               FacebookShare(obj);
           }else if (platform==2){
               InstagramShare(obj);
           }else {
               TwitterShare(obj);
           }
        }catch (Exception e){

        }

    }


    /**
     * 指定平台分享facebook
     */
    private void FacebookShare(String s){
        try{
            JSONObject jb = new JSONObject(s);
            Facebook.ShareParams fb = new Facebook.ShareParams();
            fb.setTitle(jb.getString("title"));
            fb.setImageUrl(jb.getString("pic"));
            fb.setText(jb.getString("desc"));
            fb.setUrl(jb.getString("url"));
            fb.setTitleUrl(jb.getString("url"));
            fb.setShareType(Platform.SHARE_WEBPAGE);
            Platform facebook = ShareSDK.getPlatform(Facebook.NAME);
            facebook.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    mweb.setValue("shareUrl","1");
                    Log.e("Facebook", "onComplete: "+"分享成功" );
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    mweb.setValue("shareUrl","0");
                    Log.e("Facebook", "onComplete: "+"分享失败" );
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    mweb.setValue("shareUrl","0");
                    Log.e("Facebook", "onComplete: "+"分享失败" );
                }
            });
            facebook.share(fb);
        }catch(Exception e){

        }
    }

    /**
     * 指定平台分享twitter
     */
    private void TwitterShare(String s){
        try{
            JSONObject jb = new JSONObject(s);
            Twitter.ShareParams tt = new Twitter.ShareParams();
            tt.setShareType(Platform.SHARE_VIDEO);
            tt.setTitle(jb.getString("title"));
            tt.setImageUrl(jb.getString("pic"));
            tt.setText(jb.getString("desc"));
            tt.setUrl(jb.getString("url"));
            tt.setTitleUrl(jb.getString("url"));
            Platform twitter = ShareSDK.getPlatform(Twitter.NAME);
            twitter.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    mweb.setValue("shareUrl","1");
                    Log.e("Twitter", "onComplete: "+"分享成功" );
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    mweb.setValue("shareUrl","0");
                    Log.e("Twitter", "onComplete: "+"分享失败" );
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    mweb.setValue("shareUrl","0");
                    Log.e("Twitter", "onComplete: "+"分享失败" );
                }
            });
            twitter.share(tt);
        }catch(Exception e){

        }
    }


    /**
     * 指定平台分享Instagram
     */
    private void InstagramShare(String s){
        try{
            JSONObject jb = new JSONObject(s);
            Instagram.ShareParams ig = new Instagram.ShareParams();
            ig.setShareType(Platform.SHARE_VIDEO);
            ig.setTitle(jb.getString("title"));
            ig.setImageUrl(jb.getString("pic"));
            ig.setText(jb.getString("desc"));
            ig.setUrl(jb.getString("url"));
            ig.setTitleUrl(jb.getString("url"));
            Platform instagram = ShareSDK.getPlatform(Instagram.NAME);
            instagram.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    mweb.setValue("shareUrl","1");
                    Log.e("Instagram", "onComplete: "+"分享成功" );
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    mweb.setValue("shareUrl","0");
                    Log.e("Instagram", "onComplete: "+"分享失败" );
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    mweb.setValue("shareUrl","0");
                    Log.e("Instagram", "onComplete: "+"分享失败" );
                }
            });
            instagram.share(ig);
        }catch(Exception e){

        }
    }


    private UMShareListener umShareListener;
    private void startShare(JSONObject jb) {
        if(umShareListener==null) {
            umShareListener = new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    mweb.setValue("shareUrl","{\"code\":1,\"msg\": \"分享成功\"}");
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    mweb.setValue("shareUrl","{\"code\":0,\"msg\": \"分享失败\"}");
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    mweb.setValue("shareUrl","{\"code\":0,\"msg\": \"分享失败\"}");
                }
            };
        }
        if(jb != null) {
            String title = "";
            String pic = "";
            String url = "";
            String desc = "";
            String base64 = null;
            try {
                if(jb.has("base64"))
                    base64 = jb.getString("base64");
                Log.e("嘀嘀嘀嘀嘀嘀嘀嘀", "startShare: "+base64);
                title = jb.getString("title");
                Log.e("嘀嘀嘀嘀嘀嘀嘀嘀", "startShare: "+title);
                pic = jb.getString("pic");
                Log.e("嘀嘀嘀嘀嘀嘀嘀嘀", "startShare: "+pic);
                url = jb.getString("url");
                Log.e("嘀嘀嘀嘀嘀嘀嘀嘀", "startShare: "+url);
                desc = jb.getString("desc");
                Log.e("嘀嘀嘀嘀嘀嘀嘀嘀", "startShare: "+desc);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(TextUtils.isEmpty(base64) && (TextUtils.isEmpty(title) || TextUtils.isEmpty(pic) || TextUtils.isEmpty(url) || TextUtils.isEmpty(desc))) {
                Toast.makeText(mactivity.getApplicationContext(), "分享异常，请稍后重试", Toast.LENGTH_SHORT).show();
                return;
            }
            UMImage umimg = null;
            UMWeb web = null;
            if(base64!=null) {
                byte[] bt = Base64.decode(base64, Base64.DEFAULT);
                umimg = new UMImage(mactivity, bt);
            } else {
                web = new UMWeb(url);
                web.setTitle(title);//标题
                UMImage thumb =  new UMImage(mactivity, pic);
                web.setThumb(thumb);  //缩略图
                web.setDescription(desc);//描述
            }
            int platform = 0;
            try {
                platform = Integer.parseInt(jb.getString("platform"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ShareAction sa = new ShareAction(mactivity);
            if(umimg!=null) sa.withMedia(umimg);
            else sa.withMedia(web);
            sa.setCallback(umShareListener);
            if(platform>0) {
                SHARE_MEDIA m = SHARE_MEDIA.WEIXIN;
                SHARE_MEDIA m1 = SHARE_MEDIA.FACEBOOK;
                if(platform==2) {
                    m = SHARE_MEDIA.WEIXIN_CIRCLE;
                }
                if (platform==3){
                    m1 = SHARE_MEDIA.FACEBOOK_MESSAGER;
                }
                sa.setPlatform(m);
                sa.setPlatform(m1);
                sa.share();
            } else {
                sa.setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE);
                sa.open();
            }
        }
    }


    //打开外部浏览器
    @JavascriptInterface
    public void openUrlStr(String url){
        final Uri uri = Uri.parse(url);
        final Intent it = new Intent(Intent.ACTION_VIEW, uri);
        mactivity.startActivity(it);
    }

//    private UMAuthListener authListener = null;
//    @JavascriptInterface
//    public void extLogin(final String obj) {
//        UMShareConfig config = new UMShareConfig();
//        config.isNeedAuthOnGetUserInfo(true);
//        UMShareAPI.get(mactivity).setShareConfig(config);
//        String type = "1";
//        try {
//            JSONObject jb = new JSONObject(obj);
//            type = jb.getString("type");
//            mweb.setInsCode("extLogin", jb.getString("code"));
//        } catch (Exception e) {
//        }
//        final String platform = type;
//        if (authListener == null) {
//            authListener = new UMAuthListener() {
//                @Override
//                public void onStart(SHARE_MEDIA share_media) {
//
//                }
//
//                @Override
//                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                    JSONObject successJb = new JSONObject();
//                    try {
//                        String unionid = map.get("unionid");
//                        if (unionid == null || unionid.length() <= 0)
//                            unionid = map.get("openid");
//                        successJb.put("unionid", unionid);
//                        successJb.put("openid", map.get("openid"));
//                        successJb.put("nickname", map.get("screen_name"));
//                        successJb.put("sex", map.get("gender"));
//                        successJb.put("face", map.get("profile_image_url"));
//                        successJb.put("city", map.get("city"));
//                        successJb.put("province", map.get("province"));
//                        successJb.put("type", Integer.parseInt(platform));
//                        successJb.put("code", "1");
////                        Log.e("hehehehheheheeheh", "extLogin: "+"unionid"+successJb.put("unionid", unionid)+"openid"+successJb.put("openid", map.get("openid"))
////                        +"nickname"+successJb.put("nickname", map.get("screen_name"))+"sex"+successJb.put("sex", map.get("gender"))+"face"+successJb.put("face", map.get("profile_image_url"))
////                        +"city"+successJb.put("city", map.get("city"))+"province"+successJb.put("province", map.get("province"))
////                                +"type"+successJb.put("type", Integer.parseInt(platform))+"code"+successJb.put("code", "1"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    UMShareAPI.get(mactivity).deleteOauth(mactivity, (Integer.parseInt(platform) == 2 ? SHARE_MEDIA.QQ : SHARE_MEDIA.WEIXIN), null);
//                    if (successJb.has("unionid")) {
//                        mweb.setValue("extLogin", successJb.toString());
//                    } else {
//                        mweb.setValue("extLogin", "{\"code\":0,\"msg\": \"获取用户登录信息失败\"}");
//                    }
//                }
//
//                @Override
//                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//                    mweb.setValue("extLogin", "{\"code\":0,\"msg\": \"第三方登录失败\"}");
//                }
//
//                @Override
//                public void onCancel(SHARE_MEDIA share_media, int i) {
//                    mweb.setValue("extLogin", "{\"code\":0,\"msg\": \"用户取消登录\"}");
//                }
//            };
//        }
//       UMShareAPI.get(mactivity).getPlatformInfo(mactivity, Integer.parseInt(platform) == 2 ? SHARE_MEDIA.QQ : SHARE_MEDIA.WEIXIN, authListener);
////        UMShareAPI.get(mactivity).getPlatformInfo(mactivity, Integer.parseInt(platform) == 1 ? SHARE_MEDIA.TWITTER : SHARE_MEDIA.FACEBOOK, authListener);
////        if (Integer.parseInt(platform) == 1) {
////            UMShareAPI.get(mactivity).getPlatformInfo(mactivity, SHARE_MEDIA.TWITTER, authListener);
////        }else if (Integer.parseInt(platform) == 2){
////            UMShareAPI.get(mactivity).getPlatformInfo(mactivity, SHARE_MEDIA.FACEBOOK, authListener);
////        }else if (Integer.parseInt(platform) == 3){
////            UMShareAPI.get(mactivity).getPlatformInfo(mactivity, SHARE_MEDIA.WEIXIN, authListener);
////        }else {
////            UMShareAPI.get(mactivity).getPlatformInfo(mactivity, SHARE_MEDIA.QQ, authListener);
////        }
//    }

    @JavascriptInterface
    public void extLogin(String obj){
        final String type;
        Platform platform = null;
        try{

            JSONObject jb = new JSONObject(obj);
            type = jb.getString("type");
            mweb.setInsCode("extLogin", jb.getString("code"));
            if (type.equals("1")){
               platform = ShareSDK.getPlatform(Facebook.NAME);
            }else if (type.equals("2")){
                platform =ShareSDK.getPlatform(Twitter.NAME);
            }else {
                platform =ShareSDK.getPlatform(Instagram.NAME);
            }
            platform.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    Log.e("1111111111111", "onComplete: "+platform.getDb().exportData());
                    JSONObject successJb = new JSONObject();
                    try{
                        if (i==Platform.ACTION_AUTHORIZING){
                            PlatformDb platdb = platform.getDb();
                            successJb.put("platdb",platform.getDb().exportData());
                            successJb.put("token",platdb.getToken());
                            successJb.put("icon",platdb.getUserIcon());
                            successJb.put("id",platdb.getUserId());
                            successJb.put("nickname",platdb.getUserName());
                            successJb.put("formNname",platdb.getPlatformNname());
                            successJb.put("time",platdb.getExpiresTime());
                            successJb.put("type", Integer.parseInt(type));
                            successJb.put("code", "1");
//                            Log.e("111111111111111", "onComplete: "+platform.getDb().exportData());
//                            Log.e("111111111111111", "onComplete: "+platdb.getToken());
//                            Log.e("111111111111111", "onComplete: "+platdb.getUserIcon());
//                            Log.e("111111111111111", "onComplete: "+platdb.getUserId());
//                            Log.e("111111111111111", "onComplete: "+platdb.getUserName());
//                            Log.e("111111111111111", "onComplete: "+platdb.getUserGender());
//                            Log.e("111111111111111", "onComplete: "+platdb.getPlatformNname());
                        }
                    }catch (Exception e){

                    }
                    if (successJb.has("platdb")) {
                      mweb.setValue("extLogin", successJb.toString());
                    } else {
                       mweb.setValue("extLogin", "{\"code\":0,\"msg\": \"获取用户登录信息失败\"}");
                   }

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    mweb.setValue("extLogin", "{\"code\":0,\"msg\": \"第三方登录失败\"}");
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    mweb.setValue("extLogin", "{\"code\":0,\"msg\": \"用户取消登录\"}");
                }
            });
//            if (platform.isAuthValid()){
//                platform.removeAccount(true);
//                return;
//            }
            platform.SSOSetting(false);
            platform.authorize();
        }catch (Exception e){

        }

    }


    @JavascriptInterface
    public void qiyu(String objStr){
        try {
            JSONObject jb = new JSONObject(objStr);
            if(!jb.has("login") || jb.getInt("login")==0) {
                //注销用户
                Unicorn.logout();
            } else {
                //打开客服窗口
                //如果用户存在展示用户信息
                if(jb.has("user")) {
                    JSONObject user =  jb.getJSONObject("user");
                    YSFUserInfo userInfo = new YSFUserInfo();
                    userInfo.userId = user.getString("userId");
                    userInfo.authToken = "";
                    userInfo.data="[\n" +
                            "    {\"key\":\"real_name\", \"value\":\""+user.getString("real_name")+"\"},\n" +
                            "    {\"key\":\"mobile_phone\", \"value\":\"" + user.getString("mobile_phone") + "\"},\n" +
                            "    {\"key\":\"avatar\", \"value\": \"" + user.getString("avatar") + "\"},\n" +
                            "]";
                    YSFOptions o = myApp.options();
                    o.uiCustomization.rightAvatar = user.getString("avatar");
                    Unicorn.updateOptions(o);
                    Unicorn.setUserInfo(userInfo);
                }

                ConsultSource source = new ConsultSource("", "", "");

                if(jb.has("goods")) {
                    JSONObject goods =  jb.getJSONObject("goods");
                    ProductDetail.Builder gb =  new ProductDetail.Builder();
                    gb.setTitle(goods.getString("title"));
                    gb.setPicture(goods.getString("picture"));
                    gb.setDesc(goods.getString("desc"));
                    gb.setNote(goods.getString("note"));
                    gb.setAlwaysSend(true);
                    source.productDetail = gb.create();
                }

                Unicorn.openServiceActivity(mactivity, "皮革客服", source);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private ProgressDialog progressDialog;
    private List<String> downImgs = new ArrayList<>();
    @JavascriptInterface
    public void saveImg(final String obj){
        WebPermissionManager.getInstance(mactivity).CheckPermission(StoragePermissions, new WebPermissionManager.OnPermissionBack() {
            @Override
            public void success() {
                JSONObject jb = null;
                String urls = null;
                try{
                    jb = new JSONObject(obj);
                    mweb.setInsCode("saveImg",jb.getString("code"));
                    urls = jb.getString("urls");
                    String[] ds = urls.split(",");
                    for (String downImg : ds) {
                        downImgs.add(downImg);
                    }
                }catch(Exception e){}
                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(mactivity);
                        progressDialog.setMessage("正在下载图片中...");
                        progressDialog.show();
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getImgtoLocal();
                    }
                }).start();
            }

            @Override
            public void error() {
                mweb.setValue("saveImg","{\"code\":0,\"msg\": \"存储权限读取失败\"}");
            }
        });
    }

    public void getImgtoLocal() {
        if(downImgs.size()>0) {
            String url = downImgs.remove(downImgs.size()-1);
            URL imgUrl = null;
            HttpURLConnection connection=null;
            Bitmap bitmap=null;
            try {
                imgUrl = new URL(url);
                connection = (HttpURLConnection)imgUrl.openConnection();
                connection.setConnectTimeout(6000); //超时设置
                connection.setDoInput(true);
                connection.setUseCaches(false); //设置不使用缓存
                InputStream inputStream=connection.getInputStream();
                bitmap= BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bitmap!=null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                if(bitmap!=null) {
                    ImageTool.saveImageToGallery(mactivity, bitmap);
                }
            }
            getImgtoLocal();
        } else {
            mactivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });
            mweb.setValue("saveImg","{\"code\":1,\"msg\": \"保存完成\"}");
        }
    }


    @JavascriptInterface
    public void copyText(String text){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) mactivity.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Toast.makeText(mactivity.getApplicationContext(), "复制成功",
                Toast.LENGTH_SHORT).show();
    }


    @JavascriptInterface
    public void getAddressBook(String code) {
        mweb.setInsCode("getAddressBook",code);
        AddressBook.startGetList(mactivity, mweb);
    }

    @JavascriptInterface
    public void goMap(String code){
        mweb.setInsCode("goMap",code);
        Intent intent = new Intent(mactivity,MapViewActivity.class);
        mactivity.startActivity(intent);
    }





}