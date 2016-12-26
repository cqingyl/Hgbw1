package com.example.xutilstest;

import com.jetcloud.hgbw.bean.GoodsInfo;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by Cqing on 2016/12/7.
 */

@Table(name = "machine"
        , onCreated = "CREATE UNIQUE INDEX relative_unique ON machine(mid)")
public class Machine {
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "mid")
    private long mId;
    public List<GoodsInfo> getGoods(DbManager db) throws DbException {
        return db.selector(GoodsInfo.class).where("machineId", "=", this.id).findAll();
    }

    public Machine(long mId) {
        this.mId = mId;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
//                ", mid='" + mId + '\'' +
                '}';
    }
}
