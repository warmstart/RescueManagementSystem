package  net.ut11.ccmp.example.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import rms.rescuemanagementsystem.R;
import net.ut11.ccmp.example.fragment.SettingsFragment;

public class SettingsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);

		if (savedInstanceState == null) {
			getFragmentManager()
					.beginTransaction()
					.replace(R.id.container, new SettingsFragment())
					.commit();
		}
	}
}
