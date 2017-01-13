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
     * price_cny : 15.0
     * description : 端上一盘红烧肉，满屋飘香口水流。此菜本非人间有，天上佳肴落街头。北宋大文豪苏东坡也对你推崇备至，焖你的皮，煮你的肉，既酥又烂，吃后口齿流香。他与你友好合作，发明创造了流传百世的"东坡肉"；敝人的祖先更绝，数百年前元兵南侵，我祖从中原南阳逃难，在南下的颠沛流离中，竟然还携带令人垂涎三尺的"东坡肉"！
     * num : 5
     * pic : /static/food/86f0fd5c-d2f0-11e6-bc0c-6807159ba7ea.jpg
     * kind : A
     * name : 地三鲜
     * price_vr9 : 0.07
     * id : 6
     * state : 1
     */
    @Column(name = "id", isId = true, autoGen = false)
    private int id;
    @Column(name = "kind")
    private String kind;
    @Column(name = "p_machine")
    private String p_machine;
    @Column(name = "name")
    private String name;
    @Column(name = "pic")
    private String pic;
    @Column(name = "price_vr9")
    private double price_vr9;
    @Column(name = "p_address")
    private String p_address;
    //本地可变数量
    @Column(name = "p_local_number")
    private int p_local_number;
    //商品最大数量
    @Column(name = "num")
    private int num;
    @Column(name = "price_cny")
    private double price_cny;

    private String description;
    private String state;
    private boolean selected ;

    public MachineInfo getMachine(DbManager db) throws Exception {
        return db.findById(MachineInfo.class, p_machine);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getP_machine() {
        return p_machine;
    }

    public void setP_machine(String p_machine) {
        this.p_machine = p_machine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public double getPrice_vr9() {
        return price_vr9;
    }

    public void setPrice_vr9(double price_vr9) {
        this.price_vr9 = price_vr9;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice_cny() {
        return price_cny;
    }

    public void setPrice_cny(double price_cny) {
        this.price_cny = price_cny;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
