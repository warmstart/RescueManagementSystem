package  net.ut11.ccmp.example.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import rms.rescuemanagementsystem.R;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		addPreferencesFromResource(R.xml.preferences);
	}
}
