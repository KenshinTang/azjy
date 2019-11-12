package com.yunlinker.model;

import java.util.List;

/**
 * Created by Administrator on 2019/3/29/029.
 */

public class Mapfindmodel2 {

    /**
     * code : 1
     * curPage : 0
     * data : [{"addrLnt":"104.04339","classescount":"0","businessName":"Test it in the future.","businessId":"30405","addrLat":"30.641981"},{"addrLnt":"104.039615","classescount":"0","businessName":"Trading","businessId":"30407","addrLat":"30.6275758"}]
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
         * addrLnt : 104.04339
         * classescount : 0
         * businessName : Test it in the future.
         * businessId : 30405
         * addrLat : 30.641981
         */

        private String addrLnt;
        private String classescount;
        private String businessName;
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

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
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
