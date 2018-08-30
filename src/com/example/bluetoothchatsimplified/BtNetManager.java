package com.example.bluetoothchatsimplified;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class BtNetManager {
	
    private int mState;
    
    public static final int STATE_NONE = 0;       
    public static final int STATE_LISTEN = 1;     
    public static final int STATE_CONNECTING = 2; 
    public static final int STATE_CONNECTED = 3;  
    
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;

    private static final String NAME = "My Simple Chat App";
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	private BluetoothAdapter btAdapter;
	private TextView tv;
	private NewActivityLauncher newActivityLauncher;
	private MainActivity mainActivityInstance;

	private static BtNetManager myInstance;
	
	// This has to be static because there is a weird and
	// INTERESTING runtime error if not.
	public static BluetoothSocket ourSocket;

	// constructor
	public BtNetManager(MainActivity mainActivityInstance, NewActivityLauncher newActivityLauncher, TextView tv) {
		
		this.mainActivityInstance = mainActivityInstance;
		this.newActivityLauncher = newActivityLauncher;
		myInstance = this;
		this.tv = tv;
		btAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	private BtNetManager getBtNetManager() {
		return myInstance;
	}	
	
	public void createServer() {
		mAcceptThread = new AcceptThread();
		mAcceptThread.start();
	}
	
	public void connectToServer(BluetoothDevice device) {
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
	}
        
	// inner class for client
	private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmpBtS = null;
            
            try {
            	tv.setText("connecting to server");
                tmpBtS = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {}
            
            mmSocket = tmpBtS;
            tv.setText("connected to server");
        }
        
        public void run() {
            setName("ConnectThread");
            
            // Always cancel discovery because it will slow down a connection
            btAdapter.cancelDiscovery();
            
            // Make a connection to the BluetoothSocket
            try {
                mmSocket.connect();
            } 
            catch (IOException e) {
                try { mmSocket.close(); }
                catch (IOException e2) {}
                return;
            }
            mConnectThread = null;
            
            ourSocket = mmSocket;
        	newActivityLauncher.handleUpdate(0, getBtNetManager());
        }
        
        public void cancel() {
            try {mmSocket.close();}
            catch (IOException e) {}
        }
    }
	
	// inner class for server
	private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        
        public AcceptThread() {
            BluetoothServerSocket tmpBtSS = null;
            try {
            	// this waits
            	tv.setText("waiting for the client to connect");
                tmpBtSS = btAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } 
            catch (IOException e) { ; }
            
            mmServerSocket = tmpBtSS;
        }
        
        public void run() {
            setName("AcceptThread");
            BluetoothSocket socket = null;
            
            // wait for client to connect
            while (mState != STATE_CONNECTED) {
                try {
                    socket = mmServerSocket.accept(); // this method waits and return a socket
                }
                catch (IOException e) { break; }
                
                if (socket != null) { 
                	mAcceptThread = null;
                	
                	ourSocket = socket;
                	newActivityLauncher.handleUpdate(0, getBtNetManager());
                }
            }
        }
        
        public void cancel() {
            try { mmServerSocket.close(); }
            catch (IOException e) {}
        }
    }

	public MainActivity getMainActivityInstance() {
		return mainActivityInstance;
	}
}
