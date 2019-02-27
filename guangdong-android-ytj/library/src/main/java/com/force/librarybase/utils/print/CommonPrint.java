package com.force.librarybase.utils.print;

/**
 * @author chenlei
 * @version v1.0
 * @Package com.force.librarybase.utils.print
 * @Description: 类描述
 * @date 2017/11/16 19:38
 */
public class CommonPrint {

    public static final  class  MapKey{
        /**
         * 打印机IP KEY
         */
        public static final String KEY_IP = "ip";
        /**
         * 打印驱动方式KEY (1:Windows驱动,2:TCP/IP免驱动,,3:U,4:蓝牙)
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

        public static final String KEY_BLUETOOTH_ADDRESS = "bluaddress";
        public static final String KEY_BLUETOOTH_NAME = "bluname";
    }

    public static final  class  MapValue{

        /**
         * 打印驱动方式USB
         */
        public static final String KEY_PRINT_USB = "USB";

        /**
         * IP
         */
        public static final String KEY_PRINT_IP = "IP";

        /**
         * 打印驱动方式蓝牙
         */
        public static final String KEY_PRINT_BLUETOOTH = "BLUETOOTH";

    }
}
