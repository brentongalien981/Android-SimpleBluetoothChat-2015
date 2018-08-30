package com.example.bluetoothchatsimplified;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

public class ChatMessagesManagerMini {
	
	private Handler mHandler;

	public ChatMessagesManagerMini() {
		
		mHandler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				BtNetManagerMini tempBtNetManagerMini = (BtNetManagerMini) msg.obj;
				TextView tv = tempBtNetManagerMini.getTv();
				tv.setText("" + tempBtNetManagerMini.getReceivedMsg());
			}
		};
	}
	
	public void handleUpdate(int flag, BtNetManagerMini btNetManagerMini) {
		Message completeMessage = mHandler.obtainMessage(flag, btNetManagerMini);
        completeMessage.sendToTarget();

	}
}
