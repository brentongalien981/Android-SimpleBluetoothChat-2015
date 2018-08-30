package com.example.bluetoothchatsimplified;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.widget.TextView;

public class BtNetManagerMini {

	private String receivedMsg;
	private ConnectedThread mConnectedThread;
	private ChatMessagesManagerMini chatMessagesManagerMini;
	private BtNetManagerMini btNetManagerMiniInstance;
	private TextView tv;

	public BtNetManagerMini(TextView tv, BluetoothSocket socket, ChatMessagesManagerMini chatMessagesManagerMini) {
		this.tv = tv;
		this.chatMessagesManagerMini = chatMessagesManagerMini;
		btNetManagerMiniInstance = this;
		
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();
		
	}
	
	public TextView getTv() {
		return tv;
	}

	public void write(String message) {
		mConnectedThread.write(message.getBytes());
	}

	public String getReceivedMsg() {
		return receivedMsg;
	}
	
	public void setReceivedMsg(String msg) {
		receivedMsg = msg;
	}
	
	// inner thread class
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024];
			int bytes; // Is this the length of the buffer?
			while (true) {
				try {
					bytes = mmInStream.read(buffer);
					String message = new String(buffer, 0, bytes);
					setReceivedMsg(message);

					chatMessagesManagerMini.handleUpdate(0, btNetManagerMiniInstance);
				} catch (IOException e) {
					break;
				}
			}
		}

		public void write(byte[] buffer) {
			try { mmOutStream.write(buffer); } 
			catch (IOException e) {}
		}

		public void cancel() {
			try { mmSocket.close(); }
			catch (IOException e) {}
		}
	}
}
