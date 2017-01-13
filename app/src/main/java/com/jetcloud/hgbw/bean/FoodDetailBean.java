package com.jetcloud.hgbw.bean;

/**
 * Created by Cqing on 2017/1/12.
 */

public class FoodDetailBean {

    /**
     * status : success
     * info : {"price_cny":15,"id":6,
     * "description
     * ":"端上一盘红烧肉，满屋飘香口水流。此菜本非人间有，天上佳肴落街头。北宋大文豪苏东坡也对你推崇备至，焖你的皮，煮你的肉，既酥又烂，吃后口齿流香。他与你友好合作，发明创造了流传百世的\"东坡肉\"；敝人的祖先更绝，数百年前元兵南侵，我祖从中原南阳逃难，在南下的颠沛流离中，竟然还携带令人垂涎三尺的\"东坡肉\"！","state":"0","kind":"A","num":[[5]],"price_vr9":0.07,"pic":"/static/food/86f0fd5c-d2f0-11e6-bc0c-6807159ba7ea.jpg","name":"地三鲜"}
     */

    private String status;
    private InfoBean info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean extends ShopCarInfo {
    }
}
