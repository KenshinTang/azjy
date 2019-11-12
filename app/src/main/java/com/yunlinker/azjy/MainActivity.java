package com.yunlinker.azjy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.yunlinker.baseclass.BaseActivity;
import com.yunlinker.manager.ActivityManager;
import com.yunlinker.manager.HttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.yunlinker.config.WebConfig.checkUpdateUrl;
import static com.yunlinker.config.WebConfig.downloadUrl;
import static com.yunlinker.myApp.checkUpdate;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //创建时加入栈管理中
        ActivityManager.getInstance().add(MainActivity.this);

        if(!checkUpdate) {
//            checkUpdate();
            checkUpdate = true;
        }
        XXPermissions.with(this)
                //.constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES) //支持请求6.0悬浮窗权限8.0请求安装权限
                .permission(Permission.Group.LOCATION) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                           // Toast.makeText(MapViewActivity.this, "获取权限成功", Toast.LENGTH_SHORT).show();
                        }else {
                           // Toast.makeText(MapViewActivity.this, "获取权限成功，部分权限未正常授予", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if(quick) {
                           // Toast.makeText(MapViewActivity.this, "被永久拒绝授权，请手动授予权限", Toast.LENGTH_SHORT).show();
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(MainActivity.this);
                        }else {
                           // Toast.makeText(MapViewActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Add code to print out the key hash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.yunlinker.azjy",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//        } catch (NoSuchAlgorithmException e) {
//        }

    }




    //检查更新
    private void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = HttpManager.GET(checkUpdateUrl);
                try {
                    JSONObject obj = new JSONObject(res);
                    if(obj.has("code") && obj.getInt("code")==1) {
                        JSONArray data = obj.getJSONArray("data");
                        if(data.length()>0) {
                            JSONObject o = data.getJSONObject(0);
                            PackageManager pm = getApplicationContext().getPackageManager();
                            try {
                                PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
                                if(o.getInt("sysvalue") > pi.versionCode) {
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage("发现新版本，是否更新？");
                                            builder.setCancelable(false);
                                            builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    final Uri uri = Uri.parse(downloadUrl);
                                                    final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                                    MainActivity.this.startActivity(it);
                                                    dialog.cancel();
                                                }
                                            });
                                            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                    });
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
