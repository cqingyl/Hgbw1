package com.jetcloud.hgbw.bean;

import java.util.List;

/***
 * Created by Cqing on 2016/12/27.
 */

public class TakeFoodInfo {

    private List<MealBean> meal;

    public List<MealBean> getMeal() {
        return meal;
    }

    public void setMeal(List<MealBean> meal) {
        this.meal = meal;
    }

    public static class MealBean {
        /**
         * m_id : a43a467afdf-5
         * p_id : 1
         * t_name : 小炒杏鲍菇
         * p_picture : ../../images/小炒杏鲍菇.png
         * t_adderss : 成都市新希望国际
         * t_price : 15
         * t_total : 2
         * t_totalprice : 30
         * t_totalorderprice : 30
         * t_time : 2016-12-26T06:47:00.000Z
         * t_singletime : (NULL)
         * t_num : 1482734836393
         * t_state : 待取餐
         * t_jiqi : hg010100001
         */

        private String m_id;
        private int p_id;
        private String t_name;
        private String p_picture;
        private String t_adderss;
        private int t_price;
        private int t_total;
        private int t_totalprice;
        private int t_totalorderprice;
        private String t_time;
        private String t_singletime;
        private long t_num;
        private String t_state;
        private String t_jiqi;

        public String getM_id() {
            return m_id;
        }

        public void setM_id(String m_id) {
            this.m_id = m_id;
        }

        public int getP_id() {
            return p_id;
        }

        public void setP_id(int p_id) {
            this.p_id = p_id;
        }

        public String getT_name() {
            return t_name;
        }

        public void setT_name(String t_name) {
            this.t_name = t_name;
        }

        public String getP_picture() {
            return p_picture;
        }

        public void setP_picture(String p_picture) {
            this.p_picture = p_picture;
        }

        public String getT_adderss() {
            return t_adderss;
        }

        public void setT_adderss(String t_adderss) {
            this.t_adderss = t_adderss;
        }

        public int getT_price() {
            return t_price;
        }

        public void setT_price(int t_price) {
            this.t_price = t_price;
        }

        public int getT_total() {
            return t_total;
        }

        public void setT_total(int t_total) {
            this.t_total = t_total;
        }

        public int getT_totalprice() {
            return t_totalprice;
        }

        public void setT_totalprice(int t_totalprice) {
            this.t_totalprice = t_totalprice;
        }

        public int getT_totalorderprice() {
            return t_totalorderprice;
        }

        public void setT_totalorderprice(int t_totalorderprice) {
            this.t_totalorderprice = t_totalorderprice;
        }

        public String getT_time() {
            return t_time;
        }

        public void setT_time(String t_time) {
            this.t_time = t_time;
        }

        public String getT_singletime() {
            return t_singletime;
        }

        public void setT_singletime(String t_singletime) {
            this.t_singletime = t_singletime;
        }

        public long getT_num() {
            return t_num;
        }

        public void setT_num(long t_num) {
            this.t_num = t_num;
        }

        public String getT_state() {
            return t_state;
        }

        public void setT_state(String t_state) {
            this.t_state = t_state;
        }

        public String getT_jiqi() {
            return t_jiqi;
        }

        public void setT_jiqi(String t_jiqi) {
            this.t_jiqi = t_jiqi;
        }
    }
}
