package metrocar.view;

import metrocar.gps.GPSTracker;
import metrocar.main.MainCore;
import metrocar.internet.CheckInternetConnection;
import metrocar.labels.HandlerLabels;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ControlActivity extends Activity {

	private static final String tag = "Metrocar";

	private Button cont;
	private Button test;
	private Button back;
	private TextView lockName;
	private TextView lockPassword;
	private TextView obdName;
	private TextView obdUsername;
	private TextView obdPassword;
	private TextView internet;
	private TextView gps;
	private TextView bluetooth;
	private TextView testLock;
	private TextView testOBD2;
	private TextView testGET;

	private String nameLock;
	private String passLock;
	private String addressLock;
	private String nameObd;
	private String passObd;
	private String usernameObd;
	private String addressObd;

	private boolean useLock;

	private GPSTracker gpsT;
	private CheckInternetConnection cic;
	private BluetoothAdapter btAdapter;
	private MainCore mc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_control);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		bluetooth = (TextView) findViewById(R.id.textview_control_bluetooth);
		gps = (TextView) findViewById(R.id.textview_control_gps);
		internet = (TextView) findViewById(R.id.textview_control_internet);
		lockName = (TextView) findViewById(R.id.textview_control_lock_name);
		lockPassword = (TextView) findViewById(R.id.textview_control_lock_pass);
		obdName = (TextView) findViewById(R.id.textview_control_obd_name);
		obdUsername = (TextView) findViewById(R.id.textview_control_obd_username);
		obdPassword = (TextView) findViewById(R.id.textview_control_obd_pass);

		testLock = (TextView) findViewById(R.id.textview_control_test_lock);
		testOBD2 = (TextView) findViewById(R.id.textview_control_test_obd2);
		testGET = (TextView) findViewById(R.id.textview_control_test_get);

		setUp();

		cont = (Button) findViewById(R.id.button_continue);
		cont.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ConnectionActivity.class);
				startActivity(intent);
			}
		});
		back = (Button) findViewById(R.id.button_back);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
			}
		});
		test = (Button) findViewById(R.id.button_test);
		test.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				testGET.setText("");
				testLock.setText("");
				testOBD2.setText("");
				if (useLock) {
					Log.d(tag, "useLock true");
					mc = new MainCore(
							getApplicationContext(),
							(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE),
							usernameObd, passObd, addressObd, addressLock,
							passLock, mHandler);
				} else {
					Log.d(tag, "uselockFalse");
					mc = new MainCore(
							getApplicationContext(),
							(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE),
							usernameObd, passObd, addressObd, mHandler);
				}
				mc.test();
			}
		});
	}

	public void setUp() {
		SharedPreferences settings = getSharedPreferences("my", 0);
		nameLock = settings.getString("LockName", "");
		passLock = settings.getString("LockPass", "");
		nameObd = settings.getString("ObdName", "");
		passObd = settings.getString("secretKey", "");
		usernameObd = settings.getString("unitId", "");
		addressObd = settings.getString("ObdAddress", "");
		addressLock = settings.getString("LockAddress", "");
		useLock = settings.getBoolean("LockEnable", false);

		gpsT = new GPSTracker(getApplicationContext(), 0, 0, false);
		cic = new CheckInternetConnection(
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
		btAdapter = BluetoothAdapter.getDefaultAdapter();

		obdPassword.setText(passObd);
		obdUsername.setText(usernameObd);
		obdName.setText(nameObd);
		lockPassword.setText(passLock);
		lockName.setText(nameLock);

		if (btAdapter.isEnabled()) {
			bluetooth.setText("Enabled");
		} else {
			bluetooth.setText("Disabled");
		}
		if (cic.isNetworkAvailable()) {
			internet.setText("Enabled");
		} else {
			internet.setText("Disabled");
		}
		if (gpsT.isEnabled()) {
			gps.setText("Enabled");
		} else {
			gps.setText("Disabled");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		setUp();
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerLabels.GET:
				testGET.setText((String) msg.obj);
				break;
			case HandlerLabels.LOCK:
				testLock.setText((String) msg.obj);
				break;
			case HandlerLabels.OBD:
				testOBD2.setText((String) msg.obj);
				break;
			}

		}
	};

}
