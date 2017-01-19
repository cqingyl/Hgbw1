package com.jetcloud.hgbw.bean;

import java.util.List;

/**
 * Created by Cqing on 2017/1/17.
 */

public class MyOrderBean {


    /**
     * orders : [{"cost_total":0.03,"cost_real":0.03,"number":"1484639761484883","state":"1",
     * "create_time":"2017-01-16 23:56:02","id":54,"user_phone":"13340902246","pay_type":"10",
     * "food_info":{"mechine_number":"1234567890","foodd_out":{"5":"1"},"foods":[{"food_name":"苏打水",
     * "food_pic":"/static/food/85cadf38-d7c3-11e6-a470-6807159ba7ea.png","num":"1","food_pay_way":"10","id":"30",
     * "food_price":"0.03"}]}},{"cost_total":0.04,"cost_real":0.04,"number":"1484637392611403","state":"1",
     * "create_time":"2017-01-16 23:16:33","id":50,"user_phone":"13340902246","pay_type":"10",
     * "food_info":{"mechine_number":"1234567890","foodd_out":{"7":"1"},"foods":[{"num":"1","food_name":"橙汁",
     * "food_pay_way":"10","id":"29","food_price":"0.04"}]}}]
     * status : success
     */

    private String status;
    private List<OrdersBean> orders;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class OrdersBean {
        /**
         * cost_total : 0.03
         * cost_real : 0.03
         * number : 1484639761484883
         * state : 1
         * create_time : 2017-01-16 23:56:02
         * id : 54
         * user_phone : 13340902246
         * pay_type : 10
         * food_info : {"mechine_number":"1234567890","foodd_out":{"5":"1"},"foods":[{"food_name":"苏打水",
         * "food_pic":"/static/food/85cadf38-d7c3-11e6-a470-6807159ba7ea.png","num":"1","food_pay_way":"10",
         * "id":"30","food_price":"0.03"}]}
         */

        private double cost_total;
        private double cost_real;
        private String number;
        private String state;
        private String create_time;
        private int id;
        private String user_phone;
        private String pay_type;
        private FoodInfoBean food_info;

        public double getCost_total() {
            return cost_total;
        }

        public void setCost_total(double cost_total) {
            this.cost_total = cost_total;
        }

        public double getCost_real() {
            return cost_real;
        }

        public void setCost_real(double cost_real) {
            this.cost_real = cost_real;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public FoodInfoBean getFood_info() {
            return food_info;
        }

        public void setFood_info(FoodInfoBean food_info) {
            this.food_info = food_info;
        }

        public static class FoodInfoBean {
            /**
             * mechine_number : 1234567890
             * foodd_out : {"5":"1"}
             * foods : [{"food_name":"苏打水","food_pic":"/static/food/85cadf38-d7c3-11e6-a470-6807159ba7ea.png",
             * "num":"1","food_pay_way":"10","id":"30","food_price":"0.03"}]
             */

            private String mechine_number;
            private FooddOutBean foodd_out;
            private List<FoodsBean> foods;

            public String getMechine_number() {
                return mechine_number;
            }

            public void setMechine_number(String mechine_number) {
                this.mechine_number = mechine_number;
            }

            public FooddOutBean getFoodd_out() {
                return foodd_out;
            }

            public void setFoodd_out(FooddOutBean foodd_out) {
                this.foodd_out = foodd_out;
            }

            public List<FoodsBean> getFoods() {
                return foods;
            }

            public void setFoods(List<FoodsBean> foods) {
                this.foods = foods;
            }



            public static class FoodsBean {
                /**
                 * food_name : 苏打水
                 * food_pic : /static/food/85cadf38-d7c3-11e6-a470-6807159ba7ea.png
                 * num : 1
                 * food_pay_way : 10
                 * id : 30
                 * food_price : 0.03
                 */

                private String food_name;
                private String food_pic;
                private String num;
                private String food_pay_way;
                private String id;
                private String food_price;

                public String getFood_name() {
                    return food_name;
                }

                public void setFood_name(String food_name) {
                    this.food_name = food_name;
                }

                public String getFood_pic() {
                    return food_pic;
                }

                public void setFood_pic(String food_pic) {
                    this.food_pic = food_pic;
                }

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }

                public String getFood_pay_way() {
                    return food_pay_way;
                }

                public void setFood_pay_way(String food_pay_way) {
                    this.food_pay_way = food_pay_way;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getFood_price() {
                    return food_price;
                }

                public void setFood_price(String food_price) {
                    this.food_price = food_price;
                }
            }
        }
    }
}
