package com.roboceptionist.bluetooth;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class ConnectBTT extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private BluetoothAdapter btAdapter;
    private Context context;
    private View view;
 
    @SuppressLint("NewApi")
	public ConnectBTT(BluetoothDevice device, BluetoothAdapter a, Context b, View c) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;
        btAdapter = a;
        context = b;
        view = c;
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(BTTConstants.id);
        } catch (IOException e) { }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        btAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
        	Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();
        	Toast.makeText(context, connectException.getMessage(), Toast.LENGTH_LONG).show();
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
        Toast.makeText(context, "Connection successful", Toast.LENGTH_SHORT).show();
        //ManageBTT manager = new ManageBTT(mmSocket,context, view);
        //manager.run();
    }
 
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
