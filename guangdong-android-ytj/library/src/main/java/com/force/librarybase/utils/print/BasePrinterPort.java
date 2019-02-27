package com.force.librarybase.utils.print;

/**
 * @author chenlei
 * @version v1.0
 * @Package com.force.librarybase.utils.print
 * @Description: 类描述
 * @date 2017/11/15 17:42
 */
public interface BasePrinterPort {
    boolean open();

    void close();

    int write(byte[] var1);

    int read(byte[] var1);

    boolean getState();
}
