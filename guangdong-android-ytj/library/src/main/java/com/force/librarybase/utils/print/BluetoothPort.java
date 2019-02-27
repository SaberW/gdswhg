package com.force.librarybase.utils.print;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author chenlei
 * @version v1.0
 * @Package com.force.librarybase.utils.print
 * @Description: 类描述
 * @date 2017/11/15 17:41
 */
public class BluetoothPort implements BasePrinterPort {
    private static final String TAG = "BluetoothPort";
    private String mDeviceAddress;
    private BluetoothSocket mSocket;
    private BluetoothAdapter mAdapter;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private int mState;
    private BluetoothDevice mDevice;
    private final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    static BluetoothPort mBluetoothPort;


    public static BluetoothPort getInstance(String address) {
        if(mBluetoothPort == null) {
            mBluetoothPort = new BluetoothPort(address);
        }
        return mBluetoothPort;
    }

/*    public BluetoothPort(BluetoothDevice bluetoothDevice) {
        this.mDevice = bluetoothDevice;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mState = 103;
    }*/

    public BluetoothPort(String address) {
        try {
            this.mAdapter = BluetoothAdapter.getDefaultAdapter();
            this.mDevice = this.mAdapter.getRemoteDevice(address);
            this.mState = 103;
        }catch (Exception e){
            Log.i(TAG, "BluetoothPort: 请检查蓝牙连接");
        }
    }

    public boolean open() {
        boolean isConnected = false;
        if (this.mState != 103) {
            this.close();
        }

        isConnected = this.connect2Device();
        return isConnected;
    }

    public void close() {
        try {
            if (this.mSocket != null) {
                this.mSocket.close();
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }

        this.mDevice = null;
        this.mSocket = null;
        if (this.mState != 102) {
         //  this.setState(103);
        }

    }

    public int write(byte[] srcData) {
        try {
            if (outputStream == null) {
                return -3;
            }

            outputStream.write(srcData);
            outputStream.flush();
        } catch (IOException var3) {
            var3.printStackTrace();
            return -1;
        } catch (Exception var4) {
            var4.printStackTrace();
            return -2;
        }

        return srcData.length;
    }

    public int read(byte[] buffer) {
        int readLen = -1;

        try {
            if (inputStream != null && (readLen = inputStream.available()) > 0) {
                inputStream.read(buffer);
            }

            return readLen;
        } catch (IOException var4) {
            var4.printStackTrace();
            return -1;
        } catch (Exception var5) {
            var5.printStackTrace();
            return -1;
        }
    }

    public int read(int timeout, byte[] buffer) {
        int readLen = -1;

        try {
            while ((readLen = inputStream.available()) <= 0) {
                timeout -= 50;
                if (timeout <= 0) {
                    break;
                }

                try {
                    Thread.sleep(50L);
                } catch (InterruptedException var5) {
                    var5.printStackTrace();
                }
            }

            if (readLen > 0) {
                buffer = new byte[readLen];
                inputStream.read(buffer);
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return readLen;
    }

    private boolean connect2Device() {
        boolean hasError = false;
        if (this.mAdapter.isDiscovering()) {
            this.mAdapter.cancelDiscovery();
        }

        try {
            this.mSocket = this.mDevice.createRfcommSocketToServiceRecord(this.PRINTER_UUID);
            this.mSocket.connect();
        } catch (IOException var9) {
            var9.printStackTrace();

            try {
                if (this.mSocket != null) {
                    this.mSocket.close();
                }

                Thread.sleep(2000L);
            } catch (IOException var7) {
                var7.printStackTrace();
            } catch (InterruptedException var8) {
                var8.printStackTrace();
            }

            hasError = this.ReTryConnect();
        } catch (Exception var10) {
            try {
                if (this.mSocket != null) {
                    this.mSocket.close();
                }

                Thread.sleep(2000L);
            } catch (IOException var5) {
                var5.printStackTrace();
            } catch (InterruptedException var6) {
                var6.printStackTrace();
            }

            hasError = this.ReTryConnect();
        }

        if (!hasError) {
            try {
                inputStream = this.mSocket.getInputStream();
                outputStream = this.mSocket.getOutputStream();
            } catch (IOException var4) {
                hasError = true;
                var4.printStackTrace();
            }
        }

        if (hasError) {
           // this.setState(102);
            mState = 102;
            this.close();
        } else {
            mState = 101;
            //this.setState(101);
        }

        return !hasError;
    }

    @SuppressLint({"NewApi"})
    private boolean ReTryConnect() {
        try {
            if (Build.VERSION.SDK_INT >= 10) {
                if( this.mDevice != null)
                this.mSocket = this.mDevice.createInsecureRfcommSocketToServiceRecord(this.PRINTER_UUID);
            } else {
                Method e = this.mDevice.getClass().getMethod("createRfcommSocket", new Class[]{Integer.TYPE});
                this.mSocket = (BluetoothSocket) e.invoke(this.mDevice, new Object[]{Integer.valueOf(1)});
            }

            this.mSocket.connect();
            return false;
        } catch (Exception var4) {
            if (this.mSocket != null) {
                try {
                    this.mSocket.close();
                } catch (IOException var3) {
                    var3.printStackTrace();
                }
            }

            var4.printStackTrace();
            return true;
        }
    }
    public boolean getState() {
        if(mState == 101 )
            return true;
        else
            return false;
    }

/*    private synchronized void setState(int state) {
        if (this.mState != state) {
            this.mState = state;
            if (this.mHandler != null) {
                this.mHandler.obtainMessage(this.mState).sendToTarget();
            }
        }

    }*/
}
