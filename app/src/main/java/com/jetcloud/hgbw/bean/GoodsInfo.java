package com.jetcloud.hgbw.bean;

import java.util.List;

/**
 * Created by Cqing on 2016/12/23.
 */

public class GoodsInfo {


    private List<ABean> a;
    private List<BBean> b;
    private List<CBean> c;
    private List<DBean> d;
    private List<EBean> e;

    public List<ABean> getA() {
        return a;
    }

    public void setA(List<ABean> a) {
        this.a = a;
    }

    public List<BBean> getB() {
        return b;
    }

    public void setB(List<BBean> b) {
        this.b = b;
    }

    public List<CBean> getC() {
        return c;
    }

    public void setC(List<CBean> c) {
        this.c = c;
    }

    public List<DBean> getD() {
        return d;
    }

    public void setD(List<DBean> d) {
        this.d = d;
    }

    public List<EBean> getE() {
        return e;
    }

    public void setE(List<EBean> e) {
        this.e = e;
    }

    public static class ABean extends ShopCarInfo{}

    public static class BBean extends ShopCarInfo{}

    public static class CBean extends ShopCarInfo{}

    public static class DBean extends ShopCarInfo{}

    public static class EBean extends ShopCarInfo{}
}
