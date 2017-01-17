package com.jetcloud.hgbw.bean;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Cqing on 2016/12/12.
 */

@Table(name="machine")
public class MachineInfo implements Serializable{
    /**
     * version : HXD003-99
     * own : null
     * id : 65
     * locate : 四川省成都市高新区天府三街120号
     * latitude : 30.5534
     * longitude : 104.071
     * nickname : 新希望国际B
     * banner_name : www
     * state : 1
     * banner_id : 19
     * city : 成都
     * number : 1234567890
     */

    private String banner_name;
    private String banner_id;
    @Column(name="city")
    private String city;
    @Column(name="id",isId=true,autoGen=false)
    private String number;
    @Column(name="nickname")
    private String nickname;
    @Column(name = "address")
    private String address;
    private boolean selected ;
    private boolean isEditor;
    private String locate;
    private String state;
    private int id;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "latitude")
    private String latitude;
    private String version;
    private Object own;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }



    public List<ShopCarInfo> getGoods(DbManager db) throws DbException {
        return db.selector(ShopCarInfo.class).where("p_machine", "=", this.number).findAll();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEditor() {
        return isEditor;
    }

    public void setEditor(boolean editor) {
        isEditor = editor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBanner_name() {
        return banner_name;
    }

    public void setBanner_name(String banner_name) {
        this.banner_name = banner_name;
    }

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object getOwn() {
        return own;
    }

    public void setOwn(Object own) {
        this.own = own;
    }
}
