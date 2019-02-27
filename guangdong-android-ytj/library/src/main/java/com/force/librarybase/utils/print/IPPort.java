package com.force.librarybase.utils.print;

import com.force.librarybase.utils.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author chenlei
 * @version v1.0
 * @Package com.force.librarybase.utils.print
 * @Description: 类描述
 * @date 2017/11/16 9:16
 */
public class IPPort implements BasePrinterPort {

    private Socket mSock = null;
    // 通过socket流进行读写
    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;
    private String ip;
    int port;


    public IPPort(String ip, int port) {
        Logger.e( "ip = " + ip + "     port = " + port);
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean open() {
        try {
            mSock = new Socket(ip, port);
            mOutputStream = mSock.getOutputStream();
            mInputStream = mSock.getInputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() {
        try {
            if (null != mSock) {
                mSock.close();
            }
            if (null != mOutputStream) {
                mOutputStream.close();
            }
            if(null != mInputStream){
                mInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int write(byte[] var1) {

        try{
            mOutputStream.write(var1);
            mOutputStream.flush();
        }catch (IOException  e){
            e.printStackTrace();
            return -3;
        }catch (NullPointerException  e){
            e.printStackTrace();
            return -3;
        }

        return var1.length;
    }

    @Override
    public int read(byte[] var1) {
        return 0;
    }

    @Override
    public boolean getState() {
        return false;
    }
}
