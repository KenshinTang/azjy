package com.yunlinker.model;

import java.util.List;

/**
 * Created by Administrator on 2018/11/26/026.
 */

public class Mapfindmodel {

    /**
     * code : 1
     * curPage : 0
     * data : [{"addrLnt":"null","classescount":"3","businessId":"1","addrLat":"null"},{"addrLnt":"101.22","classescount":"1","businessId":"2","addrLat":"22.13"},{"addrLnt":"103.22","classescount":"7","businessId":"4","addrLat":"25.54"},{"addrLnt":"0.0","classescount":"2","businessId":"8","addrLat":"0.0"},{"addrLnt":"0.0","classescount":"3","businessId":"11","addrLat":"0.0"}]
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
         * addrLnt : null
         * classescount : 3
         * businessId : 1
         * addrLat : null
         */

        private String addrLnt;
        private String classescount;
        private String businessId;
        private String addrLat;

        public String getAddrLnt() {
            return addrLnt;
        }

        public void setAddrLnt(String addrLnt) {
            this.addrLnt = addrLnt;
        }

        public String getClassescount() {
            return classescount;
        }

        public void setClassescount(String classescount) {
            this.classescount = classescount;
        }

        public String getBusinessId() {
            return businessId;
        }

        public void setBusinessId(String businessId) {
            this.businessId = businessId;
        }

        public String getAddrLat() {
            return addrLat;
        }

        public void setAddrLat(String addrLat) {
            this.addrLat = addrLat;
        }
    }
}
