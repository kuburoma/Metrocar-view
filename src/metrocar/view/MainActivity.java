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

		settings = (Button) findViewById(R.id.settings);
		settings.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				settings();
			}
		});

		bluetoothSettings = (Button) findViewById(R.id.bluetooth_setting);
		bluetoothSettings.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				bluetoothSetting();
			}
		});
		lockBluetoothSettings = (Button) findViewById(R.id.lock_bluetooth_setting);
		lockBluetoothSettings.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				lockBluetoothSetting();
			}
		});

		database = (Button) findViewById(R.id.database);
		database.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				database();
			}
		});

		reservation = (Button) findViewById(R.id.reservation);
		reservation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				reservation();
			}
		});
		lock = (Button) findViewById(R.id.lock);
		lock.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				lock();
			}
		});
		lock = (Button) findViewById(R.id.settings_txt);
		lock.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				txt();
			}
		});
	}

	public void startRecording() {
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!btAdapter.isEnabled()) {
			btAdapter.enable();
			return;
		}
		Intent intent = new Intent(this, ControlActivity.class);
		startActivity(intent);
	}

	public void settings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void bluetoothSetting() {
		Intent intent = new Intent(this, BluetoothActivity.class);
		startActivity(intent);
	}
	
	public void lockBluetoothSetting() {
		Intent intent = new Intent(this, LockBluetoothActivity.class);
		startActivity(intent);
	}

	public void database() {
		Intent intent = new Intent(this, RideRecord.class);
		startActivity(intent);
	}

	public void reservation() {
		Intent intent = new Intent(this, ReservationActivity.class);
		startActivity(intent);
	}
	
	public void lock() {
		SharedPreferences settings = getSharedPreferences("my", 0);
		String address = settings.getString("LockAddress", "");
		if (address.equals("")) {
			Toast.makeText(getApplicationContext(), "Lock bluetooth wasnt set",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(this, LockActivity.class);
		startActivity(intent);
	}

	public void txt() {
		Intent intent = new Intent(this, TextSettingsActivity.class);
		startActivity(intent);
	}

}
