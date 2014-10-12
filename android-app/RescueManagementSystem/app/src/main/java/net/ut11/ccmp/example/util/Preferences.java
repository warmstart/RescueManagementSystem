package  net.ut11.ccmp.example.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

	private SharedPreferences preferences;
	private Context context;

	private static final String PREFERENCE_SHOW_POPUP = "show_popup";
	private static final String PREFERENCE_SHOW_POPUP_IF_LOCKED = "show_popup_if_locked";

	public Preferences(Context context) {
		this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.context = context;
	}

	public boolean showPopup() {
		return preferences.getBoolean(PREFERENCE_SHOW_POPUP, true);
	}

	public void setShowPopup(boolean show) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREFERENCE_SHOW_POPUP, show);
		editor.commit();
	}

	public boolean showPopupIfLocked() {
		return preferences.getBoolean(PREFERENCE_SHOW_POPUP_IF_LOCKED, false);
	}

	public void setShowPopupIfLocked(boolean show) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(PREFERENCE_SHOW_POPUP_IF_LOCKED, show);
		editor.commit();
	}
}
