package  net.ut11.ccmp.example.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ut11.ccmp.example.App;
import rms.rescuemanagementsystem.R;
import net.ut11.ccmp.example.view.CountdownView;
import net.ut11.ccmp.lib.net.api.endpoint.DeviceEndpoint;
import net.ut11.ccmp.lib.net.api.response.ApiException;
import net.ut11.ccmp.lib.receiver.VerificationPinReceiver;

import java.lang.ref.WeakReference;

public class VerificationInProgressFragment extends Fragment {

	private static final int COUNTDOWN_TIME = 45;

	private CountdownView countdownView;
	private static WeakReference<VerificationTimer> timer;

	private OnVerificationFinishedListener onVerificationFinishedListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.verification_in_progress_fragment, container, false);

		countdownView = (CountdownView) rootView.findViewById(R.id.countdown);
		countdownView.setMaxValue(COUNTDOWN_TIME);

		if (timer == null || timer.get() == null) {
			countdownView.setValue(COUNTDOWN_TIME);
		} else {
			countdownView.setValue(timer.get().getSeconds());
		}

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			onVerificationFinishedListener = (OnVerificationFinishedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnVerificationFinishedListener");
		}

		if (timer == null) {
			VerificationTimer vt = new VerificationTimer(this);
			timer = new WeakReference<VerificationTimer>(vt);
			vt.start();
		} else {
			timer.get().attach(this);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		onVerificationFinishedListener = null;
	}

	private void setSeconds(int seconds) {
		if (countdownView != null) {
			countdownView.setValue(seconds);
		}
	}

	private void handleVerificationFinished(String pin) {
		timer.clear();

		if (onVerificationFinishedListener != null) {
			onVerificationFinishedListener.onVerificationFinished(pin != null);
		}
	}

	private static class VerificationTimer extends CountDownTimer {

		private WeakReference<VerificationInProgressFragment> fragment;
		private PinReceiver pinReceiver;
		private int seconds;

		public VerificationTimer(VerificationInProgressFragment fragment) {
			super((COUNTDOWN_TIME + 2) * 1000, 1000);
			seconds = COUNTDOWN_TIME;
			attach(fragment);

			VerificationPinReceiver.setEnabled(true);
			pinReceiver = new PinReceiver();
			LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(pinReceiver,
					new IntentFilter(VerificationPinReceiver.INTENT_VERIFICATION_PIN_RECEIVED));
		}

		public int getSeconds() {
			return seconds;
		}

		public void attach(VerificationInProgressFragment fragment) {
			this.fragment = new WeakReference<VerificationInProgressFragment>(fragment);
		}

		@Override
		public void onFinish() {
			onStop();
		}

		private void onStop() {
			VerificationPinReceiver.setEnabled(false);
			if (pinReceiver != null) {
				LocalBroadcastManager.getInstance(App.getContext()).unregisterReceiver(pinReceiver);
				pinReceiver = null;
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			VerificationInProgressFragment f = fragment.get();
			if (f != null) {
				f.setSeconds(seconds);
				if (seconds == 0) {
					f.handleVerificationFinished(null);
				}
			}

			-- seconds;
		}

		private void verifyPin(String pin) {
			try {
				if (DeviceEndpoint.verifyPin(pin)) {
					cancel();
					onStop();

					VerificationInProgressFragment f = fragment.get();
					if (f != null) {
						f.handleVerificationFinished(pin);
					}
				}
			} catch (ApiException e) {
			}
		}

		class PinReceiver extends BroadcastReceiver {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (VerificationPinReceiver.INTENT_VERIFICATION_PIN_RECEIVED.equalsIgnoreCase(intent.getAction())) {
					final String pin = intent.getStringExtra(VerificationPinReceiver.INTENT_EXTRA_PIN);

					new Thread() {
						@Override
						public void run() {
							verifyPin(pin);
						}
					}.start();
				}
			}
		}
	}

	public interface OnVerificationFinishedListener {
		public void onVerificationFinished(boolean receivedPin);
	}
}
