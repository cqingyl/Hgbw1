package com.jetcloud.hgbw.bean;

import java.util.List;

/**
 * Created by Cqing on 2017/1/5.
 */

public class MachineListBean {

    /**
     * data : [{"locate":"四川省成都市高新区天府三街11号","state":"1","id":61,"number":"X000129383723","longitude":"104.069563",
     * "nickname":"新希望国际","latitude":"30.552521"},{"locate":"四川省成都市高新区天府三街11号","state":"1","id":62,"number":"123456",
     * "longitude":"104.069563","nickname":"新希望国际","latitude":"30.552521"},{"locate":"发生我","state":"1","id":64,
     * "number":"1234567777","longitude":"104.069563","nickname":"发士大夫","latitude":"30.552521"}]
     * status : success
     */

    private String status;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * locate : 四川省成都市高新区天府三街11号
         * state : 1
         * id : 61
         * number : X000129383723
         * longitude : 104.069563
         * nickname : 新希望国际
         * latitude : 30.552521
         */

        private String locate;
        private String state;
        private int id;
        private String number;
        private String longitude;
        private String nickname;
        private String latitude;

        public String getLocate() {
            return locate;
        }

        public void setLocate(String locate) {
            this.locate = locate;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }
    }
}
