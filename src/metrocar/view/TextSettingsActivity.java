package metrocar.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import metrocar.utils.Device;

import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public final class TextSettingsActivity extends ListActivity {

	private ArrayList<String> arrayList;
	private ArrayAdapter<String> arrayAdapter;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private Button load;
	private Button reset;
	private Button show;

	// =================================================================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {

			setContentView(R.layout.activity_settings_tex);

			settings = getSharedPreferences("my", 0); 
			editor = settings.edit();
			arrayList = new ArrayList<String>();
			arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, arrayList);
			setListAdapter(arrayAdapter);

			load = (Button) findViewById(R.id.button_load);
			load.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					try {
						arrayList.clear();
						readFile();
						arrayAdapter.notifyDataSetChanged();
					} catch (Exception e) {
						arrayList.add("File not found");
						arrayAdapter.notifyDataSetChanged();
					}

				}
			});
			reset = (Button) findViewById(R.id.button_reset);
			reset.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					arrayList.clear();
					arrayAdapter.notifyDataSetChanged();
					editor.clear();
					editor.commit();
				}
			});

			show = (Button) findViewById(R.id.button_show);
			show.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					arrayList.clear();
					if (settings.contains("doLogGPSTracker")) {
						if (settings.getBoolean("doLogGPSTracker", false)) {
							arrayList.add("doLogGPSTracker: " + "true");
						} else {
							arrayList.add("doLogGPSTracker: " + "false");
						}
					}
					if (settings.contains("doLogGet")) {
						if (settings.getBoolean("doLogGet", false)) {
							arrayList.add("doLogGet: " + "true");
						} else {
							arrayList.add("doLogGet: " + "false");
						}
					}
					if (settings.contains("doLogPost")) {
						if (settings.getBoolean("doLogPost", false)) {
							arrayList.add("doLogPost: " + "true");
						} else {
							arrayList.add("doLogPost: " + "false");
						}
					}
					if (settings.contains("doLogUnitEngine")) {
						if (settings.getBoolean("doLogUnitEngine", false)) {
							arrayList.add("doLogUnitEngine: " + "true");
						} else {
							arrayList.add("doLogUnitEngine: " + "false");
						}
					}
					if (settings.contains("doLogMainCore")) {
						if (settings.getBoolean("doLogMainCore", false)) {
							arrayList.add("doLogMainCore: " + "true");
						} else {
							arrayList.add("doLogMainCore: " + "false");
						}
					}
					if (settings.contains("doLogCheckInternetConnection")) {
						if (settings.getBoolean("doLogCheckInternetConnection",
								false)) {
							arrayList.add("doLogCheckInternetConnection: "
									+ "true");
						} else {
							arrayList.add("doLogCheckInternetConnection: "
									+ "false");
						}
					}
					if (settings.contains("doLogLockEngine")) {
						if (settings.getBoolean("doLogLockEngine", false)) {
							arrayList.add("doLogLockEngine: " + "true");
						} else {
							arrayList.add("doLogLockEngine: " + "false");
						}
					}
					if (settings.contains("doLogDatabaseController")) {
						if (settings.getBoolean("doLogDatabaseController",
								false)) {
							arrayList.add("doLogDatabaseController: " + "true");
						} else {
							arrayList
									.add("doLogDatabaseController: " + "false");
						}
					}
					if (settings.contains("doLogObdMessage")) {
						if (settings.getBoolean("doLogObdMessage", false)) {
							arrayList.add("doLogObdMessage: " + "true");
						} else {
							arrayList.add("doLogObdMessage: " + "false");
						}
					}
					if (settings.contains("authorizedPostPeriod")) {
						arrayList.add("authorizedPostPeriod: "
								+ settings.getLong("authorizedPostPeriod", 0));
					}
					if (settings.contains("authorizedGetPeriod")) {
						arrayList.add("authorizedGetPeriod: "
								+ settings.getLong("authorizedGetPeriod", 0));
					}
					if (settings.contains("unauthorizedGetPeriod")) {
						arrayList.add("unauthorizedGetPeriod: "
								+ settings.getLong("unauthorizedGetPeriod", 0));
					}
					if (settings.contains("unauthorizedPostPeriod")) {
						arrayList.add("unauthorizedPostPeriod: "
								+ settings.getLong("unauthorizedPostPeriod", 0));
					}
					if (settings.contains("get_request")) {
						arrayList.add("get_request: "
								+ settings.getString("get_request", ""));
					}
					if (settings.contains("post")) {
						arrayList.add("post: " + settings.getString("post", ""));
					}
					if (settings.contains("open_advance")) {
						arrayList.add("open_advance: "
								+ settings.getLong("open_advance", 0));
					}
					if (settings.contains("end_advance")) {
						arrayList.add("end_advance: "
								+ settings.getLong("end_advance", 0));
					}
					if (settings.contains("min_time_bw_updates")) {
						arrayList.add("min_time_bw_updates: "
								+ settings.getLong("min_time_bw_updates", 0));
					}
					if (settings.contains("min_distance_change_for_server")) {
						arrayList.add("min_distance_change_for_server: "
								+ settings.getLong(
										"min_distance_change_for_server", 0));
					}
					if (settings.contains("post_limit")) {
						arrayList.add("post_limit: "
								+ settings.getString("post_limit", ""));
					}
					if (settings.contains("post_limit")) {
						arrayList.add("post_limit: "
								+ settings.getString("post_limit", ""));
					}
					if (settings.contains("secretKey")) {
						arrayList.add("secret_key: "
								+ settings.getString("secretKey", ""));
					}
					if (settings.contains("unitId")) {
						arrayList.add("unit_id: "
								+ settings.getString("unitId", ""));
					}
					if (settings.contains("LockPass")) {
						arrayList.add("lock_key: "
								+ settings.getString("LockPass", ""));
					}
					
					arrayAdapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readFile() throws Exception {
		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath(), "metrocar.txt");
		FileInputStream fin = new FileInputStream(file);
		readInput(fin);
		fin.close();
	}

	public void readInput(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		String[] help;
		int lineNumber = 1;
		while ((line = reader.readLine()) != null) {
			help = line.split("=");
			if (help.length != 2) {
				arrayList.add("Error input on line " + lineNumber);
				// arrayList.notifyDataSetChanged();
			}
			processInput(help[0], help[1]);
			lineNumber++;
			editor.commit();
		}
	}

	public void processInput(String identificator, String value) {
		if (identificator.equals("unit_id")) {
			try {
				Long.valueOf(value);
				editor.putString("unitId", value);
				arrayList.add("Unit Id: " + value);
			} catch (Exception e) {
				arrayList.add("Unit ID is not Number");
			}
			return;
		}
		if (identificator.equals("secret_key")) {
			editor.putString("secretKey", value);
			arrayList.add("Secret Key: " + value);
			return;
		}
		if (identificator.equals("lock_key")) {
			editor.putString("LockPass", value);
			arrayList.add("Lock Key: " + value);
			return;
		}
		if (identificator.equals("do_log_gpstracker")) {
			if (value.equals("true")) {
				editor.putBoolean("doLogGPSTracker", true);
				arrayList.add("Gpstracker true");
			} else {
				editor.putBoolean("doLogGPSTracker", false);
				arrayList.add("Gpstracker false");
			}
			return;
		}
		if (identificator.equals("do_log_get")) {
			if (value.equals("true")) {
				editor.putBoolean("doLogGet", true);
				arrayList.add("Get true");
			} else {
				editor.putBoolean("doLogGet", false);
				arrayList.add("Get false");
			}
			return;
		}
		if (identificator.equals("do_log_post")) {
			if (value.equals("true")) {
				editor.putBoolean("doLogPost", true);
				arrayList.add("Post true");
			} else {
				editor.putBoolean("doLogPost", false);
				arrayList.add("Post false");
			}
			return;
		}
		if (identificator.equals("do_log_unitengine")) {
			if (value.equals("true")) {
				editor.putBoolean("doLogUnitEngine", true);
				arrayList.add("UnitEngine true");
			} else {
				editor.putBoolean("doLogUnitEngine", false);
				arrayList.add("UnitEngine false");
			}
			return;
		}
		if (identificator.equals("do_log_maincore")) {
			if (value.equals("true")) {
				editor.putBoolean("doLogMainCore", true);
				arrayList.add("MainCore true");
			} else {
				editor.putBoolean("doLogMainCore", false);
				arrayList.add("MainCore false");
			}
			return;
		}
		if (identificator.equals("do_log_checkinternetconnection")) {
			if (value.equals("true")) {
				editor.putBoolean("doLogCheckInternetConnection", true);
				arrayList.add("CheckInternetConnection true");
			} else {
				editor.putBoolean("doLogCheckInternetConnection", false);
				arrayList.add("CheckInternetConnection false");
			}
			return;
		}
		if (identificator.equals("do_log_lockengine")) {
			if (value.equals("true")) {
				editor.putBoolean("doLogLockEngine", true);
				arrayList.add("LockEngine true");
			} else {
				editor.putBoolean("doLogLockEngine", false);
				arrayList.add("LockEngine false");
			}
			return;
		}
		if (identificator.equals("do_log_databasecontroller")) {
			if (value.equals("true")) {
				editor.putBoolean("doLogDatabaseController", true);
				arrayList.add("DatabaseController true");
			} else {
				editor.putBoolean("doLogDatabaseController", false);
				arrayList.add("DatabaseController false");
			}
			return;
		}
		if (identificator.equals("do_log_obdmessage")) {
			if (value.equals("true")) {
				editor.putBoolean("doLogObdMessage", false);
				arrayList.add("ObdMessage true");
			} else {
				editor.putBoolean("doLogObdMessage", false);
				arrayList.add("ObdMessage false");
			}
			return;
		}
		if (identificator.equals("authorized_post_period")) {
			try {
				Long.valueOf(value);
				editor.putLong("authorizedPostPeriod", Long.valueOf(value));
				arrayList.add("Aut Post period " + value);
			} catch (Exception e) {
				arrayList.add("Aut Post period not a number");
			}
			return;
		}
		if (identificator.equals("authorized_get_period")) {
			try {
				Long.valueOf(value);
				editor.putLong("authorizedGetPeriod", Long.valueOf(value));
				arrayList.add("Aut Get period " + value);
			} catch (Exception e) {
				arrayList.add("Aut Get period not a number");
			}
			return;
		}
		if (identificator.equals("unauthorized_post_period")) {
			try {
				Long.valueOf(value);
				editor.putLong("unauthorizedPostPeriod", Long.valueOf(value));
				arrayList.add("Un Post period " + value);
			} catch (Exception e) {
				arrayList.add("Un Post period not a number");
			}
			return;
		}
		if (identificator.equals("unauthorized_get_period")) {
			try {
				Long.valueOf(value);
				editor.putLong("unauthorizedGetPeriod", Long.valueOf(value));
				arrayList.add("Un Get period " + value);
			} catch (Exception e) {
				arrayList.add("Un Get period not a number");
			}
			return;
		}
		if (identificator.equals("address_get")) {
			try {
				URL u = new URL(value);
				u.toURI();
				editor.putString("get_request", value);
				arrayList.add("Valid Get Address " + value);
			} catch (Exception e) {
				arrayList.add("Unvalid Get Address");
			}
			return;
		}
		if (identificator.equals("address_post")) {
			try {
				URL u = new URL(value);
				u.toURI();
				editor.putString("post", value);
				arrayList.add("Valid Post Address " + value);
			} catch (Exception e) {
				arrayList.add("Unvalid Post Address");
			}
			return;
		}
		if (identificator.equals("open_advance")) {
			try {
				Long.valueOf(value);
				editor.putLong("open_advance", Long.valueOf(value));
				arrayList.add("Open advance " + value);
			} catch (Exception e) {
				arrayList.add("Open advance not a number");
			}
			return;
		}
		if (identificator.equals("end_advance")) {
			try {
				Long.valueOf(value);
				editor.putLong("end_advance", Long.valueOf(value));
				arrayList.add("End advance " + value);
			} catch (Exception e) {
				arrayList.add("End advance not a number");
			}
			return;
		}
		if (identificator.equals("min_time_bw_updates")) {
			try {
				Long.valueOf(value);
				editor.putLong("min_time_bw_updates", Long.valueOf(value));
				arrayList.add("Time update " + value);
			} catch (Exception e) {
				arrayList.add("Time update not a number");
			}
			return;
		}
		if (identificator.equals("min_distance_change_for_server")) {
			try {
				Long.valueOf(value);
				editor.putLong("min_distance_change_for_server",
						Long.valueOf(value));
				arrayList.add("Distance update " + value);
			} catch (Exception e) {
				arrayList.add("Distance update not a number");
			}
			return;
		}
		if (identificator.equals("post_limit")) {
			try {
				Long.valueOf(value);
				editor.putString("post_limit", value);
				arrayList.add("Post limit " + value);
			} catch (Exception e) {
				arrayList.add("Post limit not a number");
			}
			return;
		}
	}
}
