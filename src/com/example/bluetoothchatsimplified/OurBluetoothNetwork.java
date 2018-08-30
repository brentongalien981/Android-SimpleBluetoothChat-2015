package com.example.bluetoothchatsimplified;

import java.io.Serializable;
import android.bluetooth.BluetoothSocket;

public class OurBluetoothNetwork implements Serializable {

	private static BluetoothSocket btSocket;
	private static final long serialVersionUID = 123L;
	
	// constructor
	public OurBluetoothNetwork() {
		//btSocket = null;
	}
	
	public void setBtSocket(BluetoothSocket btSocket) {
		this.btSocket = btSocket;
	}
	
	public BluetoothSocket getBtSocket() {
		return btSocket;
	}
}
