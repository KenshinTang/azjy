package com.yunlinker.pay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yunlinker.baseclass.BaseActivity;
import com.yunlinker.manager.HttpManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by lemon on 2017/9/21.
 */

public class OnlinePay {

    public interface PayResult {
        void success();
        void error(String msg);
    }
    public PayResult payResult;


    private ProgressDialog pg;

    public void startPay(JSONObject obj, final Activity a) {
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pg = ProgressDialog.show(a, "提示", "正在获取支付信息");
            }
        });
        String ordersid = "";
        String type = "";
        String url = "";
        String _token = "";
        String _device ="";
        String ctype ="";
        String rechamountid ="";
        String rechmoney ="";

        try {
            Log.e("obbbjooo", "startPay: "+obj);
            if(obj.has("ordersid"))
                ordersid = obj.getString("ordersid");
            if(obj.has("type"))
                type = obj.getString("type");
            if(obj.has("url"))
                url = obj.getString("url");
            if(obj.has("_token"))
                _token = obj.getString("_token");
            if (obj.has("_device"))
                _device = obj.getString("_device");
            if (obj.has("ctype"))
                ctype = obj.getString("ctype");
            if (obj.has("rechamountid"))
                rechamountid = obj.getString("rechamountid");
            if (obj.has("rechmoney"))
                rechmoney =obj.getString("rechmoney");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String errMsg = "网络异常，支付失败，请稍后重试";
        if(!TextUtils.isEmpty(ordersid)||!TextUtils.isEmpty(type)||!TextUtils.isEmpty(url)) {
            String resStr = HttpManager.GET(url + "?ordersid="+ordersid+"&type="+type+"&_token="+_token+"&_device="+_device+"&ctype="+ctype+"&rechmoney="+rechmoney+"&rechamountid="+rechamountid);
            Log.e("支付宝成功没有", "startPay: "+resStr);
            try {
                JSONObject res = new JSONObject(resStr);
                if(res.has("code")) {
                    if(res.getInt("code") == 1) {
                        if(TextUtils.equals(type,"1"))
                            alipay(res.getString("data"), a);
                        else if(TextUtils.equals(type,"3"))
                            wechatpay(res.getString("data"), a);
                        return;
                    } else {
                        errMsg = res.getString("msg");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            errMsg = "支付参数错误，支付失败，请稍后重试";
        if(payResult!=null) {
            if(pg!=null) {
                pg.dismiss();
                pg=null;
            }
            payResult.error(errMsg);
        }
    }


    private void alipay(final String order, final Activity a) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(a);
//                Map<String, String> result = alipay.payV2(order,true);
//                String rs = result.get("resultStatus");
//                String errMsg = "支付失败，请稍后重试";
//                if(TextUtils.equals(rs, "6001")) {
//                    errMsg = "用户取消支付";
//                } else if(TextUtils.equals(rs, "9000")) {
//                    errMsg = null;
//                }
//                if(payResult!=null) {
//                    if(errMsg!=null)
//                        payResult.error(errMsg);
//                    else
//                        payResult.success();
//                }
//            }
//        }).start();
//        PayTask alipay = new PayTask(a);
//        if(pg!=null) {
//            pg.dismiss();
//            pg=null;

    }

    IWXAPI msgApi = null;

    private void wechatpay(final String order, Activity a) {
        if(pg!=null) {
            pg.dismiss();
            pg=null;
        }
        JSONObject o = null;
        try {
            o = new JSONObject(order);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(o!=null &&
                o.has("package") &&
                o.has("appid") &&
                o.has("sign") &&
                o.has("partnerid") &&
                o.has("prepayid") &&
                o.has("timestamp") &&
                o.has("noncestr")) {
            PayReq request = new PayReq();
            try {
                if(msgApi==null) {
                    msgApi = WXAPIFactory.createWXAPI(a, null);
                    msgApi.registerApp(o.getString("appid"));
                }
                request.appId = o.getString("appid");
                request.partnerId = o.getString("partnerid");
                request.prepayId= o.getString("prepayid");
                request.packageValue = o.getString("package");
                request.nonceStr= o.getString("noncestr");
                request.timeStamp= o.getString("timestamp");
                request.sign= o.getString("sign");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            msgApi.sendReq(request);
        } else {
            if(payResult!=null)
                payResult.error("微信参数获取失败，支付失败");
        }
    }
}
