package com.force.librarybase.utils.print;

import com.force.librarybase.utils.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author yijiangtao
 * @version v1.0
 * @Package com.daydao.caterers.fastfood.utils.print
 * @Description: IP打印机通用输出流操作类
 * @date 2017/6/5
 */

public class IpPrinter implements PrintInterface {

    private static  final String TAG = "IpPrinter";

    private Socket mSock = null;

    // 通过socket流进行读写
    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;

    public IpPrinter(String ip, int port) throws IOException {
        Logger.d( "ip = " + ip + "                 port = " + port);
        mSock = new Socket(ip,port);
        mOutputStream = mSock.getOutputStream();
        mInputStream = mSock.getInputStream();
    }

    @Override
    public void close() throws IOException {
        if(null != mSock){
            mSock.close();
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
