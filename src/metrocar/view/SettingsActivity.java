package metrocar.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;


public class SettingsActivity extends Activity {

	private static final String tag = "Metrocar";

	private EditText securityKey;
	private EditText unitId;
	private Button save;
	private Button reset;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		setUpView();

		save = (Button) findViewById(R.id.button_save);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(tag, "save");
				SharedPreferences settings = getSharedPreferences("my", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("unitId", unitId.getText().toString());
				editor.putString("secretKey", securityKey.getText().toString());
				editor.commit();
				setUpView();
				Toast.makeText(getApplicationContext(),
						"Setting have been saved",
						Toast.LENGTH_SHORT).show();
			}
		});
		reset = (Button) findViewById(R.id.button_reset);
		reset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences settings = getSharedPreferences("my", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.remove("automaticSending");
				editor.remove("unitId");
				editor.remove("secretKey");
				editor.remove("roadId");
				editor.commit();
				setUpView();
			}
		});

	}

	public void setUpView() {
		SharedPreferences settings = getSharedPreferences("my", 0);
		String secretKeyString = settings.getString("secretKey", "");
		String unitIdString = settings.getString("unitId", "");

		securityKey = (EditText) findViewById(R.id.edittex_secret_key);
		if (!secretKeyString.equals("")) {
			securityKey.setText(secretKeyString);
		}else{
			securityKey.setText("");
		}

		unitId = (EditText) findViewById(R.id.edittext_unit_id);
		if (!unitIdString.equals("")) {
			unitId.setText(unitIdString);
		}else{
			unitId.setText("");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(tag, "on pause");
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
