package com.jetcloud.hgbw.bean;

/**
 * Created by Cqing on 2016/12/12.
 */

public class GoodsInfo {

    private String title;
    private String content;
    private int num;
    private double money;
    private String imageUrl;
    private boolean selected ;

    public GoodsInfo(String title, String content, int num, double money, String imageUrl) {
        this.title = title;
        this.content = content;
        this.num = num;
        this.money = money;
        this.imageUrl = imageUrl;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
