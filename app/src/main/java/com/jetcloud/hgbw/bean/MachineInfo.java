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
    @Column(name="id",isId=true,autoGen=false)
    private String id;
    @Column(name="name")
    private String name;
    private boolean selected ;
    private boolean isEditor;

    public String getId() {
        return id;
    }


    public List<ShopCarInfo> getGoods(DbManager db) throws DbException {
        return db.selector(ShopCarInfo.class).where("p_machine", "=", this.id).findAll();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
