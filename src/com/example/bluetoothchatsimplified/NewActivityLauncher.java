package com.example.bluetoothchatsimplified;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

public class NewActivityLauncher {

	private Handler mHandler;

	public NewActivityLauncher() {
		
		mHandler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				BtNetManager tempBt = (BtNetManager) msg.obj;
				tempBt.getMainActivityInstance().sendToGame(tempBt.ourSocket);
			}
		};
	}
	
	public void handleUpdate(int flag, BtNetManager btNetManager) {
		Message completeMessage = mHandler.obtainMessage(flag, btNetManager);
        completeMessage.sendToTarget();

	}
}
