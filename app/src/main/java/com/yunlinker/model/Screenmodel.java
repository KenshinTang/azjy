package com.yunlinker.model;

import java.util.List;

/**
 * Created by Administrator on 2018/11/21/021.
 */

public class Screenmodel {

    /**
     * code : 1
     * curPage : 0
     * data : [{"dictcode":"articleType","dictid":21,"dictname":"文章类型","dictpid":0,"dicttype":"sys","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"articleCategory","dictid":46,"dictname":"文章类别","dictpid":0,"dicttype":"sys","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"genderLimit","dictid":1,"dictname":"genderLimit","dictpid":0,"dicttype":"sys","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"ageLimit","dictid":5,"dictname":"ageLimit","dictpid":0,"dicttype":"sys","enable":1,"orderindex":3,"remark":"null","subdate":-1},{"dictcode":"state","dictid":9,"dictname":"state","dictpid":0,"dicttype":"sys","enable":1,"orderindex":4,"remark":"null","subdate":-1},{"dictcode":"busHistory","dictid":28,"dictname":"busHistory","dictpid":0,"dicttype":"sys","enable":1,"orderindex":6,"remark":"null","subdate":-1},{"dictcode":"postType","dictid":32,"dictname":"postType","dictpid":0,"dicttype":"sys","enable":1,"orderindex":7,"remark":"null","subdate":-1},{"dictcode":"course","dictid":36,"dictname":"course","dictpid":0,"dicttype":"sys","enable":1,"orderindex":8,"remark":"null","subdate":-1},{"dictcode":"role","dictid":39,"dictname":"role","dictpid":0,"dicttype":"sys","enable":1,"orderindex":9,"remark":"null","subdate":-1},{"dictcode":"Interest","dictid":42,"dictname":"Interest","dictpid":0,"dicttype":"sys","enable":1,"orderindex":10,"remark":"null","subdate":-1},{"dictcode":"adpostion","dictid":48,"dictname":"广告位置","dictpid":0,"dicttype":"sys","enable":1,"orderindex":11,"remark":"null","subdate":-1},{"dictcode":"course_price","dictid":51,"dictname":"course_price","dictpid":0,"dicttype":"sys","enable":1,"orderindex":12,"remark":"null","subdate":-1},{"dictcode":"btype","dictid":20,"dictname":"机构类别","dictpid":0,"dicttype":"sys","enable":1,"orderindex":20,"remark":"","subdate":1.540805891137E12},{"dictcode":"noLimit","dictid":4,"dictname":"noLimit","dictpid":1,"dicttype":"genderLimit","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"boy","dictid":2,"dictname":"boy","dictpid":1,"dicttype":"genderLimit","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"girl","dictid":3,"dictname":"girl","dictpid":1,"dicttype":"genderLimit","enable":1,"orderindex":3,"remark":"null","subdate":-1},{"dictcode":"five-ten","dictid":6,"dictname":"5-10","dictpid":5,"dicttype":"ageLimit","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"eleven-fifteen","dictid":7,"dictname":"11-15","dictpid":5,"dicttype":"ageLimit","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"sixteen-twenty","dictid":8,"dictname":"16-20","dictpid":5,"dicttype":"ageLimit","enable":1,"orderindex":3,"remark":"null","subdate":-1},{"dictcode":"New South Wales","dictid":10,"dictname":"New South Wales","dictpid":9,"dicttype":"state","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"Queensland","dictid":11,"dictname":"Queensland","dictpid":9,"dicttype":"state","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"South Australia","dictid":12,"dictname":"South Australia","dictpid":9,"dicttype":"state","enable":1,"orderindex":3,"remark":"null","subdate":-1},{"dictcode":"Victoria","dictid":13,"dictname":"Victoria","dictpid":9,"dicttype":"state","enable":1,"orderindex":4,"remark":"null","subdate":-1},{"dictcode":"Western Australia","dictid":14,"dictname":"Western Australia","dictpid":9,"dicttype":"state","enable":1,"orderindex":5,"remark":"null","subdate":-1},{"dictcode":"Tasmania","dictid":15,"dictname":"Tasmania","dictpid":9,"dicttype":"state","enable":1,"orderindex":6,"remark":"null","subdate":-1},{"dictcode":"Northern Territory","dictid":16,"dictname":"Northern Territory","dictpid":9,"dicttype":"state","enable":1,"orderindex":7,"remark":"null","subdate":-1},{"dictcode":"Australian National Territory - ACT","dictid":17,"dictname":"Australian National Territory - ACT","dictpid":9,"dicttype":"state","enable":1,"orderindex":8,"remark":"null","subdate":-1},{"dictcode":"article_aa","dictid":22,"dictname":"aa","dictpid":21,"dicttype":"articleType","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"article_bb","dictid":23,"dictname":"bb","dictpid":21,"dicttype":"articleType","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"article_consul","dictid":45,"dictname":"咨询快报","dictpid":21,"dicttype":"articleType","enable":1,"orderindex":3,"remark":"null","subdate":-1},{"dictcode":"mathematics","dictid":25,"dictname":"mathematics","dictpid":24,"dicttype":"culturalCourse","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"music","dictid":26,"dictname":"music","dictpid":24,"dicttype":"culturalCourse","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"english","dictid":27,"dictname":"english","dictpid":24,"dicttype":"culturalCourse","enable":1,"orderindex":3,"remark":"null","subdate":-1},{"dictcode":"following year","dictid":30,"dictname":"following year","dictpid":28,"dicttype":"busHistory","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"one-three years","dictid":29,"dictname":"one-three years","dictpid":28,"dicttype":"busHistory","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"three-five years","dictid":31,"dictname":"three-five years","dictpid":28,"dicttype":"busHistory","enable":1,"orderindex":3,"remark":"null","subdate":-1},{"dictcode":"primary","dictid":33,"dictname":"primary","dictpid":32,"dicttype":"postType","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"middle","dictid":34,"dictname":"middle","dictpid":32,"dicttype":"postType","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"senior","dictid":35,"dictname":"senior","dictpid":32,"dicttype":"postType","enable":1,"orderindex":3,"remark":"null","subdate":-1},{"dictcode":"culturalCourse","dictid":24,"dictname":"culturalCourse","dictpid":36,"dicttype":"course","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"outerCourse","dictid":37,"dictname":"outerCourse","dictpid":36,"dicttype":"course","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"Survival adventure","dictid":38,"dictname":"Survival adventure","dictpid":37,"dicttype":"outerCourse","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"superAdmin","dictid":40,"dictname":"superAdmin","dictpid":39,"dicttype":"role","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"teacher","dictid":41,"dictname":"teacher","dictpid":39,"dicttype":"role","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"Run","dictid":43,"dictname":"Run","dictpid":42,"dicttype":"Interest","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"Swimming","dictid":44,"dictname":"Swimming","dictpid":42,"dicttype":"Interest","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"articleCategory_aa","dictid":47,"dictname":"科幻","dictpid":46,"dicttype":"articleCategory","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"adpostion_home","dictid":49,"dictname":"首页","dictpid":48,"dicttype":"adpostion","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"course_price_1","dictid":52,"dictname":"0-100","dictpid":51,"dicttype":"course_price","enable":1,"orderindex":1,"remark":"null","subdate":-1},{"dictcode":"course_price_2","dictid":53,"dictname":"100-200","dictpid":51,"dicttype":"course_price","enable":1,"orderindex":2,"remark":"null","subdate":-1},{"dictcode":"course_price_3","dictid":54,"dictname":"200-300","dictpid":51,"dicttype":"course_price","enable":1,"orderindex":3,"remark":"null","subdate":-1},{"dictcode":"course_price_4","dictid":55,"dictname":"300-400","dictpid":51,"dicttype":"course_price","enable":1,"orderindex":4,"remark":"null","subdate":-1},{"dictcode":"course_price_5","dictid":56,"dictname":"400-0","dictpid":51,"dicttype":"course_price","enable":1,"orderindex":5,"remark":"null","subdate":-1}]
     * msg :
     * pageSize : 0
     * totalCount : 0
     */

    private int code;
    private int curPage;
    private String msg;
    private int pageSize;
    private int totalCount;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * dictcode : articleType
         * dictid : 21
         * dictname : 文章类型
         * dictpid : 0
         * dicttype : sys
         * enable : 1
         * orderindex : 1
         * remark : null
         * subdate : -1.0
         */

        private String dictcode;
        private int dictid;
        private String dictname;
        private int dictpid;
        private String dicttype;
        private int enable;
        private int orderindex;
        private String remark;
        private double subdate;

        public String getDictcode() {
            return dictcode;
        }

        public void setDictcode(String dictcode) {
            this.dictcode = dictcode;
        }

        public int getDictid() {
            return dictid;
        }

        public void setDictid(int dictid) {
            this.dictid = dictid;
        }

        public String getDictname() {
            return dictname;
        }

        public void setDictname(String dictname) {
            this.dictname = dictname;
        }

        public int getDictpid() {
            return dictpid;
        }

        public void setDictpid(int dictpid) {
            this.dictpid = dictpid;
        }

        public String getDicttype() {
            return dicttype;
        }

        public void setDicttype(String dicttype) {
            this.dicttype = dicttype;
        }

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }

        public int getOrderindex() {
            return orderindex;
        }

        public void setOrderindex(int orderindex) {
            this.orderindex = orderindex;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public double getSubdate() {
            return subdate;
        }

        public void setSubdate(double subdate) {
            this.subdate = subdate;
        }
    }
}
