package com.roboceptionist.bluetooth;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class AcceptBTT extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private BluetoothAdapter btAdapter;
    private Context context;
    private View view;
 
    @SuppressLint("NewApi")
	public AcceptBTT(BluetoothAdapter a, Context b, View c) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        btAdapter = a;
        context = b;
        view = c;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = btAdapter.listenUsingRfcommWithServiceRecord(BTTConstants.name, BTTConstants.id);
            Toast.makeText(context, "UUID =" + String.valueOf(BTTConstants.id), Toast.LENGTH_SHORT).show();
        } catch (IOException e) { Toast.makeText(context, "never madde UUID", Toast.LENGTH_SHORT).show();}
        mmServerSocket = tmp;
        ////run();
    }
 
    public void run() {
        BluetoothSocket socket = null;
        Toast.makeText(context, "testing", Toast.LENGTH_SHORT).show();
        // Keep listening until exception occurs or a socket is returned
        int i =0;
        while (true) {
            try {
            	Toast.makeText(context, "trying to connect "+ i, Toast.LENGTH_SHORT).show();
                socket = mmServerSocket.accept();
            } catch (IOException e) {
            	Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                try {
                	Toast.makeText(context, "bluetooth connection accepted", Toast.LENGTH_SHORT).show();
					mmServerSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                //ManageBTT manager = new ManageBTT(socket,context,view);
                //manager.run();
                break;
            }
            i++;
        }
    }
 
    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}