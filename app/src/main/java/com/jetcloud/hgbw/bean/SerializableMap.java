package com.jetcloud.hgbw.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Cqing on 2016/12/30.
 */

public class SerializableMap implements Serializable {
    private Map<String, List<ShopCarInfo>> map;

    public Map<String, List<ShopCarInfo>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<ShopCarInfo>> map) {
        this.map = map;
    }

}
