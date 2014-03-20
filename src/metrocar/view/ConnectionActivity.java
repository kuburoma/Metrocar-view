package metrocar.view;

import java.util.ArrayList;
import metrocar.labels.HandlerLabels;
import metrocar.main.MainCore;
import metrocar.utils.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "HandlerLeak" })
public class ConnectionActivity extends Activity {

	private static final String tag = "Metrocar";

	private ListView listView;

	private Button loginButton;
	private EditText userText;
	private EditText passwordText;

	private MainCore mc;

	private Button stopButton;
	private Button informationListButton;
	private Button informationListButtonReturn;

	private ArrayList<String> informationList = new ArrayList<String>();
	private ArrayAdapter<String> arrayAdapter;

	private String info;

	private TextView state;
	private TextView t0105;
	private TextView t010D;
	private TextView t010C;
	private TextView t0111;
	private TextView tSpeedRequest;
	private TextView tAltitude;
	private TextView tAccuracy;
	private TextView tGPS;

	private AlertDialog.Builder builder;

	private BluetoothDevice device;

	private String message;
	private String id;
	private String secretKey;
	private String unitId;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getSharedPreferences("my", 0);
		String addressObd = settings.getString("ObdAddress", "");
		String addressLock = settings.getString("LockAddress", "");
		boolean useLock = settings.getBoolean("LockEnable", false);
		String lockPass = settings.getString("LockPass", "");

		secretKey = settings.getString("secretKey", "");
		unitId = settings.getString("unitId", "");

		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!btAdapter.isEnabled())
			btAdapter.enable();

		if (useLock) {
			mc = new MainCore(
					getApplicationContext(),
					(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE),
					unitId, secretKey, addressObd, addressLock, lockPass,
					mHandler,textSettings());
		} else {
			mc = new MainCore(
					getApplicationContext(),
					(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE),
					unitId, secretKey, addressObd, mHandler,textSettings());
		}
		if (mc.setUp()) {
			logginView();
		} else {
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
			Toast.makeText(getApplicationContext(),
					("Set all required settings"), Toast.LENGTH_LONG).show();
		}

	}

	private UnitSettings textSettings() {
		UnitSettings unit = new UnitSettings();
		SharedPreferences settings = getSharedPreferences("textSettings", 0);
		if (settings.contains("doLogGPSTracker")) {
			unit.setDoLogGPSTracker(settings.getBoolean("doLogGPSTracker",
					false));
		}
		if (settings.contains("doLogGet")) {
			unit.setDoLogGet(settings.getBoolean("doLogGet", false));
		}
		if (settings.contains("doLogPost")) {
			unit.setDoLogPost(settings.getBoolean("doLogPost", false));
		}
		if (settings.contains("doLogUnitEngine")) {
			unit.setDoLogUnitEngine(settings.getBoolean("doLogUnitEngine",
					false));
		}
		if (settings.contains("doLogMainCore")) {
			unit.setDoLogMainCore(settings.getBoolean("doLogMainCore", false));
		}
		if (settings.contains("doLogCheckInternetConnection")) {
			unit.setDoLogCheckInternetConnection(settings.getBoolean(
					"doLogCheckInternetConnection", false));
		}
		if (settings.contains("doLogLockEngine")) {
			unit.setDoLogLockEngine(settings.getBoolean("doLogLockEngine",
					false));
		}
		if (settings.contains("doLogDatabaseController")) {
			unit.setDoLogDatabaseController(settings.getBoolean(
					"doLogDatabaseController", false));
		}
		if (settings.contains("doLogObdMessage")) {
			unit.setDoLogObdMessage(settings.getBoolean("doLogObdMessage",
					false));
		}
		if (settings.contains("authorizedPostPeriod")) {
			unit.setAuthorizedPostPeriod(settings.getLong(
					"authorizedPostPeriod", 0));
		}
		if (settings.contains("authorizedGetPeriod")) {
			unit.setAuthorizedGetPeriod(settings.getLong("authorizedGetPeriod",
					0));
		}
		if (settings.contains("unauthorizedGetPeriod")) {
			unit.setUnauthorizedGetPeriod(settings.getLong(
					"unauthorizedGetPeriod", 0));
		}
		if (settings.contains("unauthorizedPostPeriod")) {
			unit.setUnauthorizedPostPeriod(settings.getLong(
					"unauthorizedPostPeriod", 0));
		}
		if (settings.contains("get_request")) {
			unit.setGet_request(settings.getString("get_request", ""));
		}
		if (settings.contains("post")) {
			unit.setPost(settings.getString("post", ""));
		}
		if (settings.contains("open_advance")) {
			unit.setOpen_advance(settings.getLong("open_advance", 0));
		}
		if (settings.contains("end_advance")) {
			unit.setEnd_advance(settings.getLong("end_advance", 0));
		}
		if (settings.contains("min_time_bw_updates")) {
			unit.setMin_time_bw_updates(settings.getLong("min_time_bw_updates",
					0));
		}
		if (settings.contains("min_distance_change_for_server")) {
			unit.setMin_distance_change_for_server(settings.getLong(
					"min_distance_change_for_server", 0));
		}
		if (settings.contains("post_limit")) {
			unit.setPost_limit(settings.getString("post_limit", ""));
		}

		return unit;
	}

	private void logginView() {
		setContentView(R.layout.activity_login);

		userText = (EditText) findViewById(R.id.edittext_login_user_name);
		passwordText = (EditText) findViewById(R.id.edittex_login_password);

		loginButton = (Button) findViewById(R.id.button_login);
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (userText.getText().toString().equals(unitId)
						&& passwordText.getText().toString().equals(secretKey)) {
					Toast.makeText(getApplicationContext(),
							"Set all needed parametres", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(intent);
					return;
				}
				if (mc.authorizedUser(userText.getText().toString(),
						passwordText.getText().toString())) {
					authorizationView();
				} else {
					Toast.makeText(getApplicationContext(),
							("Wrong username or password"), Toast.LENGTH_LONG)
							.show();
				}
			}

		});

	}

	private void authorizationView() {
		connectionView();
	}

	private void unauthorizationView() {
		logginView();
	}

	private void connectionView() {
		setContentView(R.layout.activity_connection);

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					mc.unauthorizedUser();
					unauthorizationView();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					break;
				}
			}
		};

		builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener);

		state = (TextView) findViewById(R.id.textview_state);
		t0105 = (TextView) findViewById(R.id.textview_engine_temperature);
		t0105.setText("0");
		t010D = (TextView) findViewById(R.id.textview_speed);
		t010D.setText("0");
		t010C = (TextView) findViewById(R.id.textview_engine_rpm);
		t010C.setText("0");
		t0111 = (TextView) findViewById(R.id.textview_throttle);
		t0111.setText("0");
		tSpeedRequest = (TextView) findViewById(R.id.textview_speed_request);
		tSpeedRequest.setText("0");
		tAltitude = (TextView) findViewById(R.id.textview_altitude);
		tAltitude.setText("0");
		tAccuracy = (TextView) findViewById(R.id.textview_accuracy);
		tAccuracy.setText("0");
		tGPS = (TextView) findViewById(R.id.textview_gps);
		tGPS.setText("0");

		stopButton = (Button) findViewById(R.id.button_stop);
		stopButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				builder.show();
			}
		});

		// informationListButton = (Button)
		// findViewById(R.id.button_information_list);
		// informationListButton.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// informationListView();
		// }
		// });
	}

	private void informationListView() {
		setContentView(R.layout.activity_connection_messages);
		informationListButtonReturn = (Button) findViewById(R.id.button_information_list_return);
		informationListButtonReturn
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						connectionView();
					}
				});
		listView = (ListView) findViewById(R.id.list);
		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, informationList);
		listView.setAdapter(arrayAdapter);
	}

	public Activity get_activity() {
		return this;
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

	public int hexToInt(String value) {
		return Integer.parseInt(value, 16);

	}

	public String intToHex(int value) {
		return java.lang.Integer.toHexString(value);

	}

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	protected void onDestroy() {
		mc.close();
		super.onDestroy();
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerLabels.POST:
				message = (String) msg.obj;
				id = intToHex(msg.arg1);
				switch (msg.arg1) {
				case HandlerLabels.POST_ERROR:
					Toast.makeText(getApplicationContext(),
							("PostError: " + message), Toast.LENGTH_LONG)
							.show();
					break;

				case HandlerLabels.POST_WARNING:
					Toast.makeText(getApplicationContext(),
							("PostWar: " + message), Toast.LENGTH_LONG).show();
					break;
				case HandlerLabels.POST_AUTHENTICATION_FAILED:
					Toast.makeText(getApplicationContext(),
							("PostWar: " + message), Toast.LENGTH_LONG).show();
					break;
				}
				break;
			case HandlerLabels.GET:
				message = (String) msg.obj;
				id = intToHex(msg.arg1);
				switch (msg.arg1) {
				case HandlerLabels.GET_ERROR:
					Toast.makeText(getApplicationContext(),
							("GetError: " + message), Toast.LENGTH_LONG).show();
					break;

				case HandlerLabels.GET_WARNING:
					Toast.makeText(getApplicationContext(),
							("GetWar: " + message), Toast.LENGTH_LONG).show();
					break;
				case HandlerLabels.GET_AUTHENTICATION_FAILED:
					Toast.makeText(getApplicationContext(),
							("GetWar: " + message), Toast.LENGTH_LONG).show();
					break;
				}
				break;
			case HandlerLabels.OBD:
				switch (msg.arg1) {
				case HandlerLabels.OBD_CYCLE_COMPLETED:
					ShowValues a = (ShowValues) msg.obj;
					t0105.setText(a.getV0105());
					t010D.setText(a.getV010D());
					t010C.setText(a.getV010C());
					t0111.setText(a.getV0111());
					tSpeedRequest.setText(a.getvSpeedRequest());
					tAltitude.setText(a.getvAltitude());
					tAccuracy.setText(a.getvAccuracy());
					tGPS.setText(a.getvGPS());
					break;
				case HandlerLabels.OBD_STATE_NONE:
					state.setText("None");
					break;
				case HandlerLabels.OBD_STATE_SET_UP:
					state.setText("Set Up");
					break;
				case HandlerLabels.OBD_STATE_CONNECTING:
					state.setText("Connecting");
					break;
				case HandlerLabels.OBD_STATE_CONNECTING_FAILED_RECCONECT:
					state.setText("Connecting trying to connect");
					break;
				case HandlerLabels.OBD_STATE_CONNECTED:
					state.setText("Connected");
					break;
				case HandlerLabels.OBD_STATE_CONNECTED_LOST:
					state.setText("Connection Lost");
					break;
				case HandlerLabels.OBD_WARNING:
					message = (String) msg.obj;
					id = intToHex(msg.arg1);
					info = ("War: " + id + " " + message);
					informationList.add(info);
					break;
				case HandlerLabels.OBD_ERROR:
					message = (String) msg.obj;
					id = intToHex(msg.arg1);
					info = ("Err: " + id + " " + message);
					informationList.add(info);
					break;
				case HandlerLabels.OBD_INFORMATION:
					message = (String) msg.obj;
					id = intToHex(msg.arg1);
					info = ("Inf: " + id + " " + message);
					informationList.add(info);
					break;
				}
			}

		}
	};
}
