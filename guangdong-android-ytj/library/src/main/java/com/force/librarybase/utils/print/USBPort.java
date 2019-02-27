package com.force.librarybase.utils.print;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author chenlei
 * @version v1.0
 * @Package com.force.librarybase.utils.print
 * @Description: 类描述
 * @date 2017/11/15 17:47
 */
public class USBPort implements BasePrinterPort {

    private Context mContext;
    private final UsbManager mUsbManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mConnection;
    private UsbEndpoint mUsbEndpoint;
    private PendingIntent mPendingIntent = null;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("com.android.example.USB_PERMISSION".equals(action)) {
                synchronized(this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra("device");
                    boolean fromUser = intent.getBooleanExtra("permission", false);
                    if(fromUser) {
                        if(device != null) {
                            USBPort.this.findPrintDevice(device);
                        } else {
                            USBPort.this.close();
                        }
                    }
                }
            }

        }
    };

    public USBPort(Context mContext){
        this.mContext = mContext;
        this.mUsbManager = (UsbManager)mContext.getSystemService(Context.USB_SERVICE);
        this.mPendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
        IntentFilter mFilter = new IntentFilter("com.android.example.USB_PERMISSION");
        mFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        mFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        mContext.registerReceiver(this.mReceiver, mFilter);
    }

    static USBPort mUSBPort = null;
    public static USBPort getInstance(Context mContext) {
        if(mUSBPort == null) {
            mUSBPort = new USBPort(mContext);
        }

        return mUSBPort;
    }

    @Override
    public boolean open() {
        HashMap deviceList;
        Iterator deviceIterator;
        UsbDevice device;
        if(this.mDevice != null) {
            this.findPrintDevice(this.mDevice);
            if(this.mConnection == null) {
                deviceList = this.mUsbManager.getDeviceList();
                deviceIterator = deviceList.values().iterator();

                while(deviceIterator.hasNext()) {
                    device = (UsbDevice)deviceIterator.next();
                    this.mUsbManager.requestPermission(device, this.mPendingIntent);
                }
            }
        } else {
            deviceList = this.mUsbManager.getDeviceList();
            deviceIterator = deviceList.values().iterator();

            while(deviceIterator.hasNext()) {
                device = (UsbDevice)deviceIterator.next();
                this.mUsbManager.requestPermission(device, this.mPendingIntent);
            }
        }

        Log.d("USBPort", "--time--end--" + System.currentTimeMillis());
        return true;
    }

    @Override
    public void close() {
        if(this.mConnection != null) {
            this.mConnection.close();
            this.mConnection = null;
            this.mContext.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public int write(byte[] var1) {
        {
            synchronized(this) {
                int length = -1;
                if(this.mConnection != null) {
                    length = this.mConnection.bulkTransfer(this.mUsbEndpoint, var1, var1.length, 10000);
                }

                int mResult;
                if(length < 0) {
                    mResult = -1;
                    Log.i("USBPort", "fail in send！" + length);
                    this.open();
                } else {
                    mResult = var1.length;
                    Log.i("USBPort", "send" + length + "byte data");
                }

                return mResult;
            }
        }
    }

    @Override
    public int read(byte[] var1) {
        return 0;
    }

    private void findPrintDevice(UsbDevice device) {
        if(device != null) {
            UsbInterface intf = null;
            UsbEndpoint ep = null;
            int InterfaceCount = device.getInterfaceCount();
            this.mDevice = device;

            int j;
            for(j = 0; j < InterfaceCount; ++j) {
                intf = device.getInterface(j);
                if(intf.getInterfaceClass() == 7) {
                    int UsbEndpointCount = intf.getEndpointCount();

                    int i;
                    for(i = 0; i < UsbEndpointCount; ++i) {
                        ep = intf.getEndpoint(i);
                        if(ep.getDirection() == 0 && ep.getType() == 2) {
                            break;
                        }
                    }

                    if(i != UsbEndpointCount) {
                        break;
                    }
                }
            }

            if(j == InterfaceCount) {
                Log.i("USBPort", "No printer interface!");
                return;
            }

            this.mUsbEndpoint = ep;
            if(device != null) {
                UsbDeviceConnection connection = this.mUsbManager.openDevice(device);
                if(connection != null && connection.claimInterface(intf, true)) {
                    Log.i("USBPort", "Open success！");
                    this.mConnection = connection;
                } else {
                    Log.i("USBPort", "Open failed！");
                    this.mConnection = null;
                }
            }
        }
    }

    public boolean getState() {
        if(this.mDevice != null && this.mConnection != null )
            return true;
        else
            return false;
    }
}
