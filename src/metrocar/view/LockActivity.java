package metrocar.view;

import metrocar.engine.LockEngine;
import metrocar.labels.HandlerLabels;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LockActivity extends Activity {

	private static final String tag = "Metrocar";

	private Button reset;
	private Button test;
	private Button open;
	private Button close;
	private Button set;
	private CheckBox use;
	private TextView name;
	private EditText oldPass;
	private EditText newPass;
	private EditText setPass;
	private LockEngine lock;
	private BluetoothDevice device;
	private String pass;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_lock);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		SharedPreferences settings = getSharedPreferences("my", 0);
		String address = settings.getString("LockAddress", "");
		String nameLock = settings.getString("LockName", "");
		boolean useLock = settings.getBoolean("LockEnable", false);
		pass = settings.getString("LockPass", "");

		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!btAdapter.isEnabled())
			btAdapter.enable();

		device = btAdapter.getRemoteDevice(address);

		use = (CheckBox) findViewById(R.id.useLock);
		use.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences settings = getSharedPreferences("my", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("LockEnable", use.isChecked());
				editor.commit();
			}
		});
		use.setChecked(useLock);

		name = (TextView) findViewById(R.id.textview_lock_name);
		name.setText(nameLock);

		oldPass = (EditText) findViewById(R.id.edittext_old_pass);
		newPass = (EditText) findViewById(R.id.edittext_new_pass);
		setPass = (EditText) findViewById(R.id.edittext_set_pass);
		setPass.setText(pass);

		lock = new LockEngine(this, mHandler, device, pass, true);
		lock.start();

		open = (Button) findViewById(R.id.button_open);
		open.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				lock.open();
			}
		});
		close = (Button) findViewById(R.id.button_close);
		close.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				lock.close();
			}
		});

		set = (Button) findViewById(R.id.button_set);
		set.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences settings = getSharedPreferences("my", 0);
				SharedPreferences.Editor editor = settings.edit();
				pass = setPass.getText().toString();
				Toast.makeText(getApplicationContext(),
						"Lock password: " + pass, Toast.LENGTH_SHORT).show();
				editor.putString("LockPass", pass);
				editor.commit();
				setPass.setText(pass);

			}
		});

		reset = (Button) findViewById(R.id.button_reset);
		reset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(tag, "resetActivity");
				String olde = oldPass.getText().toString();
				String newe = newPass.getText().toString();
				if (olde.equals("") || newe.equals("")) {
					Toast.makeText(getApplicationContext(), "Set both fields",
							Toast.LENGTH_SHORT).show();
					return;
				}
				lock.reset(olde, newe);
				SharedPreferences settings = getSharedPreferences("my", 0);
				SharedPreferences.Editor editor = settings.edit();
				pass = newPass.getText().toString();
				editor.putString("LockPass", pass);
				editor.commit();
				oldPass.setText("");
				newPass.setText("");
				setPass.setText(pass);

			}
		});
		test = (Button) findViewById(R.id.button_test);
		test.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				lock.test(pass);
			}
		});
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerLabels.LOCK:
				switch (msg.arg1) {
				case HandlerLabels.LOCK_TEST_OK:
					Toast.makeText(getApplicationContext(), "Test OK",
							Toast.LENGTH_SHORT).show();
					break;
				case HandlerLabels.LOCK_CONNECTED:
					Toast.makeText(getApplicationContext(), "Connected",
							Toast.LENGTH_SHORT).show();
					break;
				case HandlerLabels.LOCK_CONNECTION_LOST:
					Toast.makeText(getApplicationContext(), "Connection Lost",
							Toast.LENGTH_SHORT).show();
					onBackPressed();
					break;
				case HandlerLabels.LOCK_CONNECTION_FAILED:
					Toast.makeText(getApplicationContext(),
							"Connection Failed", Toast.LENGTH_SHORT).show();
					onBackPressed();
					break;
				case HandlerLabels.LOCK_TEST_FAILED:
					Toast.makeText(getApplicationContext(), "Test Failed",
							Toast.LENGTH_SHORT).show();
					break;
				case HandlerLabels.LOCK_RESET_OK:
					Toast.makeText(getApplicationContext(), "Reset Ok",
							Toast.LENGTH_SHORT).show();
					break;
				case HandlerLabels.LOCK_RESET_FAILED:
					Toast.makeText(getApplicationContext(), "Reset Failed",
							Toast.LENGTH_SHORT).show();
					break;
				case HandlerLabels.LOCK_UNKNOWN_RESPONCE:
					Toast.makeText(getApplicationContext(), "Unknown responce",
							Toast.LENGTH_SHORT).show();
					break;
				}
				break;
			}

		}
	};

	@Override
	public void onBackPressed() {
		if (lock != null) {
			lock.stop();
		}
		super.onBackPressed();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(tag, "on pause");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(tag, "on resume");
	}

}
