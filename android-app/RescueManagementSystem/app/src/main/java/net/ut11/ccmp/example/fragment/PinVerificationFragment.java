package  net.ut11.ccmp.example.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import net.ut11.ccmp.example.App;
import rms.rescuemanagementsystem.R;
import net.ut11.ccmp.example.fragment.dialog.ErrorDialogFragment;
import net.ut11.ccmp.example.fragment.dialog.ProgressDialogFragment;
import net.ut11.ccmp.lib.net.api.endpoint.DeviceEndpoint;
import net.ut11.ccmp.lib.net.api.response.ApiException;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

public class PinVerificationFragment extends Fragment implements View.OnClickListener {

	private static final String PROGRESS_TAG = "verifyProgress";

	private static final int ERROR_PIN = 1;
	private static final int ERROR_OTHER = 2;

	private EditText pin;
	private View register;

	private OnPinRegistrationListener onPinRegistrationListener;
	private static WeakReference<VerificationTask> task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pin_verification_fragment, container, false);

		pin = (EditText) rootView.findViewById(R.id.pin);
		register = rootView.findViewById(R.id.register);
		register.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			onPinRegistrationListener = (OnPinRegistrationListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnPinRegistrationListener");
		}

		if (task != null && task.get() != null) {
			task.get().attach(this);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		onPinRegistrationListener = null;
	}

	@Override
	public void onClick(View v) {
		if (v.equals(register)) {
			doRegister();
		}
	}

	private void doRegister() {
		InputMethodManager imm = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(pin.getWindowToken(), 0);

		String pinStr = pin.getText().toString();

		if (onPinRegistrationListener != null) {
			pinStr = pinStr.replaceAll("\\D", "");

			if (pinStr.length() > 3) {
				ProgressDialogFragment pdf = ProgressDialogFragment.newInstance(R.string.pin_verification_progress_text);
				pdf.show(getFragmentManager(), PROGRESS_TAG);

				VerificationTask verifyTask =  new VerificationTask(this, pinStr);
				task = new WeakReference<VerificationTask>(verifyTask);
				verifyTask.execute();
			}
		}
	}

	public interface OnPinRegistrationListener {
		public void onPinRegistration();
	}

	private void handleVerificationResult(String pin, int errorCode) {
		task.clear();

		FragmentManager fm = getFragmentManager();
		ProgressDialogFragment pdf = (ProgressDialogFragment) fm.findFragmentByTag(PROGRESS_TAG);

		if (pdf != null) {
			pdf.dismiss();
		}

		if (errorCode == 0 && onPinRegistrationListener != null) {
			onPinRegistrationListener.onPinRegistration();
		} else {
			int res = errorCode == ERROR_PIN ? R.string.pin_verification_wrong_pin_text : R.string.pin_verification_failed_text;
			ErrorDialogFragment error = ErrorDialogFragment.newInstance(res);
			error.show(fm, null);
		}
	}

	private static class VerificationTask extends AsyncTask<Void, Void, Integer> {

		private WeakReference<PinVerificationFragment> fragment;
		private String pin;

		public VerificationTask(PinVerificationFragment fragment, String pin) {
			this.pin = pin;
			attach(fragment);
		}

		public void attach(PinVerificationFragment fragment) {
			this.fragment = new WeakReference<PinVerificationFragment>(fragment);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				DeviceEndpoint.verifyPin(pin);
			} catch (ApiException e) {
				if (e.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) {
					return ERROR_PIN;
				}

				return ERROR_OTHER;
			}

			return 0;
		}

		@Override
		protected void onPostExecute(Integer error) {
			fragment.get().handleVerificationResult(pin, error);
		}
	}
}
