package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.MyOrderBean;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CusAlertDialog;
import com.jetcloud.hgbw.view.CusAlertDialogWithTwoBtn;
import com.jetcloud.hgbw.view.MyListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.jetcloud.hgbw.app.HgbwStaticString.JUMP_RESOURCE;

/***
 * Created by Cqing on 2016/12/30.
 */

public class MyOrderParentAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<MyOrderBean.OrdersBean> data;
    private static final String TAG_LOG = MyOrderParentAdapter.class.getSimpleName();
    private Context context;
    private HgbwApplication app;
    private MachineInfo machineInfo;
    private List<MyOrderBean.OrdersBean.FoodInfoBean.FoodsBean> foodInfoBeanList;

    public MyOrderParentAdapter(Context context, List<MyOrderBean.OrdersBean> data) {
        super();
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        app = (HgbwApplication) context.getApplicationContext();

    }

    public void setData(List<MyOrderBean.OrdersBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addNewData(List<MyOrderBean.OrdersBean> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        final MyOrderViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_my_order_parent, null);
            holder = new MyOrderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyOrderViewHolder) convertView.getTag();
        }
        //加载子list view
        final MyOrderBean.OrdersBean ordersBean = data.get(i);
        foodInfoBeanList = ordersBean.getFood_info().getFoods();
        holder.lv_myorder_in.setAdapter(new MyOrderChildrenAdapter(context, foodInfoBeanList));

//        machineInfo.setNumber(ordersBean.getNumber());
//        String machineName = machineInfo.getNumber();
        holder.tv_machine_name.setText(ordersBean.getFood_info().getMechine_name());
        holder.tv_order_number.setText(String.format(context.getString(R.string.take_food_order_num), ordersBean.getNumber()));
        holder.tv_time.setText(String.format(context.getString(R.string.trade_time), ordersBean
                .getCreate_time()));

        if (ordersBean.getPay_type().equals(HgbwStaticString.PAY_WAY_VR9)) {
            holder.tv_total_price.setText(String.format(context.getString(R.string.take_food_gcb_total), ordersBean.getCost_real()));
        } else if (ordersBean.getPay_type().equals(HgbwStaticString.PAY_WAY_CNY)){
            holder.tv_total_price.setText(String.format(context.getString(R.string.take_food_total), ordersBean.getCost_real()));
        }
        String foodStateText = null;
        /**
         * # state = {
         #     '0': '未支付',
         #     '1': '未取餐',
         #     '2': '已取餐',
         #     '3': '已退款',
         #     '4': '未支付并返餐',
         # }
         *
         * */
        if (ordersBean.getState().equals("1")) {
            holder.tv_food_type.setTextColor(context.getResources().getColor(R.color.red));
            foodStateText = "待取餐";
        } else if (ordersBean.getState().equals("2")){
            foodStateText = "已取餐";
            holder.tv_food_type.setTextColor(context.getResources().getColor(R.color.gray));
//            holder.btn_go_to_take_food.setVisibility(View.GONE);
        } else if (ordersBean.getState().equals("3")) {
            foodStateText = "已退款";
            holder.tv_food_type.setTextColor(context.getResources().getColor(R.color.gray));
//            holder.btn_go_to_take_food.setVisibility(View.GONE);
        }
        holder.tv_food_type.setText(foodStateText);

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ordersBean.getPay_type().equals(HgbwStaticString.PAY_WAY_CNY))) {

                    if (ordersBean.getState().equals("3")){
                        final CusAlertDialog dialog = new CusAlertDialog(context);
                        dialog.setTitle("提示");
                        dialog.setContent("你已退款");
                        dialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else if (ordersBean.getState().equals("1")){
                        final CusAlertDialogWithTwoBtn dialog = new CusAlertDialogWithTwoBtn(context);
                        dialog.setTitle("提示");
                        dialog.setTitle("你确定要退款");
                        dialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                refundRequest(ordersBean.getNumber());
                                dialog.dismiss();
                            }
                        });
                        dialog.setNegativeButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                } else if ((ordersBean.getPay_type().equals(HgbwStaticString.PAY_WAY_VR9))) {
                    final CusAlertDialog dialog = new CusAlertDialog(context);
                    dialog.setTitle("提示");
                    dialog.setContent("暂不支持该退款");
                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

            }
        });
        holder.btn_go_to_take_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ordersBean.getState().equals("1")){
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra(JUMP_RESOURCE, MyOrderParentAdapter.class.getSimpleName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                } else if (ordersBean.getState().equals("2")){
                    final CusAlertDialog dialog = new CusAlertDialog(context);
                    dialog.setTitle("该商品已被取餐");
                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else if (ordersBean.getState().equals("3")){
                    final CusAlertDialog dialog = new CusAlertDialog(context);
                    dialog.setTitle("提示");
                    dialog.setContent("你已退款");
                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
        return convertView;
    }

    private void refundRequest(String orderNum) {
        final RequestParams params = new RequestParams(HgbwUrl.PAY_REFUND_URL);
        //缓存时间
        params.addQueryStringParameter("identity", SharedPreferenceUtils.getIdentity());
        params.addQueryStringParameter("APP", "android_hgbw");
        params.addQueryStringParameter("number", orderNum);
        params.setCacheMaxAge(1000 * 60);

        x.task().run(new Runnable() {
            @Override
            public void run() {
                x.http().post(params, new Callback.CacheCallback<String>() {

                    private boolean hasError = false;
                    private String result = null;


                    @Override
                    public boolean onCache(String result) {
                        this.result = result;
                        return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
                    }

                    @Override
                    public void onSuccess(String result) {
                        // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                        if (result != null) {
                            this.result = result;
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        hasError = true;
                        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG_LOG, "refundRequest onError: " + ex.getMessage());
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            Log.e(TAG_LOG, "refundRequest onError " + " code: " + responseCode + " message: " + responseMsg);
                        } else { // 其他错误
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "refundRequest cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {
                        if (!hasError && result != null) {
                            Log.i(TAG_LOG, "refundRequest onFinished: " + result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.has("status") && jsonObject.getString("status").equals("success")){
                                    final CusAlertDialog dialog = new CusAlertDialog(context);
                                    dialog.setTitle("提示");
                                    dialog.setContent("退款成功");
                                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
            }
        });
    }

    private static class MyOrderViewHolder {
        @ViewInject(R.id.tv_machine_name)
        TextView tv_machine_name;
        @ViewInject(R.id.tv_order_number)
        TextView tv_order_number;
        @ViewInject(R.id.tv_food_type)
        TextView tv_food_type;
        @ViewInject(R.id.lv_myorder_in)
        MyListView lv_myorder_in;
        @ViewInject(R.id.tv_time)
        TextView tv_time;
        @ViewInject(R.id.tv_total_price)
        TextView tv_total_price;
        @ViewInject(R.id.btn_cancel)
        Button btn_cancel;
        @ViewInject(R.id.btn_go_to_take_food)
        Button btn_go_to_take_food;

        MyOrderViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
