package com.force.librarybase.utils.print;

import android.text.TextUtils;

import com.force.librarybase.utils.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yijiangtao
 * @version v1.0
 * @Package com.daydao.caterers.fastfood.utils.print
 * @Description: 串口打印机通用输出流操作类
 * @date 2017/6/5
 */

public class SerialPortPrinter implements PrintInterface {

    private static  final String TAG = "SerialPortPrinter";

    private SerialPort mSerialPort;

    private OutputStream mOutputStream;
    private InputStream mInputStream;

    public SerialPortPrinter(String deviceAdd, int baudrate, int flags){
        Logger.d( "deviceAdd = " + deviceAdd+"                baudrate = " + baudrate);
        try {
            if(TextUtils.isEmpty(deviceAdd)){
                deviceAdd = "/dev/ttyS1";
            }
            if(0 == baudrate){
                baudrate = 9600;
            }

            File device = new File(deviceAdd);
            mSerialPort = new SerialPort(device,baudrate,flags);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if(null != mSerialPort){
            mSerialPort.close();
        }
        if(null != mOutputStream){
            mOutputStream.close();
        }
    }

    @Override
    public OutputStream getOutputStream() {
        return mOutputStream;
    }

    @Override
    public InputStream getInputStream() {
        return mInputStream;
    }
}
