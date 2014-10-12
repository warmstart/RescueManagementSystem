package  net.ut11.ccmp.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


import net.ut11.ccmp.example.fragment.MainFragment;
import net.ut11.ccmp.example.fragment.PinVerificationFragment;
import net.ut11.ccmp.example.fragment.RegistrationFragment;
import net.ut11.ccmp.example.fragment.VerificationInProgressFragment;
import net.ut11.ccmp.example.fragment.VerificationSuccessfulFragment;
import net.ut11.ccmp.lib.db.Message;

import rms.rescuemanagementsystem.App;
import rms.rescuemanagementsystem.R;

public class MainActivity extends ActionBarActivity
		implements RegistrationFragment.OnRegistrationListener,
		VerificationInProgressFragment.OnVerificationFinishedListener,
		PinVerificationFragment.OnPinRegistrationListener,
		VerificationSuccessfulFragment.OnCompleteRegistrationListener,
		MainFragment.OnMessageClickedListener{

	private static final String STATE_REGISTRATION = "registration";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		if (savedInstanceState == null) {
			//boolean registered = App.getLibPreferences().isRegistered();
			//Fragment f = registered ? new MainFragment() : new RegistrationFragment();
			//getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRegistration(final long msisdn) {
		VerificationInProgressFragment f = new VerificationInProgressFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.container, f).addToBackStack(STATE_REGISTRATION).commit();
	}

	@Override
	public void onVerificationFinished(boolean receivedPin) {
		FragmentManager fm = getSupportFragmentManager();

		Fragment f = receivedPin ? new VerificationSuccessfulFragment() : new PinVerificationFragment();
		fm.popBackStack(STATE_REGISTRATION, FragmentManager.POP_BACK_STACK_INCLUSIVE);


		FragmentTransaction t = fm.beginTransaction().replace(R.id.container, f);
		if (!receivedPin) {
			t.addToBackStack(STATE_REGISTRATION);
		}
		t.commit();
	}

	@Override
	public void onPinRegistration() {
		FragmentManager fm = getSupportFragmentManager();

		Fragment f = new VerificationSuccessfulFragment();
		fm.popBackStack(STATE_REGISTRATION, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		fm.beginTransaction().replace(R.id.container, f).commit();
	}

	@Override
	public void onCompleteRegistration() {
		FragmentManager fm = getSupportFragmentManager();

		Fragment f = new MainFragment();
		fm.popBackStack(STATE_REGISTRATION, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		fm.beginTransaction().replace(R.id.container, f).commit();
	}

	@Override
	public void onMessageClicked(Message msg) {
		Intent i = new Intent(this, MessageDetailActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra(MessageDetailActivity.INTENT_EXTRA_MESSAGE_ID, msg.getId());
		startActivity(i);
	}
}
