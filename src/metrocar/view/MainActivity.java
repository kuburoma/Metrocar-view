package metrocar.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private static final String tag = "Metrocar";

	private Button startRecording;
	private Button settings;
	private Button bluetoothSettings;
	private Button lockBluetoothSettings;
	private Button database;
	private Button reservation;
	private Button lock;
	private Button log;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		startRecording = (Button) findViewById(R.id.start_recording);
		startRecording.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startRecording();
			}
		});

		bluetoothSettings = (Button) findViewById(R.id.bluetooth_setting);
		bluetoothSettings.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				bluetoothSetting();
			}
		});

		database = (Button) findViewById(R.id.database);
		database.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				database();
			}
		});

	}

	public void startRecording() {
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!btAdapter.isEnabled()) {
			btAdapter.enable();
			return;
		}
		Intent intent = new Intent(this, ConnectionActivity.class);
		startActivity(intent);
	}


	public void bluetoothSetting() {
		Intent intent = new Intent(this, BluetoothActivity.class);
		startActivity(intent);
	}
	
	public void database() {
		Intent intent = new Intent(this, RideRecord.class);
		startActivity(intent);
	}

}
