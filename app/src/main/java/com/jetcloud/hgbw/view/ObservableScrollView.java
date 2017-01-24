package com.jetcloud.hgbw.view;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ScrollView;

/***
 * Created by Cqing
 * on 2017/1/20.
 * *****************************************************
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG            #
 * #                                                   #
 * *****************************************************
 */


public class ObservableScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;
    private boolean mIsStop = false;
    private int scrollX;
    private int scrollY;
    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            if (Math.abs(t - oldt) < 2 || t >= getMeasuredHeight() || t == 0) {
                mIsStop = true;
            } else {
                mIsStop = false;
            }
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt, mIsStop);
        }

    }

    public void sScrollTo(int scrollX, int scrollY) {
        this.scrollTo(scrollX, scrollY);
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        scrollTo(scrollX, scrollY);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        scrollX = getScrollY();
        scrollY = getScrollX();
        return super.onSaveInstanceState();
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

}
