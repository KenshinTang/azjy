package com.yunlinker.azjy;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.kongzue.stacklabelview.StackLabel;
import com.kongzue.stacklabelview.interfaces.OnLabelClickListener;
import com.yunlinker.adapter.AgeAdapder;
import com.yunlinker.adapter.GenderAdapder;
import com.yunlinker.adapter.RecruitAdapderone;
import com.yunlinker.adapter.RecruitAdapdertwo;
import com.yunlinker.adapter.ScreenAdapter;
import com.yunlinker.model.Mapfindmodel;
import com.yunlinker.model.Mapfindmodel2;
import com.yunlinker.model.Screenmodel;
import com.yunlinker.util.Controlunit;
import com.yunlinker.util.PermissionUtils;
import com.yunlinker.util.Requestcontroller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.yunlinker.config.WebConfig.MAPFINDING;
import static com.yunlinker.config.WebConfig.SCREEN;


public class MapViewActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener, Controlunit,
        View.OnClickListener {


    ImageView imgBack;
    TextView textView2;
    ImageView imgSearch;
    ImageView imgLb;
    RelativeLayout relativeLayout;
    TextView time;
    ImageView imgTime;
    LinearLayout textTime;
    TextView price;
    ImageView imgPrice;
    LinearLayout textPrice;
    TextView age;
    ImageView imgAge;
    LinearLayout textAge;
    TextView gender;
    ImageView imgGender;
    LinearLayout textGender;
    TextView recruitStatus;
    ImageView imgRecruitStatus;
    LinearLayout textRecruitStatus;
    ListView genderList;
    ListView priceList;
    ListView ageList;
    ListView recruitStatusListOne;
    ListView recruitStatusListTwo;
    LinearLayout recruitStatusLinear;
    StackLabel stackLabelView;
    HorizontalScrollView horizontalScrollView;
    TextView textReset;
    TextView textDone;
    RelativeLayout recruitStatusRelative;

    private TimePickerView pvTime;
    private Calendar selectedDate, endDate, startDate;
    private GoogleMap gmap;
    private String TAG = "MapViewActivity";
    private Screenmodel screenmodel;
    private int recruitStatusid, recruitStatustwoid;
    private RecruitAdapderone recruitAdapderone;
    private RecruitAdapdertwo recruitAdapdertwo;
    private Requestcontroller requestcontrol = new Requestcontroller(this);
    private Mapfindmodel2 mapmodel;

    private Mapfindmodel mapmodel2;
    //获取数据需要的id
    private String[] AllIds = {"Time", "price", "age", "gender", "recruitStatusd", "recruitStatusd2"};
    //放到list页面上的list
    private List<String> labels = new ArrayList<>();
    //用于筛选数据的list
    private Map<String, String> addHash = new HashMap<>();
    //参数接收
    private Map<String, Object> param_map = new HashMap<>();
    private Double Uplongitude, Uplatitude, downlongitude, downlatitude;
    private String recruitStatusd,recruitStatusd2;
    /**
     * 位置许可请求请求代码.
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        imgBack = findViewById(R.id.img_back);
        textView2 = findViewById(R.id.textView2);
        imgSearch = findViewById(R.id.img_search);
        imgLb = findViewById(R.id.img_lb);
        relativeLayout = findViewById(R.id.relativeLayout);
        time = findViewById(R.id.time);
        imgTime = findViewById(R.id.img_time);
        textTime = findViewById(R.id.text_time);
        price = findViewById(R.id.price);
        imgPrice = findViewById(R.id.img_price);
        textPrice = findViewById(R.id.text_price);
        age = findViewById(R.id.age);
        imgAge = findViewById(R.id.img_age);
        textAge = findViewById(R.id.text_age);
        gender = findViewById(R.id.gender);
        imgGender = findViewById(R.id.img_gender);
        textGender = findViewById(R.id.text_gender);
        recruitStatus = findViewById(R.id.recruit_status);
        imgRecruitStatus = findViewById(R.id.img_recruit_status);
        textRecruitStatus = findViewById(R.id.text_recruit_status);
        genderList = findViewById(R.id.gender_list);
        priceList = findViewById(R.id.price_list);
        ageList = findViewById(R.id.age_list);
        recruitStatusListOne = findViewById(R.id.recruit_status_list_one);
        recruitStatusListTwo = findViewById(R.id.recruit_status_list_two);
        recruitStatusLinear = findViewById(R.id.recruit_status_linear);
        stackLabelView = findViewById(R.id.stackLabelView);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        textReset = findViewById(R.id.text_reset);
        textDone = findViewById(R.id.text_done);
        recruitStatusRelative = findViewById(R.id.recruit_status_relative);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        selectedDate = Calendar.getInstance();
        startDate = Calendar.getInstance();
        startDate.set(1998, 1, 0);//设置起始年份
        endDate = Calendar.getInstance();
        endDate.set(2100, 1, 0);//设置结束年份
        initscreen();


        // 利用手机LocationManager,获取手机经纬度,然后谷歌定位到该经纬度
        LocationManager mLocationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            /**参1:选择定位的方式
             * 参2:定位的间隔时间
             * 参3:当位置改变多少时进行重新定位
             * 参4:位置的回调监听*/
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5*1000, 10,mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5*1000, 10,mLocationListener);
        }

    }

    /**
     * 定位
     */
    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,15));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * 筛选请求
     */

    private void initscreen() {
        OkHttpClient okHttp = new OkHttpClient();
        final Request request1 = new Request.Builder()
                .get()
                .url(SCREEN)
                .build();
        Call calle = okHttp.newCall(request1);
        calle.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        String screen = response.body().string();
                        Gson gson = new Gson();
                        screenmodel = gson.fromJson(screen, Screenmodel.class);
                        if (screenmodel.getData() != null) {
                            final Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    addscreen(screenmodel);
                                }
                            };
                            new Thread() {
                                public void run() {
                                    new Handler(Looper.getMainLooper()).post(runnable);
                                }
                            }.start();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e);
                }

            }
        });

    }

    /**
     * 筛选数据
     * @param screen
     */

    private void addscreen(final Screenmodel screen) {
        //总数据
        final List<Screenmodel.DataBean> screenlist = new ArrayList<>();
        //价格数据
        final List<String> pricelist = new ArrayList<>();
        //年龄数据
        final List<String> agelist = new ArrayList<>();
        //性别数据
        final List<String> genderlist = new ArrayList<>();
        //招募状态数据
        final List<String> recruitstatuslist = new ArrayList<>();
        //招募状态数据2
        final List<String> recruitstatuslisttwo = new ArrayList<>();
        //价格dictid
        final List<Integer> pricelistid = new ArrayList<>();
        //年龄dictid
        final List<Integer> agelistid = new ArrayList<>();
        //性别dictid
        final List<Integer> genderlistid = new ArrayList<>();
        //招募状态dictid
        final List<Integer> recruitstatuslistid = new ArrayList<>();
        //招募状态id
        final List<Integer> recruitstatuslisttwoid = new ArrayList<>();

        for (int i = 0; i < screen.getData().size(); i++) {
            if (screen.getData().get(i).getDictpid() == 51) {
                pricelist.add(screen.getData().get(i).getDictname());
                pricelistid.add(screen.getData().get(i).getDictid());
            }
            if (screen.getData().get(i).getDictpid() == 5) {
                agelist.add(screen.getData().get(i).getDictname());
                agelistid.add(screen.getData().get(i).getDictid());
            }
            if (screen.getData().get(i).getDictpid() == 1) {
                genderlist.add(screen.getData().get(i).getDictname());
                genderlistid.add(screen.getData().get(i).getDictid());
            }
            if (screen.getData().get(i).getDictpid() == 36) {
                recruitstatuslist.add(screen.getData().get(i).getDictname());
                recruitstatuslistid.add(screen.getData().get(i).getDictid());
            }
            screenlist.add(screen.getData().get(i));

        }
        //价格适配器
        final ScreenAdapter adapter = new ScreenAdapter(this, pricelist);
        priceList.setAdapter(adapter);
        //年龄适配器
        final AgeAdapder ageAdapder = new AgeAdapder(this, agelist);
        ageList.setAdapter(ageAdapder);
        //性别适配器
        final GenderAdapder genderAdapder = new GenderAdapder(this, genderlist);
        genderList.setAdapter(genderAdapder);
        //招募状态适配器
        recruitAdapderone = new RecruitAdapderone(this, recruitstatuslist);
        recruitStatusListOne.setAdapter(recruitAdapderone);
        //价格列表点击事件
        priceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stackLabelView.setLabels(FilterData(pricelist.get(position), "price"));
                stackLabelView.setVisibility(View.VISIBLE);
                param_map.put("price", pricelist.get(position));
                getdataparam();
                priceList.setVisibility(View.GONE);


            }
        });
        //年龄列表点击事件
        ageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stackLabelView.setLabels(FilterData(agelist.get(position) + " years old", "age"));
                stackLabelView.setVisibility(View.VISIBLE);
                ageList.setVisibility(View.GONE);
                param_map.put("age", agelistid.get(position));
                getdataparam();
            }
        });
        //性别列表点击事件
        genderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stackLabelView.setLabels(FilterData(genderlist.get(position), "gender"));
                stackLabelView.setVisibility(View.VISIBLE);
                genderList.setVisibility(View.GONE);
                param_map.put("gender", genderlistid.get(position));
                getdataparam();
            }
        });
        //招募状态列表点击事件
        recruitStatusListOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recruitstatuslisttwo.clear();
                for (int x = 0; x < recruitstatuslistid.size(); x++) {
                    recruitStatusid = recruitstatuslistid.get(position);
                }
                for (int y = 0; y < screenlist.size(); y++) {
                    if (screenlist.get(y).getDictpid() == recruitStatusid) {
                        recruitstatuslisttwo.add(screenlist.get(y).getDictname());
                        recruitstatuslisttwoid.add(screenlist.get(y).getDictid());
                    }
                }
                 recruitStatusd = recruitstatuslist.get(position);

                recruitAdapderone.setSelectItem(position);
                recruitAdapderone.notifyDataSetChanged();

                recruitAdapdertwo = new RecruitAdapdertwo(MapViewActivity.this, recruitstatuslisttwo);
                recruitStatusListTwo.setAdapter(recruitAdapdertwo);
                recruitStatusListTwo.setVisibility(View.VISIBLE);

            }
        });
        //招募状态列表2点击事件
        recruitStatusListTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                recruitStatusd2 = recruitstatuslisttwo.get(position);
                recruitStatustwoid =  recruitstatuslisttwoid.get(position);
                recruitAdapdertwo.setSelectItem(position);
                recruitAdapdertwo.notifyDataSetChanged();
            }
        });
        /**
         *筛选item
         */
        stackLabelView.setOnLabelClickListener(new OnLabelClickListener() {
            @Override
            public void onClick(int index, View v, String s) {

                //循环HashMap
                for (int i = 0; i < AllIds.length; i++) {
                    if (addHash.containsKey(AllIds[i])) {
                        //找到你删除的那个值然后从hashMap中删除
                        if (addHash.get(AllIds[i]).equals(labels.get(index))) {
                            addHash.remove(AllIds[i]);
                            param_map.remove(AllIds[i]);
                            getdataparam();
                        }
                    }
                }
                //删除并重新设置标签
                labels.remove(index);
                stackLabelView.setLabels(labels);
                if (labels.size() == 0) {
                    stackLabelView.setVisibility(View.GONE);
                } else {
                   // Toast.makeText(MapViewActivity.this, "点击了：" + s, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 根据筛选的值请求
     */

    private void getdataparam() {
        //左上角经纬度
        Point pt = new Point();
        pt.x = 0;
        pt.y = 0;
        LatLng upLeft = gmap.getProjection().fromScreenLocation(pt);
        //获取到左上角的经纬度

        Uplongitude = upLeft.longitude;
        Uplatitude = upLeft.latitude;
        Log.e("移动没有，移动没有", "longitude=" + Uplongitude + "latitude" + Uplatitude);
        //右下角经纬度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Point pty = new Point();
        pty.x = dm.widthPixels;
        pty.y = dm.heightPixels;
        LatLng downRight = gmap.getProjection().fromScreenLocation(pty);


        //获取到右下角的经纬度
        downlongitude = downRight.longitude;
        downlatitude = downRight.latitude;
        Log.e("移动没有，移动没有", "longitude=" + downlongitude + "latitude" + downlatitude);
        //地图当前缩放等级

        requestcontrol.get(Uplongitude, Uplatitude, downlongitude, downlatitude,
                param_map.get("Time") == null ? "" : param_map.get("Time").toString(),
                param_map.get("price") == null ? "" : param_map.get("price").toString(),
                param_map.get("recruitStatusd") == null ? 0 : Integer.parseInt(param_map.get("recruitStatusd").toString()),
                param_map.get("recruitStatusd2") == null ? 0 : Integer.parseInt(param_map.get("recruitStatusd2").toString()),
                param_map.get("age") == null ? 0 : Integer.parseInt(param_map.get("age").toString()),
                param_map.get("gender") == null ? 0 : Integer.parseInt(param_map.get("gender").toString()));
    }

    /**
     * 时间选择器
     */

    private void Timeview() {
        pvTime = new TimePickerBuilder(MapViewActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
                String datetime = formatter.format(date);
                stackLabelView.setLabels(FilterData(datetime, "Time"));
                stackLabelView.setVisibility(View.VISIBLE);
                String timedate = datetime.replace("-", "");
                param_map.put("Time", timedate);
                getdataparam();
            }
        }).setType(new boolean[]{true, true, false, false, false, false})
                .setCancelText("Cancel")//取消按钮文字
                .setSubmitText("Confirm")//确认按钮文字
                .setContentTextSize(16)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("SELECT TIME")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(getResources().getColor(R.color.contents_text))//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.theme_color))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.colorPrimary))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.encode_view))//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("", "", "Day", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.img_back:
                finish();
                break;
            //搜索
            case R.id.img_search:
                Intent intent = new Intent(MapViewActivity.this, MainActivity.class);
                intent.putExtra("sendUrl", "search.html");
                startActivity(intent);
                break;
            //目录
            case R.id.img_lb:
                finish();
                break;
            //时间
            case R.id.text_time:
                time.setTextColor(Color.parseColor("#68AF47"));
                imgTime.setImageResource(R.drawable.xuanzhong);
                price.setTextColor(Color.parseColor("#333333"));
                imgPrice.setImageResource(R.drawable.moren);
                gender.setTextColor(Color.parseColor("#333333"));
                imgGender.setImageResource(R.drawable.moren);
                age.setTextColor(Color.parseColor("#333333"));
                imgAge.setImageResource(R.drawable.moren);
                recruitStatus.setTextColor(Color.parseColor("#333333"));
                imgRecruitStatus.setImageResource(R.drawable.moren);
                Timeview();
                genderList.setVisibility(View.GONE);
                priceList.setVisibility(View.GONE);
                ageList.setVisibility(View.GONE);
                recruitStatusRelative.setVisibility(View.GONE);
                break;
            //价格
            case R.id.text_price:
                time.setTextColor(Color.parseColor("#333333"));
                imgTime.setImageResource(R.drawable.moren);
                price.setTextColor(Color.parseColor("#68AF47"));
                imgPrice.setImageResource(R.drawable.xuanzhong);
                gender.setTextColor(Color.parseColor("#333333"));
                imgGender.setImageResource(R.drawable.moren);
                age.setTextColor(Color.parseColor("#333333"));
                imgAge.setImageResource(R.drawable.moren);
                recruitStatus.setTextColor(Color.parseColor("#333333"));
                imgRecruitStatus.setImageResource(R.drawable.moren);
                genderList.setVisibility(View.GONE);
                priceList.setVisibility(View.VISIBLE);
                ageList.setVisibility(View.GONE);
                recruitStatusRelative.setVisibility(View.GONE);
                break;
            //年龄
            case R.id.text_age:
                time.setTextColor(Color.parseColor("#333333"));
                imgTime.setImageResource(R.drawable.moren);
                price.setTextColor(Color.parseColor("#333333"));
                imgPrice.setImageResource(R.drawable.moren);
                gender.setTextColor(Color.parseColor("#333333"));
                imgGender.setImageResource(R.drawable.moren);
                age.setTextColor(Color.parseColor("#68AF47"));
                imgAge.setImageResource(R.drawable.xuanzhong);
                recruitStatus.setTextColor(Color.parseColor("#333333"));
                imgRecruitStatus.setImageResource(R.drawable.moren);
                genderList.setVisibility(View.GONE);
                priceList.setVisibility(View.GONE);
                ageList.setVisibility(View.VISIBLE);
                recruitStatusRelative.setVisibility(View.GONE);
                break;
            //性别
            case R.id.text_gender:
                time.setTextColor(Color.parseColor("#333333"));
                imgTime.setImageResource(R.drawable.moren);
                price.setTextColor(Color.parseColor("#333333"));
                imgPrice.setImageResource(R.drawable.moren);
                gender.setTextColor(Color.parseColor("#68AF47"));
                imgGender.setImageResource(R.drawable.xuanzhong);
                age.setTextColor(Color.parseColor("#333333"));
                imgAge.setImageResource(R.drawable.moren);
                recruitStatus.setTextColor(Color.parseColor("#333333"));
                imgRecruitStatus.setImageResource(R.drawable.moren);
                genderList.setVisibility(View.VISIBLE);
                priceList.setVisibility(View.GONE);
                ageList.setVisibility(View.GONE);
                recruitStatusRelative.setVisibility(View.GONE);
                break;
            //招募状态
            case R.id.text_recruit_status:
                time.setTextColor(Color.parseColor("#333333"));
                imgTime.setImageResource(R.drawable.moren);
                price.setTextColor(Color.parseColor("#333333"));
                imgPrice.setImageResource(R.drawable.moren);
                gender.setTextColor(Color.parseColor("#333333"));
                imgGender.setImageResource(R.drawable.moren);
                age.setTextColor(Color.parseColor("#333333"));
                imgAge.setImageResource(R.drawable.moren);
                recruitStatus.setTextColor(Color.parseColor("#68AF47"));
                imgRecruitStatus.setImageResource(R.drawable.xuanzhong);
                genderList.setVisibility(View.GONE);
                priceList.setVisibility(View.GONE);
                ageList.setVisibility(View.GONE);
                recruitStatusRelative.setVisibility(View.VISIBLE);
                param_map.remove("recruitStatusd2");
                recruitStatusd2=null;
                addHash.remove("recruitStatusd2");
                break;
            //重置
            case R.id.text_reset:
                recruitAdapderone.setSelectItem(-1);
                recruitAdapderone.notifyDataSetChanged();
                recruitAdapdertwo.setSelectItem(-1);
                recruitAdapdertwo.notifyDataSetChanged();
                break;
            //确定
            case R.id.text_done:
                if (recruitStatusd!=null){
                    stackLabelView.setLabels(FilterData(recruitStatusd,"recruitStatusd"));
                    stackLabelView.setVisibility(View.VISIBLE);
                }

                if (recruitStatusid!=0){
                    param_map.put("recruitStatusd", recruitStatusid);
                    getdataparam();
                }
                if (recruitStatusd2!=null){
                    stackLabelView.setLabels(FilterData(recruitStatusd2,"recruitStatusd2"));
                    stackLabelView.setVisibility(View.VISIBLE);
                }
                if (recruitStatustwoid!=0){
                    param_map.put("recruitStatusd2", recruitStatustwoid);
                    getdataparam();
                }
                if (recruitStatusd2==null){
                    addHash.remove("recruitStatusd2");
                    stackLabelView.setLabels(labels);
                }

                recruitStatusRelative.setVisibility(View.GONE);
                break;

        }
    }

    /**
     * 初始化地图
     * @param googleMap
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmap = googleMap;
        //地图显示模式
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //onPoiClick
        //gmap.setOnPoiClickListener(this);
        //设置禁止手势
        UiSettings settings = gmap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        settings.setCompassEnabled(false);
        settings.setMapToolbarEnabled(false);
        settings.setTiltGesturesEnabled(false);
        settings.setRotateGesturesEnabled(false);

        //定位图标
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 缺少访问该位置的权限。
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (gmap != null) {
            // 访问该位置已被授予应用程序。
            gmap.setMyLocationEnabled(true);
        }
        gmap.setOnMyLocationButtonClickListener(this);
        gmap.setOnMyLocationClickListener(this);

        getCoordinates();

        // 在地图上添加许多标记。

        gmap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.e(TAG, "onMyLocationClick: "+location.toString());

    }

    /**
     *  设置覆盖物
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : param_map.entrySet()) {
            sb.append("&");
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        Intent intent = new Intent(MapViewActivity.this, MainActivity.class);
        intent.putExtra("sendUrl", "institutionsCourse.html?businessId=" + marker.getSnippet()+sb.toString());
        startActivity(intent);
        return false;

    }

    /**
     * 地图手势交互
     */

    private void getCoordinates() {

        //设置地图开始移动方法
//                gmap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//                    @Override
//                    public void onCameraMoveStarted(int i) {
//
//                    }
//                });
        //设置地图位置发生改变方法
        gmap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                getdataparam();
            }
        });

        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e(TAG, "LatLng: " + latLng);
                priceList.setVisibility(View.GONE);
                ageList.setVisibility(View.GONE);
                genderList.setVisibility(View.GONE);
                recruitStatusRelative.setVisibility(View.GONE);
            }
        });

    }

    /**
     *
     * @param Uplongitude
     * @param Uplatitude
     * @param downlongitude
     * @param downlatitude
     * @param time
     * @param price
     * @param category1
     * @param category2
     * @param ageLimit
     * @param sex
     * 覆盖物的请求方法
     */

    @Override
    public void connect(Double Uplongitude, Double Uplatitude, Double downlongitude, Double downlatitude, String time, String price,int category1, int category2, int ageLimit, int sex) {
        String categorytwo = String.valueOf(category2);
        String agelimit = String.valueOf(ageLimit);
        String sexter = String.valueOf(sex);
        String categoryone = String.valueOf(category1);
        FormBody.Builder params = new FormBody.Builder();

        if (Uplongitude != null && Uplatitude != null) {
            params.add("minLatLng", Uplongitude.toString() + "," + Uplatitude.toString());
        }
        if (downlongitude != null && downlatitude != null) {
            params.add("maxLatLng", downlongitude.toString() + "," + downlatitude.toString());
        }
        if (!time.equals("")) {
            params.add("time", time.toString());
        }
        if (!price.equals("")) {
            params.add("price", price.toString());
        }
        if (!categoryone.equals("0")){
            params.add("category1", categoryone.toString());
        }
        if (!categorytwo.equals("0")) {
            params.add("category2", categorytwo.toString());
        }
        if (!agelimit.equals("0")) {
            params.add("ageLimit", agelimit.toString());
        }
        if (!sexter.equals("0")) {
            params.add("sex", sexter.toString());
        }
        FormBody form = params.build();
        final StringBuilder sb = new StringBuilder(MAPFINDING);
        for (int i = 0; i < form.size(); i++) {
            sb.append(form.name(i)).append("=").append(form.value(i)).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        Log.e("11111111111111111", "connect: "+sb.toString());
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(sb.toString())
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ShowToast("请检查您的网络");
                Log.e(TAG, "onFailure: "+e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        mapmodel = gson.fromJson(responseData, Mapfindmodel2.class);

                        if (mapmodel.getData() == null) {
                            mapviewclear();
                        }
                        if (mapmodel.getData() != null) {
                            final Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    addOverlays(mapmodel);
                                }
                            };
                            new Thread() {
                                public void run() {
                                    new Handler(Looper.getMainLooper()).post(runnable);
                                }
                            }.start();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e);
                }

            }
        });
        requestcontrol.tellFinish();
    }


    private void mapviewclear() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                gmap.clear();
            }
        };
        new Thread() {
            public void run() {
                new Handler(Looper.getMainLooper()).post(runnable);
            }
        }.start();

    }

    /**
     * 线程更新Toast
     */
    private void ShowToast(final String msg) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MapViewActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        };
        new Thread() {
            public void run() {
                new Handler(Looper.getMainLooper()).post(runnable);
            }
        }.start();
    }

    /**
     * 添加覆盖物
     *
     * @param mapmodel
     */
    private void addOverlays(Mapfindmodel2 mapmodel) {
        gmap.clear();
        LatLng positon[] = new LatLng[mapmodel.getData().size()];
        for (int i = 0; i < mapmodel.getData().size(); i++) {
            if (mapmodel.getData().get(i).getAddrLat() != null && mapmodel.getData().get(i).getAddrLnt() != null
                    && mapmodel.getData().get(i).getBusinessId() != null && mapmodel.getData().get(i).getClassescount() != null) {
                String lat = mapmodel.getData().get(i).getAddrLat();
                String lng = mapmodel.getData().get(i).getAddrLnt();
                String classescount = mapmodel.getData().get(i).getClassescount();
                String businessName = mapmodel.getData().get(i).getBusinessName();
//                String s = businessName.toUpperCase();
//                String [] strArr = s.split("\\s");
//
//                String address = s.substring(0, 4);
                double oplat = Double.parseDouble(lat);
                double oplnt = Double.parseDouble(lng);
                positon[i] = new LatLng(oplat, oplnt);
                View view = View.inflate(MapViewActivity.this, R.layout.layout_map_scenic_maker, null);
                // 填充数据
                TextView nameView = (TextView) view.findViewById(R.id.maker_name);
                nameView.setText(businessName);
                Bitmap bitmap1 = getViewBitmap(view);
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(bitmap1);

                gmap.addMarker(new MarkerOptions().position(positon[i]).icon(bitmap).snippet(mapmodel.getData().get(i).getBusinessId()));
            }

        }

    }

    /**
     * 将View转换成Bitmap
     *
     * @param addViewContent
     * @return
     */

    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    /**
     * 筛选 筛选列表的数据
     */

    private List<String> FilterData(String value, String theId) {

        //如果没有这个id
        if (!addHash.containsKey(theId)) {
            addHash.put(theId, value);
        } else {
            //有这个id先移除再添加
            addHash.remove(theId);
            addHash.put(theId, value);
        }
        labels.clear();
        //取出所有想对应id的值
        for (int i = 0; i < AllIds.length; i++) {
            if (addHash.containsKey(AllIds[i])) {
                labels.add(addHash.get(AllIds[i]));
            }
        }

        //Log.e(TAG, addHash.size() + "");
        return labels;
    }

}

