package com.creatoo.guangdong_pos.utils;

/**
 * @author Jacky.Cai
 * @version v1.0
 * @ Package com.dayhr.caterers.mobile.utils
 * @ Description:
 * @ date 16/8/23 下午6:02
 */
public class CommonKey {
    public static final class OnceKey {
        public static final String KEY_HAS_LOGINED = "key_has_logined";
        public static final String KEY_HAS_ACTIVED = "key_has_actived";
    }

    /**
     * apiParams Key
     */
    public static final class ApiParams {
        public static final String TICKET_NO = "ticketNo";
        public static final String ORDER_ID = "orderId";
        public static final String ORDER_TYPE = "orderType";
        public static final String USER_NUMBER = "usernumber";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }

    public static final class TableName{

    }

    public static final class BundleKey {

    }

    public static final class RequestCode {

    }

    public static final class ResultCode {

    }

    public static final class SharedPreferenceKey {


    }

    public static final  class  MapKey{
        /**
         * 打印机IP KEY
         */
        public static final String KEY_IP = "ip";
        /**
         * 打印驱动方式KEY (1:Windows驱动,2:TCP/IP免驱动)
         */
        public static final String KEY_PRINT_DRIVING_TYPE = "printdrivingtype";
        /**
         * 打印模板KEY (1:80mm,2:76mm,3:58mm)
         */
        public static final String KEY_PRINT_TEMPLATE = "printtemplate";
        /**
         * 保存PrintSolutionsDetailEntity的JavaBean KEY
         */
        public static final String KEY_SOLUTION_DETAIL = "detail";


        public static final String KEY_BAND_RATE = "baudrate";

        public static final String KEY_PORT = "port";
    }

    public static final class Constant {

        public static final int MODE_DEFAULT = 0;
        public static final int MODE_SONIC = 1;
        public static final int MODE_SONIC_WITH_OFFLINE_CACHE = 2;
    }

    }
