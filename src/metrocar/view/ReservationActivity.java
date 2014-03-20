package metrocar.view;

import java.util.ArrayList;

import metrocar.database.DatabaseController;
import metrocar.database.Reservation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ReservationActivity extends Activity implements
		OnItemClickListener {

	DatabaseController datasource;
	ListView list;
	int i = 0;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		datasource = new DatabaseController(this,false);
		datasource.open();

		setContentView(R.layout.activity_reservation);

		ArrayList<Reservation> values = datasource.getAllReservations();

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView

		ReservationAdapter dataAdapter = new ReservationAdapter(this,
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

	/*public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		RecordedTable table = (RecordedTable) arg0.getItemAtPosition(arg2);
		Intent intent = new Intent(this, BluetoothActivity.class);
		intent.putExtra("KEY", table.getTableName());
		startActivity(intent);

	}
	*/

	private class ReservationAdapter extends ArrayAdapter<Reservation> {

		private ArrayList<Reservation> reservationList;

		public ReservationAdapter(Context context, int textViewResourceId,
				ArrayList<Reservation> countryList) {
			super(context, textViewResourceId, countryList);
			this.reservationList = new ArrayList<Reservation>();
			this.reservationList.addAll(countryList);
		}

		private class ViewHolder {
			TextView userId;
			TextView user;
			TextView start;
			TextView end;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.activity_reservation_item,
						null);

				holder = new ViewHolder();
				holder.userId = (TextView) convertView
						.findViewById(R.id.userId);
				holder.user = (TextView) convertView.findViewById(R.id.user);
				holder.start = (TextView) convertView.findViewById(R.id.start);
				holder.end = (TextView) convertView.findViewById(R.id.end);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Reservation res = reservationList.get(position);
			holder.userId.setText(res.getUserId()+"");
			holder.user.setText(res.getUser());
			holder.start.setText(res.convertTime(Long.valueOf(res.getStart())) );
			holder.end.setText(res.convertTime(Long.valueOf(res.getEnd())));
			return convertView;

		}

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
