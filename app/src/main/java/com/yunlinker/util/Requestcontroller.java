package com.yunlinker.util;

/**
 * 单例模式网络请求控制器
 */

public class Requestcontroller {

    private double Uplongitude;
    private double Uplatitude;
    private double downlongitude;
    private double downlatitude;
    private String time;
    private String price;
    private int category1;
    private int category2;
    private int ageLimit;
    private int sex;

    private boolean Whetherequest;
    private Controlunit controlunit;


    public Requestcontroller(Controlunit controlunit){
        this.controlunit=controlunit;

    }
    public void tellFinish(){
        this.Whetherequest=false;
    }

    /**
     * 得到数据
     * @param time
     * @param price
     * @param Uplongitude
     * @param Uplatitude
     * @param downlongitude
     * @param downlatitude
     * @param category2
     * @param ageLimit
     * @param sex
     */
    public void get(Double Uplongitude,Double Uplatitude,Double downlongitude,
                    Double downlatitude,String time,String price,int category1,int category2,int ageLimit,int sex){
        this.Uplongitude = Uplongitude;
        this.Uplatitude = Uplatitude;
        this.downlatitude = downlatitude;
        this.downlongitude = downlongitude;
        this.time = time;
        this.price = price;
        this.category1 = category1;
        this.category2 = category2;
        this.ageLimit = ageLimit;
        this.sex = sex;
        if (!Whetherequest){
            loading();
        }
    }

    public void loading() {
        if (!this.Whetherequest) {
            this.Whetherequest = true;
            this.controlunit.connect(this.Uplongitude,this.Uplatitude,this.downlongitude, this.downlatitude,this.time,this.price,this.category1,this.category2,this.ageLimit,this.sex);
        }

    }
}
