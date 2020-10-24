package com.chauncy.account.utils;

public class AccountConstant {
    public static final int ASSET_WIDGET = 100;
    public static final int ASSET_DETAIL_WIDGET = 101;
    public static final int FUNCTION_WIDGET = 102;
    public static final int POSITION_ORDER_WIDGET = 103;

    public static final String ACCOUNT_TYPE_HK = "港股";
    public static final String ACCOUNT_TYPE_USA = "美股";
    public static final String ACCOUNT_TYPE_CN = "A股";

    public static final String ACCOUNT_ID_DEFAULT_VALUE="UNKNOWN";

    public static final String ACCOUNT_FUNCTION_TYPE_CASH="CASH";

    public static final String ACCOUNT_FUNCTION_TYPE_MARGIN="MARGIN";


    public static class Order{
        public static final String ORDER_SUBMITTED ="已提交";
        public static final String ORDER_PENDING_SUBMIT="等待提交";
        public static final String ORDER_CANCELLED ="已撤单";
    }

}
