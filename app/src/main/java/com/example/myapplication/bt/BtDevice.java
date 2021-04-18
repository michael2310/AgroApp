package com.example.myapplication.bt;

import android.bluetooth.BluetoothDevice;

public class BtDevice {
   final  BluetoothDevice device;

    public BtDevice(BluetoothDevice device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return device.getName() + "\n" + device.getAddress();
    }
}
