package com.yunlinker.config;

import static com.yunlinker.config.Api.API;

/**
 * Created by lemon on 2017/8/21.
 */

public class WebConfig {
    public static String saveKey = "MWebApp";
    public static String AssestRoot = "file:///android_asset/";

    //接口
    public static String  SCREEN=API+"api/dict/list?";
    public static String  MAPFINDING=API+"api/course/mapfinding?";

    public static final String WXID = "wx3fa1e0ee54c28e63";
    public static final String WXSECRET = "bc4f50ebd8b631473abd3363732531c6";

    public static final String QQID ="1107591572";
    public static final String QQSECRET ="97Ez3gw2CsLGh216";

    public static final String QYSECRET = "e69e0593f2546c70496d67eddc041dd4";

    public static final String UMKEY = "5af3bec8b27b0a038600008d";

    public static final String TwitterID = "2QCb1sVy6qXUJEVrslzVNfKzX";
    public static final String TwitterSECRET = "2VAMzLpBzNE34mZ2RVq40laKTOBCl1LQNjWTxhXreUuIQvrE2M";


    public static final String bucket = "ali-sgoss";
    public static final String endpoint = " http://oss-ap-southeast-1.aliyuncs.com/";

    public static final int SELECTED_IMAGE_CODE = 2000;
    public static final int QRCODE_GET_CODE = 2001;
    public static final int UMSHARE_CALLBACK_CODE = 10103;
    public static final int GPS_REQUEST_CODE = 191091;


    public static final String downloadUrl = "http://www.baidu.com";
    public static final String checkUpdateUrl = "http://39.108.173.126/housekeep/api/sysconfig/get?keys=ANDROID_VERSION";

}
