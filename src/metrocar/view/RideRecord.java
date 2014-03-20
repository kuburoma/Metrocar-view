package metrocar.view;

import java.util.ArrayList;

import metrocar.database.DatabaseController;
import metrocar.database.Record;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RideRecord extends Activity {

	DatabaseController datasource;
	ListView list;
	int i = 0;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		datasource = new DatabaseController(this,false);
		datasource.open();

		setContentView(R.layout.activity_showrecords);

		ArrayList<Record> values = datasource.getAllRecords();

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView

		ConnectionAdapter dataAdapter = new ConnectionAdapter(this,
				R.layout.activity_showrecords_item, values);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(dataAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	private class ConnectionAdapter extends ArrayAdapter<Record> {

		private ArrayList<Record> connectionList;

		public ConnectionAdapter(Context context, int textViewResourceId,
				ArrayList<Record> countryList) {
			super(context, textViewResourceId, countryList);
			this.connectionList = new ArrayList<Record>();
			this.connectionList.addAll(countryList);
		}

		private class ViewHolder {
			TextView time;
			TextView latitude;
			TextView longtitude;
			TextView altitude;
			TextView m0105;
			TextView m010C;
			TextView m010D;
			TextView m0111;
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.activity_showrecords_item,
						null);

				holder = new ViewHolder();
				holder.time = (TextView) convertView
						.findViewById(R.id.time);
				holder.latitude = (TextView) convertView
						.findViewById(R.id.latitude);
				holder.longtitude = (TextView) convertView
						.findViewById(R.id.longtitude);
				holder.altitude = (TextView) convertView
						.findViewById(R.id.altitude);
				holder.m0105 = (TextView) convertView
						.findViewById(R.id.m0105);
				holder.m010C = (TextView) convertView
						.findViewById(R.id.m010C);
				holder.m010D = (TextView) convertView
						.findViewById(R.id.m010D);
				holder.m0111 = (TextView) convertView
						.findViewById(R.id.m0111);			
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Record connection = connectionList.get(position);
			holder.time.setText(connection.getTime()+" - ");
			holder.longtitude.setText(connection.getLongtituted()+" - ");
			holder.latitude.setText(connection.getLatitude()+" - ");
			holder.altitude.setText(connection.getAltitude()+" - ");
			holder.m0105.setText(connection.getM0105()+" - ");
			holder.m010C.setText(connection.getM010C()+" - ");
			holder.m010D.setText(connection.getM010D()+" - ");
			holder.m0111.setText(connection.getM0111()+" - ");
			return convertView;

		}

	}

}
