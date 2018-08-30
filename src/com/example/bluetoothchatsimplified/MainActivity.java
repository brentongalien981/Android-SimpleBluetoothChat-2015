package com.example.bluetoothchatsimplified;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements Button.OnClickListener {

	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
		
	private BluetoothAdapter bluetoothAdapter;
	private Button bCreate, bConnect;
	private TextView tv1;
	private BtNetManager btNetMgr;
	private NewActivityLauncher newActivityLauncher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// FOR DEBUG
		tv1 = (TextView) findViewById(R.id.textView1);
		
		newActivityLauncher = new NewActivityLauncher();
		setBluetooth();
		makeBtDiscoverable();
		setButtons();
	}	

	private void setButtons() {
		bCreate = (Button) findViewById(R.id.button_create);
		bConnect = (Button) findViewById(R.id.button_connect);

		bCreate.setOnClickListener(this);
		bConnect.setOnClickListener(this);
	}


	private void setBluetooth() {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		btNetMgr = new BtNetManager(this, newActivityLauncher, tv1);
		
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}		
	}

	private void makeBtDiscoverable() {
		if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
				// Attempt to connect to the device
				btNetMgr.connectToServer(device);
			}
			break;
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_create:
			btNetMgr.createServer();
			break;
		case R.id.button_connect:
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			break;
		}
		
	}

	public void sendToGame(BluetoothSocket socket) {
    	Intent intent = new Intent(this, ChatRoom.class);
    	
    	OurBluetoothNetwork  ourBluetoothNetwork = new OurBluetoothNetwork();
    	ourBluetoothNetwork.setBtSocket(socket);
    	
    	intent.putExtra("socket", ourBluetoothNetwork);
    	startActivity(intent);		
	}

}
