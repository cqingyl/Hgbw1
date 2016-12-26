package com.example.xutilstest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.bean.GoodsInfo;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_db_result)
    TextView tv_db_result;
    @ViewInject(R.id.rv)
    RecyclerView mRecyclerView;
    @ViewInject(R.id.back)
    ImageView back;
    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.subtitle)
    TextView subtitle;
    @ViewInject(R.id.top_bar)
    LinearLayout topBar;
    @ViewInject(R.id.tv_total_price)
    TextView tvTotalPrice;
    @ViewInject(R.id.all_chekbox)
    CheckBox allChekbox;
    @ViewInject(R.id.tv_delete)
    TextView tvDelete;
    @ViewInject(R.id.tv_go_to_pay)
    TextView tvGoToPay;

    @ViewInject(R.id.ll_shar)
    LinearLayout llShar;
    @ViewInject(R.id.ll_info)
    LinearLayout llInfo;

    @ViewInject(R.id.tv_share)
    TextView tvShare;
    @ViewInject(R.id.tv_save)
    TextView tvSave;
    @ViewInject(R.id.ll_cart)
    LinearLayout llCart;

    private int number;
    private int flag = 0;
    private List<Machine> machineLists = new ArrayList<>();
    private Map<String, Machine> goods = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        tv_db_result = (TextView) findViewById(R.id.tv_db_result);
//        mRecyclerView = (RecyclerView) findViewById(R.id.lv);
//        subtitle = (TextView) findViewById(R.id.subtitle);

        x.view().inject(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private String s = "{\"id\":157336,\"results\":[{\"title\":\"ma ling shu\",\"content\":\"ok!!\",\"num\":\"3\",\"price\":\"23\"},{\"title\":\"yang yu pian\",\"content\":\"nice!!\",\"num\":\"1\",\"price\":\"29\"},{\"title\":\"tu dou si\",\"content\":\"good!!\",\"num\":\"4\",\"price\":\"14\"}]}";

    public void createMachine(){
        for (int i = 0; i < 2; i++){
            machineLists.add(new Machine(i));
            List<GoodsInfo> goodsInfoList = new ArrayList<>();
            for (int j = 0; j <= i; j++){
                goodsInfoList.add(new GoodsInfo());
            }
        }
    }
    @Override
    protected void onResume() {
//            tv_db_result.setText("wait...");
//            x.task().run(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        String temp = "";
////                        XUTL.setContext(MainActivity.this);
//
////                        DbManager.DaoConfig daoConfig = XUTL.getDaoConfig();
////                        DbManager db = x.getDb(daoConfig);
//                        JSONObject jsonObject = new JSONObject(s);
//
//                        long mid = jsonObject.getInt("id");
//                        Machine machine = new Machine(mid);
////                        db.replace(machine);
//                        GoodsInfo goodsInfo;
//                        temp += "machine: " +  machine.getmId();
//                        JSONArray array = jsonObject.getJSONArray("results");
//                        JSONObject goodsObject;
//                        for (int i = 0; i < array.length(); i++) {
//                            goodsObject = array.getJSONObject(i);
//                            goodsInfo = new GoodsInfo();
////                            goodsInfo.setTitle(goodsObject.getString("title"));
////                            goodsInfo.setContent(goodsObject.getString("content"));
//                            number = goodsObject.getInt("num");
////                            goodsInfo.setNumber(number);
////                            goodsInfo.setPrice(Double.parseDouble(goodsObject.getString("price")));
////                            goodsInfo.setMachineId(mid);
////                            db.replace(goodsInfo);
//
//                        }
//
//                        List<GoodsInfo> goodsInfos = db.selector(GoodsInfo.class).findAll();
//                        temp += "goodsInfo size:" + goodsInfos.size() + "\n";
//                        if (goodsInfos.size() > 0) {
//                            temp += "last goodsInfo:" + goodsInfos.get(goodsInfos.size() - 1) + "\n" + db.selector(Machine.class).findAll();
//                        }
//
//                        final String result = temp;
//                        final List a = (List) db.findAll(GoodsInfo.class);
//                        x.task().post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mRecyclerView.setAdapter(new MyAdapter(MainActivity.this, a));
//                                tv_db_result.setText(result);
//                            }
//                        });
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        super.onResume();
    }
    @Event({R.id.all_chekbox, R.id.tv_delete, R.id.tv_go_to_pay, R.id.subtitle, R.id.tv_save, R.id.tv_share})
    private void onClickSubtitle(View view) {
        switch (view.getId()){
            case R.id.subtitle:
            if (flag == 0) {
                llInfo.setVisibility(View.GONE);
                tvGoToPay.setVisibility(View.GONE);
                llShar.setVisibility(View.VISIBLE);
                subtitle.setText("完成");
            } else if (flag == 1) {
                llInfo.setVisibility(View.VISIBLE);
                tvGoToPay.setVisibility(View.VISIBLE);
                llShar.setVisibility(View.GONE);
                subtitle.setText("编辑");
            }
            flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
            break;
        }
    }

}
