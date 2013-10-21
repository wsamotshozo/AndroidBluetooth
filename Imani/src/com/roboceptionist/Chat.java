package com.roboceptionist;

import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.roboceptionist.bluetooth.AcceptBTT;
import com.roboceptionist.bluetooth.ConnectBTT;
import com.roboceptionist.model.BTDevice;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Chat extends Activity{
	private BluetoothAdapter btAdapter;
	private ArrayList<String> deviceList= new ArrayList<String>();
	private ArrayList<BTDevice> deviceInfo = new ArrayList<BTDevice>();
	private ArrayAdapter btDeviceList;/*= new ArrayAdapter<String>(
            Chat.this,
            android.R.layout.select_dialog_singlechoice);*/
	private final static int REQUEST_ENABLE_BT = 1;
	final Context context = this;
	private TextView result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatactivity);
		
		result = (TextView)findViewById(R.id.Conversation);
		
		 btAdapter = BluetoothAdapter.getDefaultAdapter();
		 if (btAdapter == null) {
			   Toast.makeText(this, "there is no bluetooth adapter", Toast.LENGTH_SHORT);
		 }
		 if (!btAdapter.isEnabled()) {
			    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		 }
		 Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		    	deviceList.add(device.getName() + " ][ "+ device.getAddress());
		    	deviceInfo.add(new BTDevice(device.getName(),device.getAddress()));
		    	//btDeviceList.add(device.getName() + " ][ "+ device.getAddress());
		    }
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}
	
    public boolean onOptionsItemSelected(MenuItem item) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.devicelist);
		ListView list = (ListView)dialog.findViewById(R.id.possiblelist);
        switch (item.getItemId()) {
        case R.id.scan:
            // Launch the DeviceListActivity to see devices and do scan
            //Intent serverIntent = new Intent(this, DeviceListActivity.class);
            //startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			dialog.setTitle("Pick the device you want to use");
			btDeviceList = new ArrayAdapter<String>(
		            dialog.getContext(),
		            android.R.layout.select_dialog_item,deviceList);
			//Toast.makeText(dialog.getContext(), deviceList.toString(),Toast.LENGTH_LONG).show();
			list.setAdapter(btDeviceList);
			list.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
	                    long arg3) {
	               /* String fname = main_genral_class.file_list.get(pos);*/
	            	
	            	String deviceName = deviceInfo.get(pos).macaddress;
	            	ConnectBTT start = new ConnectBTT(btAdapter.getRemoteDevice(deviceName),btAdapter,context, result);
	                start.run();
	            	dialog.dismiss();

	            }
	        });
			dialog.show();
			return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            //ensureDiscoverable();

            AcceptBTT accept = new AcceptBTT(btAdapter,context,result);
        	accept.run();
        	//start.run();
            return true;
        case R.id.pair:
			final Dialog pdialog = new Dialog(context);
			pdialog.setContentView(R.layout.devicelist);
			pdialog.setTitle("Pick the device you want to use");
			btDeviceList = new ArrayAdapter<String>(
		            pdialog.getContext(),
		            android.R.layout.select_dialog_item,deviceList);
			//Toast.makeText(dialog.getContext(), deviceList.toString(),Toast.LENGTH_LONG).show();
			list.setAdapter(btDeviceList);
			list.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
	                    long arg3) {

	                pdialog.dismiss();

	            }
	        });
			dialog.show();
        	return true;
        }
        return false;
    }
    private OnItemClickListener onClick = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
        }
    };
}
