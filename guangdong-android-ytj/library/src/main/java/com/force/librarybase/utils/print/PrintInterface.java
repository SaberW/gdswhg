package com.force.librarybase.utils.print;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yijiangtao
 * @version v1.0
 * @Package com.daydao.caterers.fastfood.utils.print
 * @Description: 打印机通用输出流接口类
 * @date 2017/6/5
 */

public interface PrintInterface {

    /**
     * 关闭的方法
     */
    void close() throws IOException;

    /**
     * 获取向打印机输出流的方法
     */
    OutputStream getOutputStream();

    InputStream getInputStream();
}
