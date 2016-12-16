package com.example.xutilstest;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Cqing on 2016/12/7.
 */

@Table(name = "goods"
        , onCreated = "CREATE UNIQUE INDEX relative_uniques ON goods(title,content)")//为表创建NAME,EMAIL联合唯一索引
public class Good {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "machineId", property = "NOT NULL")
    private long machineId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "number")
    private int number;

    @Column(name = "price")
    private double price;

    public Machine getMachine(DbManager db) throws Exception {
        return db.findById(Machine.class, machineId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Good{" +
                "id=" + id +
                ", machineId='" + machineId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
