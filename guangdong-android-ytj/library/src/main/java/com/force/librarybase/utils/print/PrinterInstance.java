package com.force.librarybase.utils.print;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.force.librarybase.utils.Null;
import com.force.librarybase.utils.logger.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * @author chenlei
 * @version v1.0
 * @Package com.force.librarybase.utils.print
 * @Description: 类描述
 * @date 2017/11/16 13:41
 */
public class PrinterInstance {

    /**
     * 打印纸一行最大的字节 --- 58mm
     */
    private static final int INT_LINE_58_BYTE_SIZE = 32;
    /**
     * 打印纸一行最大的字节 --- 80mm
     */
    private static final int INT_LINE_80_BYTE_SIZE = 48;
    /**
     * 当前打印纸一行最大的字节
     */
    private static int INT_LINE_BYTE_SIZE;
    /**
     * 三列--左边区域长度（字节数），默认等分
     */
    private static int INT_LEFT_LENGTH ;
    /**
     * 三列--右边区域长度（字节数）,默认等分
     */
    private static int INT_RIGHT_LENGTH;
    /**
     * 四列--1~2列间区域长度（字节数），默认等分
     */
    private static int INT_DIV_1_LENGTH ;
    /**
     * 四列--2~3列间区域长度（字节数）,默认等分
     */
    private static int INT_DIV_2_LENGTH;
    /**
     * 四列--3~4列间区域长度（字节数）,默认等分
     */
    private static int INT_DIV_3_LENGTH;
    /**
     * 小票打印菜品的名称，上限字数，超过则截断 --- （80mm：8个字，58mm：6个字）
     */
    public static int INT_DISH_NAME_MAX_LEN = 8;

    // 对齐方式
    public static final int ALIGN_LEFT = 0;     // 靠左
    public static final int ALIGN_CENTER = 1;   // 居中
    public static final int ALIGN_RIGHT = 2;    // 靠右

    private static int fontsize = 1;

    private String charsetName = "GBK";

    private BasePrinterPort myPrinterPort;

    private boolean isConnected = false;


    public PrinterInstance(HashMap<String, Object> info,Context context){
        if(TextUtils.equals("2", info.get(CommonPrint.MapKey.KEY_PRINT_DRIVING_TYPE).toString())){

            String ip = info.get(CommonPrint.MapKey.KEY_IP).toString();
            String ipStr = info.get(CommonPrint.MapKey.KEY_PORT).toString();
            int port = 0;
            if(ipStr !=null)
              port =  Integer.valueOf(ipStr);
            if(ip != null && port != 0) {
                myPrinterPort = new IPPort(ip, port);
                openConnection();
            }
        }else if(TextUtils.equals("3", info.get(CommonPrint.MapKey.KEY_PRINT_DRIVING_TYPE).toString())){
            myPrinterPort =  USBPort.getInstance (context);
            openConnection();//myPrinterPort.open();

        }else if(TextUtils.equals("4", info.get(CommonPrint.MapKey.KEY_PRINT_DRIVING_TYPE).toString())){
            String  address = info.get(CommonPrint.MapKey.KEY_BLUETOOTH_ADDRESS).toString();
            if(address != null && !address.equals("")) {
                myPrinterPort = BluetoothPort.getInstance(address);
                openConnection();
            }
        }else{

        }
        //初始化列宽相关参数值
        String printTemplate = info.get(CommonPrint.MapKey.KEY_PRINT_TEMPLATE).toString();
        if (!Null.isNull(printTemplate)){
            initColumnParms(printTemplate);
        }
    }

    public PrinterInstance(Context context ,String printTemplate ) {
        myPrinterPort =  USBPort.getInstance (context);

        //初始化列宽相关参数值
        if (!Null.isNull(printTemplate)){
            initColumnParms(printTemplate);
        }
    }

    public PrinterInstance(String address,String printTemplate){
        myPrinterPort =  BluetoothPort.getInstance(address);

        //初始化列宽相关参数值
        if (!Null.isNull(printTemplate)){
            initColumnParms(printTemplate);
        }
    }

    public PrinterInstance(String ip, int port,String printTemplate){
        myPrinterPort = new IPPort(ip,port);
        //初始化列宽相关参数值
        if (!Null.isNull(printTemplate)){
            initColumnParms(printTemplate);
        }

    }

    public boolean openConnection() {
        if (myPrinterPort == null) {
            return false;
        }
        if (myPrinterPort instanceof USBPort || myPrinterPort instanceof BluetoothPort)
            isConnected = getState();
        if (isConnected){
            return isConnected;
        }else {
            isConnected = myPrinterPort.open();
            return isConnected;
        }
    }

    public void closeConnection() {
        if(myPrinterPort != null) {
            myPrinterPort.close();
        }
    }

    public void closeIpConnection(){
        if(myPrinterPort instanceof IPPort && myPrinterPort != null) {
            myPrinterPort.close();
        }
    }

    public int sendBytesData(byte[] srcData) {
        if(myPrinterPort == null) {
            return -1;
        } else if(srcData != null && srcData.length != 0) {
            if(myPrinterPort.write(srcData) < 0) {
                    return -3;
            }
            return srcData.length;
        } else {
            return -2;
        }
    }

    public void printText(String content) {
        byte[] data = null;
        if(this.charsetName != "") {
            try {
                data = content.getBytes(this.charsetName);
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
            }
        } else {
            data = content.getBytes();
        }

        this.sendBytesData(data);
    }

    public boolean isConnected() {
        return isConnected;
    }

    /**
     * 初始化打印机
     *
     */
    public void initPos(StringBuilder builder)  {

        byte[] data = {0x1B, '@'};
        builder.append(new String(data));
    }

    public void openCasherBox(StringBuilder builder)  {
        byte SendCash[] = {0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00};
        builder.append(new String(SendCash));
    }

    /**
     * 进纸并全部切割
     *
     * @return
     * @throws IOException
     */
    public  void feedAndCut(StringBuilder builder) {
        // 走纸并切纸，最后一个参数控制走纸的长度
        byte[] data = {(byte) 0x1d, (byte) 0x56, (byte) 0x42, (byte) 0x15};

        builder.append( new String(data));
    }

    /**
     * 打印位置调整
     *
     * @param alignMode 打印位置  0：居左(默认) 1：居中 2：居右
     * @throws
     */
    public  void setAlignCmd(StringBuilder stringBuilder ,int alignMode) {
        byte[] data = {(byte) 0x1b, (byte) 0x61, (byte) 0x0};
        if (alignMode == ALIGN_LEFT) {
            data[2] = (byte) 0x00;
        } else if (alignMode == ALIGN_CENTER) {
            data[2] = (byte) 0x01;
        } else if (alignMode == ALIGN_RIGHT) {
            data[2] = (byte) 0x02;
        }

        stringBuilder.append(new String(data));
    }

    /**
     * 初始化打印机
     *
     * @throws IOException
     */
    public void printOneLine(StringBuilder builder){
        builder.append("\n");
        if (INT_LINE_BYTE_SIZE == 32){
            builder.append("--------------------------------");
        }else{
            builder.append("------------------------------------------------");
        }
    }

    /**
     * 添加换行符
     */
    public  void addLineSeparator(StringBuilder builder) {
        builder.append("\n");
    }

    /**
     * 添加N个换行符
     */
    public  void addNLineSeparator(StringBuilder builder,int count) {
        for (int i=0 ; i<count;i++)
            builder.append("\n");
    }

    /**
     * 新起一行，打印文字
     */
    public  void addLineSeparatorText(StringBuilder builder,String text) {
        builder.append("\n");
        builder.append(text);
    }

    /**
     *
     * @param num 字体大小倍数
     * @return
     */
    public  void  setFontSizeCmd(StringBuilder builder,int num)  {
        byte[] data = {(byte) 0x1d, (byte) 0x21, (byte) 0x0};
        fontsize = num;
        switch (num)
        {
            case 1:
                data[2] = 0x00;
                break;
            case 2:
                data[2] = 0x11;
                break;
            case 3:
                data[2] = 0x22;
                break;
            case 4:
                data[2] = 0x33;
                break;
            case 5:
                data[2] = 0x44;
                break;
            case 6:
                data[2] = 0x55;
                break;
            case 7:
                data[2] = 0x66;
                break;
            case 8:
                data[2] = 0x77;
                break;
            default:
                fontsize = 1;
                data[2] = 0x00;
                break;
        }
        builder.append(new String(data));
    }

    /**
     * 初始化列宽相关参数值
     * @param printTemplate 打印模板(1:80mm,2:76mm,3:58mm)
     */
    private void initColumnParms(String printTemplate) {

        if(TextUtils.isEmpty(printTemplate) || TextUtils.equals("3",printTemplate)){
            INT_LINE_BYTE_SIZE = INT_LINE_58_BYTE_SIZE;
            INT_LEFT_LENGTH = INT_LINE_BYTE_SIZE / 2;
            INT_RIGHT_LENGTH = INT_LINE_BYTE_SIZE / 2;
            INT_DIV_1_LENGTH = 16;
            INT_DIV_2_LENGTH = 6;
            INT_DIV_3_LENGTH = 10;
            INT_DISH_NAME_MAX_LEN = 6;
        }else{
            INT_LINE_BYTE_SIZE = INT_LINE_80_BYTE_SIZE;
            INT_LEFT_LENGTH = INT_LINE_BYTE_SIZE / 2;
            INT_RIGHT_LENGTH = INT_LINE_BYTE_SIZE / 2;
            INT_DIV_1_LENGTH = 24;
            INT_DIV_2_LENGTH = 12;
            INT_DIV_3_LENGTH = 12;
            INT_DISH_NAME_MAX_LEN = 10;
        }
    }

    /**
     * 组合文字--生成两列
     *
     * @param leftStr  左侧文字
     * @param rightStr 右侧文字
     * @return  返回组成的文字，列间插入空格
     */
    @SuppressLint("NewApi")
    public String combineTwoData(String leftStr, String rightStr) {
        if(null == leftStr){
            Logger.e("leftStr is null");
            return null;
        }
        if(null == rightStr){
            Logger.e("rightStr is null");
            return null;
        }
        leftStr = leftStr.trim();
        rightStr = rightStr.trim();

        StringBuilder sb = new StringBuilder();
        int lTxtLength = getBytesLength(leftStr);
        int rTxtLength = getBytesLength(rightStr);
        sb.append(leftStr);

        // 计算两侧文字中间的空格
        int spaceBetweenLeftAndRight = INT_LINE_BYTE_SIZE/fontsize - lTxtLength - rTxtLength;

        for (int i = 0; i < spaceBetweenLeftAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightStr);
        return sb.toString();
    }

    /**
     * 组合文字--生成三列
     *
     * @param leftStr   左侧文字
     * @param middleStr 中间文字
     * @param rightStr  右侧文字
     * @return  返回组成的文字，列间插入空格
     */
    @SuppressLint("NewApi")
    public  String combineThreeData(String leftStr, String middleStr, String rightStr) {
        if(null == leftStr){
            Logger.e("leftStr is null");
            return null;
        }
        if(null == middleStr){
            Logger.e("middleStr is null");
            return null;
        }
        if(null == rightStr){
            Logger.e("rightStr is null");
            return null;
        }
        leftStr = leftStr.trim();
        middleStr = middleStr.trim();
        rightStr = rightStr.trim();

        StringBuilder sb = new StringBuilder();
        // 第一列最多显示 INT_DISH_NAME_MAX_LEN 个汉字 + 两个点
        leftStr = formatDishName(leftStr);

        int lTxtLength = getBytesLength(leftStr);
        int mTxtLength = getBytesLength(middleStr);
        int rTxtLength = getBytesLength(rightStr);
        sb.append(leftStr);

        // 计算左侧文字和中间文字的空格长度
        int spaceBetweenLeftAndMid = INT_LEFT_LENGTH - lTxtLength - mTxtLength / 2;
        if (mTxtLength % 2 != 0){
            spaceBetweenLeftAndMid --;
        }

        for (int i = 0; i < spaceBetweenLeftAndMid; i++) {
            sb.append(" ");
        }
        sb.append(middleStr);

        // 计算右侧文字和中间文字的空格长度
        int spaceBetweenMidAndRight = INT_RIGHT_LENGTH - mTxtLength / 2 - rTxtLength;

        for (int i = 0; i < spaceBetweenMidAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightStr);
        return sb.toString();
    }

    /**
     * 组合文字--生成四列
     *
     * @param firstColStr   第一列文字
     * @param secondColStr  第二列文字
     * @param thirdColStr   第三列文字
     * @param fourthColStr  第四列文字
     * @return  返回组成的文字，列间插入空格
     */
    @SuppressLint("NewApi")
    public  String combineFourData(String firstColStr, String secondColStr, String thirdColStr, String fourthColStr) {
        if(null == firstColStr){
            Logger.e("firstColStr is null");
            return null;
        }
        if(null == secondColStr){
            Logger.e("secondColStr is null");
            return null;
        }
        if(null == thirdColStr){
            Logger.e("thirdColStr is null");
            return null;
        }
        if(null == fourthColStr){
            Logger.e("fourthColStr is null");
            return null;
        }
        firstColStr = firstColStr.trim();
        secondColStr = secondColStr.trim();
        thirdColStr = thirdColStr.trim();
        fourthColStr = fourthColStr.trim();

        StringBuilder sb = new StringBuilder();
        // 第一列最多显示 INT_DISH_NAME_MAX_LEN 个汉字 + 两个点
        firstColStr = formatDishName(firstColStr);

        int firTxtLength = getBytesLength(firstColStr);
        int secTxtLength = getBytesLength(secondColStr);
        int thrTxtLength = getBytesLength(thirdColStr);
        int fourTxtLength = getBytesLength(fourthColStr);
        sb.append(firstColStr);

        // 计算第一列文字和第二列文字的空格长度
        int spaceBetweenFirAndSec = INT_DIV_1_LENGTH - firTxtLength - secTxtLength / 2;

        for (int i = 0; i < spaceBetweenFirAndSec; i++) {
            sb.append(" ");
        }
        sb.append(secondColStr);

        // 计算第二列文字和第三列文字的空格长度
        int spaceBetweenSecAndThr = INT_DIV_2_LENGTH - secTxtLength / 2 - thrTxtLength / 2 - 4;

        for (int i = 0; i < spaceBetweenSecAndThr; i++) {
            sb.append(" ");
        }
        sb.append(thirdColStr);

        // 计算第三列文字和第四列文字的空格长度
        INT_DIV_3_LENGTH = INT_LINE_BYTE_SIZE - getBytesLength(sb.toString());
        int spaceBetweenThrAndFour = INT_DIV_3_LENGTH - fourTxtLength;
        //int spaceBetweenThrAndFour = INT_DIV_3_LENGTH - thrTxtLength / 2 - fourTxtLength;

        for (int i = 0; i < spaceBetweenThrAndFour; i++) {
            sb.append(" ");
        }
        sb.append(fourthColStr);

        return sb.toString();
    }

    /**
     * 获取数据长度
     *
     * @param msg
     */
    @SuppressLint("NewApi")
    private  int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    /**
     * 格式化菜品名称，最多显示INT_DISH_NAME_MAX_LEN个数
     *
     * @param name
     * @return
     */
    private  String formatDishName(String name) {
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        if (name.length() > INT_DISH_NAME_MAX_LEN) {
            return name.substring(0, INT_DISH_NAME_MAX_LEN) + "..";
        }
        return name;
    }

    /**
     * 新起一行，打印文字
     *
     * @param text
     * @throws IOException
     */
    public void printTextNewLine(String text) throws IOException {

        StringBuilder str = new StringBuilder("\n");
        str.append(text);

        this.printText(str.toString());
    }

    public void printLine(int lineNum) throws IOException {
        for (int i = 0; i < lineNum; i++) {
            printTextNewLine(" ");
        }
    }

    /**
     * 打印换行(只换一行)
     *
     * @throws IOException
     */
    public void printLine() throws IOException {
       // byte[] content = s.getBytes(charsetName);
        this.printText("\n");
    }

    public  boolean getState(){
        if(myPrinterPort != null)
           return myPrinterPort.getState();
        return false;
    }
}
