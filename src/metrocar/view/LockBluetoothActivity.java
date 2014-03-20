package metrocar.view;

import java.util.ArrayList;
import java.util.Set;

import metrocar.utils.Device;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public final class LockBluetoothActivity extends ListActivity {


	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	private ArrayAdapter<String> arrayAdapter;

	private ArrayList<Device> allDevice = new ArrayList<Device>();
	private int i = 0;

	private static final String tag = "Metrocar";

	Intent discoverableIntent;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkBluetooth();
		queryToPairedDevice(); 

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		BluetoothDevice remoteDevice = bluetoothAdapter
				.getRemoteDevice(allDevice.get(position).getMacAddress());

		SharedPreferences settings = getSharedPreferences("my", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("LockName", remoteDevice.getName());
		editor.putString("LockAddress", remoteDevice.getAddress());
		editor.commit();
		Toast.makeText(getApplicationContext(),
				"Bluetooth device was selected",
				Toast.LENGTH_LONG).show();
	}

	private void checkBluetooth() {
		if (bluetoothAdapter == null) {
			callInfoDialog("Toto zarizeni nepodporuje bluetooth");
		}
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 1);
		}
	}

	private void queryToPairedDevice() {
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter
				.getBondedDevices();
		ArrayList<String> pairedDev = new ArrayList<String>();

		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				pairedDev.add(device.getName() + "\n" + "Parovano ");
				allDevice.add(new Device(i, device.getAddress(), device
						.getName(), "Parovano"));
				i++;
			}
		}
		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, pairedDev);
		setListAdapter(arrayAdapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_bluetooth, menu);
		return true;
	}

	private void callInfoDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setCancelable(false)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						LockBluetoothActivity.this.finish();
					}
				});
		builder.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (bluetoothAdapter != null) {
			bluetoothAdapter.cancelDiscovery();
		}
	}
}
