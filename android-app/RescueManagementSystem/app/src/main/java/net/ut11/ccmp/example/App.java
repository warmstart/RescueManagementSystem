package net.ut11.ccmp.example;

import net.ut11.ccmp.example.util.Preferences;
import net.ut11.ccmp.lib.LibApp;

public class App extends LibApp {

	private static Preferences preferences;

	@Override
	public void onCreate() {
		super.onCreate();

		preferences = new Preferences(getContext());
	}

	public static Preferences getPreferences() {
		return preferences;
	}
}
