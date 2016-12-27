package com.jetcloud.hgbw.bean;

import java.util.List;

/**
 * Created by Cqing on 2016/12/27.
 */

public class FoodDetail {

    private List<PMealBean> p_meal;

    public List<PMealBean> getP_meal() {
        return p_meal;
    }

    public void setP_meal(List<PMealBean> p_meal) {
        this.p_meal = p_meal;
    }

    public static class PMealBean {
        /**
         * p_id : 1
         * s_name : 小炒杏鲍菇
         * s_picture : ../images/小炒杏鲍菇.png
         * s_price : 15
         * s_total : null
         * s_totalprice : null
         * s_introduce :
         * 端上一盘红烧肉，满屋飘香口水流。此菜本非人间有，天上佳肴落街头。北宋大文豪苏东坡也对你推崇备至，焖你的皮，煮你的肉，既酥又烂，吃后口齿流香。他与你友好合作，发明创造了流传百世的"东坡肉"；敝人的祖先更绝，数百年前元兵南侵，我祖从中原南阳逃难，在南下的颠沛流离中，竟然还携带令人垂涎三尺的"东坡肉"！
         * s_phone : 12321231
         */

        private int p_id;
        private String s_name;
        private String s_picture;
        private int s_price;
        private Object s_total;
        private Object s_totalprice;
        private String s_introduce;
        private int s_phone;

        public int getP_id() {
            return p_id;
        }

        public void setP_id(int p_id) {
            this.p_id = p_id;
        }

        public String getS_name() {
            return s_name;
        }

        public void setS_name(String s_name) {
            this.s_name = s_name;
        }

        public String getS_picture() {
            return s_picture;
        }

        public void setS_picture(String s_picture) {
            this.s_picture = s_picture;
        }

        public int getS_price() {
            return s_price;
        }

        public void setS_price(int s_price) {
            this.s_price = s_price;
        }

        public Object getS_total() {
            return s_total;
        }

        public void setS_total(Object s_total) {
            this.s_total = s_total;
        }

        public Object getS_totalprice() {
            return s_totalprice;
        }

        public void setS_totalprice(Object s_totalprice) {
            this.s_totalprice = s_totalprice;
        }

        public String getS_introduce() {
            return s_introduce;
        }

        public void setS_introduce(String s_introduce) {
            this.s_introduce = s_introduce;
        }

        public int getS_phone() {
            return s_phone;
        }

        public void setS_phone(int s_phone) {
            this.s_phone = s_phone;
        }
    }
}
