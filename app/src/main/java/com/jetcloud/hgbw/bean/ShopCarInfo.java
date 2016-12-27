package com.jetcloud.hgbw.bean;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Cqing on 2016/12/20.
 */
@Table(name = "good")
public class ShopCarInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * p_id : 1
     * p_type : a
     * p_machine : HG010100001
     * p_name : 午餐肉套餐
     * p_picture : ../images/cp01.png
     * p_price : 15
     * p_address : 成都市新希望国际
     * p_number : 8
     */
    @Column(name = "p_id", isId = true, autoGen = false)
    private int p_id;
    @Column(name = "p_type")
    private String p_type;
    @Column(name = "p_machine")
    private String p_machine;
    @Column(name = "p_name")
    private String p_name;
    @Column(name = "p_picture")
    private String p_picture;
    @Column(name = "p_price")
    private float p_price;
    @Column(name = "p_address")
    private String p_address;
    //本地可变数量
    @Column(name = "p_local_number")
    private int p_local_number;
    //商品最大数量
    @Column(name = "p_number")
    private int p_number;
    private boolean selected ;


    public MachineInfo getMachine(DbManager db) throws Exception {
        return db.findById(MachineInfo.class, p_machine);
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public String getP_machine() {
        return p_machine;
    }

    public void setP_machine(String p_machine) {
        this.p_machine = p_machine;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_picture() {
        return p_picture;
    }

    public void setP_picture(String p_picture) {
        this.p_picture = p_picture;
    }

    public float getP_price() {
        return p_price;
    }

    public void setP_price(float p_price) {
        this.p_price = p_price;
    }

    public String getP_address() {
        return p_address;
    }

    public void setP_address(String p_address) {
        this.p_address = p_address;
    }

    public int getP_local_number() {
        return p_local_number;
    }

    public void setP_local_number(int p_local_number) {
        this.p_local_number = p_local_number;
    }

    public int getP_number() {
        return p_number;
    }

    public void setP_number(int p_number) {
        this.p_number = p_number;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
