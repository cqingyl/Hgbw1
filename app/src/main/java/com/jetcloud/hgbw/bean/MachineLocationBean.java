package com.jetcloud.hgbw.bean;

import java.util.List;

/**
 * Created by Cqing on 2017/1/11.
 */

public class MachineLocationBean {


    /**
     * status : success
     * mechines : [{"version":"HXD003-99","own":null,"id":65,"locate":"四川省成都市高新区天府三街120号","latitude":30.5534,
     * "longitude":104.071,"nickname":"新希望国际B","banner_name":"www","state":"1","banner_id":19,"city":"成都",
     * "number":"1234567890"},{"version":"HXD003-99","own":null,"id":61,"locate":"四川省成都市高新区天府三街11号","latitude":30
     * .5525,"longitude":104.07,"nickname":"新希望国际A","banner_name":"aaa","state":"1","banner_id":16,"city":"成都",
     * "number":"X000129383723"}]
     */

    private String status;
    private List<MechinesBean> mechines;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MechinesBean> getMechines() {
        return mechines;
    }

    public void setMechines(List<MechinesBean> mechines) {
        this.mechines = mechines;
    }

    public static class MechinesBean extends MachineInfo {
    }
}
