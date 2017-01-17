package com.jetcloud.hgbw.bean;

import java.util.List;

/**
 * Created by Cqing on 2017/1/17.
 */

public class MachineBannerBean {

    /**
     * banner : [{"url":"11","banner_id":19,"pic":"/static/banner/d890eeae-dbaa-11e6-b357-6807159ba7ea.png",
     * "btype":"A","keyword":null,"id":20}]
     * status : success
     */

    private String status;
    private List<BannerBean> banner;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class BannerBean {
        /**
         * url : 11
         * banner_id : 19
         * pic : /static/banner/d890eeae-dbaa-11e6-b357-6807159ba7ea.png
         * btype : A
         * keyword : null
         * id : 20
         */

        private String url;
        private int banner_id;
        private String pic;
        private String btype;
        private Object keyword;
        private int id;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getBanner_id() {
            return banner_id;
        }

        public void setBanner_id(int banner_id) {
            this.banner_id = banner_id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getBtype() {
            return btype;
        }

        public void setBtype(String btype) {
            this.btype = btype;
        }

        public Object getKeyword() {
            return keyword;
        }

        public void setKeyword(Object keyword) {
            this.keyword = keyword;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
