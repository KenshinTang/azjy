package com.yunlinker.azjy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,View.OnClickListener {

    @BindView(R.id.map_back)
    ImageView mapBack;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.play_daohang)
    ImageView playDaohang;
    private GoogleMap mMap;
    private String lat, lng, name;
    private PopupWindow window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initview();
    }

    private void initview() {
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");
        name = intent.getStringExtra("addr");
        //地址名称
        address.setText(name);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double mlat = Double.parseDouble(lat);
        double mlng = Double.parseDouble(lng);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(mlat,mlng);
        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.wu)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngzoom(sydney),15);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mlat,mlng), 15));// 定位当前经纬度，level 10显示
    }


    @OnClick({R.id.map_back, R.id.play_daohang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map_back:
                finish();
                break;
            case R.id.play_daohang:
                dhPopupView();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void dhPopupView() {
        View popupView = getLayoutInflater().inflate(R.layout.map_navgation_sheet, null);
        TextView baidu_btn = (TextView) popupView.findViewById(R.id.baidu_btn);
        TextView gaode_btn = (TextView)popupView.findViewById(R.id.gaode_btn);
        TextView tencent_btn = (TextView)popupView.findViewById(R.id.tencent_btn);
        TextView cancel_btn2 = (TextView)popupView.findViewById(R.id.cancel_btn2);

        baidu_btn.setOnClickListener(this);
        gaode_btn.setOnClickListener(this);
        tencent_btn.setOnClickListener(this);
        cancel_btn2.setOnClickListener(this);
        // : 2016/5/17 创建PopupWindow对象，指定宽度和高度
//        window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window = new PopupWindow();

        window.setContentView(popupView);
        //设置宽高
        window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // : 2016/5/17 设置动画
//        window.setAnimationStyle(R.style.popup_window_anim);
        // : 2016/5/17 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#88323232")));
        // : 2016/5/17 设置可以获取焦点
        window.setFocusable(true);
        // : 2016/5/17 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // ：更新popupwindow的状态
        window.update();
        window.setClippingEnabled(false);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        window.showAtLocation(popupView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, winHeight - rect.bottom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.baidu_btn:
                startNaviGoogle();
                break;
            case R.id.gaode_btn:
                gaodeMap();
                break;
            case R.id.cancel_btn2:
                if (window != null) {
                    window.dismiss();
                }
                break;
        }
    }


    //谷歌地图,起点就是定位点
    // 终点是LatLng ll = new LatLng("你的latitude","你的longitude");
    public void startNaviGoogle() {
        if (isAvilible(MapsActivity.this, "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else {
            Toast.makeText(MapsActivity.this, "您尚未安装谷歌地图或地图版本过低", Toast.LENGTH_SHORT).show();
        }
    }

    //验证各种导航地图是否安装
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    //高德地图,起点就是定位点  startNaviGao
    // 终点是LatLng ll = new LatLng("你的纬度latitude","你的经度longitude");
    public void gaodeMap() {
        if (isAvilible(MapsActivity.this, "com.autonavi.minimap")) {
            try{
                Intent  intent = Intent.getIntent("androidamap://navi?sourceApplication="+name+"&poiname=我的目的地&lat="+lat+"&lon="+lng+"&dev=0");
                startActivity(intent);
            } catch (URISyntaxException e)
            {e.printStackTrace(); }
        }else{
            Toast.makeText(MapsActivity.this, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        window.dismiss();
    }

}
