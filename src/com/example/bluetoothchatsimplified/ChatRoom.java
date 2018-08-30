package com.example.bluetoothchatsimplified;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatRoom extends Activity implements Button.OnClickListener {

	private TextView tv1;
	private EditText et1;
	private Button b1;
	private OurBluetoothNetwork ourBluetoothNetwork;
	private static BluetoothSocket socket;
	private BtNetManagerMini btNetManagerMini;
	private ChatMessagesManagerMini chatMessagesManagerMini;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_room);

		setViews();
		setSocket();
		
		chatMessagesManagerMini = new ChatMessagesManagerMini();
		btNetManagerMini = new BtNetManagerMini(tv1, socket, chatMessagesManagerMini);
	}

	private void setSocket() {
		Intent intent = getIntent();
		ourBluetoothNetwork = (OurBluetoothNetwork) intent.getSerializableExtra("socket");
		socket = ourBluetoothNetwork.getBtSocket();
	}
	
	private void setViews() {
		tv1 = (TextView) findViewById(R.id.textView_cr);	tv1.setText("socket created dau ");
		et1 = (EditText) findViewById(R.id.editText_cr);
		b1 = (Button) findViewById(R.id.button_cr);

		b1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_cr:
			btNetManagerMini.write(et1.getText().toString());
			break;
		}		
	}
}
