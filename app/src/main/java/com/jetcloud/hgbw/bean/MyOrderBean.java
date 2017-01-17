package com.jetcloud.hgbw.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Cqing on 2017/1/17.
 */

public class MyOrderBean {

    /**
     * orders : [{"user_phone":"13340902246","number":"1484535664582154","cost_total":0.07,"cost_real":0.07,
     * "food_info":{"foods":[{"num":"1","id":"14"}],"mechine_number":"1234567890","foodd_out":{"4":"1"}},
     * "pay_type":"10","create_time":"2017-01-17 00:00:00","id":5,"state":"0"},{"user_phone":"13340902246",
     * "number":"1484553566706743","cost_total":0.07,"cost_real":0.07,"food_info":{"foods":[{"id":"14","num":"1"}],
     * "mechine_number":"1234567890","foodd_out":{"4":"1"}},"pay_type":"10","create_time":"2017-01-17 00:00:00",
     * "id":41,"state":"1"},{"user_phone":"13340902246","number":"1484553607577186","cost_total":0.07,"cost_real":0
     * .07,"food_info":{"foods":[{"id":"14","num":"1"}],"mechine_number":"1234567890","foodd_out":{"4":"1"}},
     * "pay_type":"10","create_time":"2017-01-17 00:00:00","id":42,"state":"1"},{"user_phone":"13340902246",
     * "number":"1484553708476752","cost_total":0.07,"cost_real":0.07,"food_info":{"foods":[{"num":"1","id":"14"}],
     * "mechine_number":"1234567890","foodd_out":{"4":"1"}},"pay_type":"10","create_time":"2017-01-17 00:00:00",
     * "id":43,"state":"1"},{"user_phone":"13340902246","number":"1484553718834182","cost_total":0.07,"cost_real":0
     * .07,"food_info":{"foods":[{"num":"1","id":"14"}],"mechine_number":"1234567890","foodd_out":{"4":"1"}},
     * "pay_type":"10","create_time":"2017-01-17 00:00:00","id":44,"state":"1"},{"user_phone":"13340902246",
     * "number":"1484556942561208","cost_total":0.11,"cost_real":0.11,"food_info":{"foods":[{"id":"11","num":"1"}],
     * "mechine_number":"1234567890","foodd_out":{"2":"1"}},"pay_type":"10","create_time":"2017-01-17 00:00:00",
     * "id":46,"state":"1"},{"user_phone":"13340902246","number":"1484557026204322","cost_total":0.11,"cost_real":0
     * .11,"food_info":{"foods":[{"id":"11","num":"1"}],"mechine_number":"1234567890","foodd_out":{"2":"1"}},
     * "pay_type":"10","create_time":"2017-01-17 00:00:00","id":47,"state":"1"},{"user_phone":"13340902246",
     * "number":"1484557321867521","cost_total":0.03,"cost_real":0.03,"food_info":{"foods":[{"id":"30","num":"1"}],
     * "mechine_number":"1234567890","foodd_out":{"5":"1"}},"pay_type":"10","create_time":"2017-01-17 00:00:00",
     * "id":48,"state":"1"},{"user_phone":"13340902246","number":"1484557442070115","cost_total":0.15,"cost_real":0
     * .15,"food_info":{"foods":[{"id":"12","num":"1"}],"mechine_number":"1234567890","foodd_out":{"3":"1"}},
     * "pay_type":"10","create_time":"2017-01-17 00:00:00","id":49,"state":"1"}]
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
         * user_phone : 13340902246
         * number : 1484535664582154
         * cost_total : 0.07
         * cost_real : 0.07
         * food_info : {"foods":[{"num":"1","id":"14"}],"mechine_number":"1234567890","foodd_out":{"4":"1"}}
         * pay_type : 10
         * create_time : 2017-01-17 00:00:00
         * id : 5
         * state : 0
         */

        private String user_phone;
        private String number;
        private double cost_total;
        private double cost_real;
        private FoodInfoBean food_info;
        private String pay_type;
        private String create_time;
        private int id;
        private String state;

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

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

        public FoodInfoBean getFood_info() {
            return food_info;
        }

        public void setFood_info(FoodInfoBean food_info) {
            this.food_info = food_info;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public static class FoodInfoBean {
            /**
             * foods : [{"num":"1","id":"14"}]
             * mechine_number : 1234567890
             * foodd_out : {"4":"1"}
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

            public static class FooddOutBean {
                /**
                 * 4 : 1
                 */

                @SerializedName("4")
                private String value4;

                public String getValue4() {
                    return value4;
                }

                public void setValue4(String value4) {
                    this.value4 = value4;
                }
            }

            public static class FoodsBean {
                /**
                 * num : 1
                 * id : 14
                 */

                private String num;
                private String id;

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }
            }
        }
    }
}
