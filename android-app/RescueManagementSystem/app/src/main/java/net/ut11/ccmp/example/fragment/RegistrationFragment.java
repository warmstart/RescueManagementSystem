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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import net.ut11.ccmp.example.App;
import rms.rescuemanagementsystem.R;
import net.ut11.ccmp.example.fragment.dialog.ErrorDialogFragment;
import net.ut11.ccmp.example.fragment.dialog.ProgressDialogFragment;
import net.ut11.ccmp.lib.net.api.endpoint.DeviceEndpoint;
import net.ut11.ccmp.lib.net.api.response.ApiException;

import java.lang.ref.WeakReference;

public class RegistrationFragment extends Fragment
		implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

	private static final String PROGRESS_TAG = "regProgress";

	private EditText msisdn;
	private CheckBox terms;
	private View register;

	private OnRegistrationListener onRegistrationListener;
	private static WeakReference<RegistrationTask> task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.registration_fragment, container, false);

		msisdn = (EditText) rootView.findViewById(R.id.msisdn);
		terms = (CheckBox) rootView.findViewById(R.id.terms);
		register = rootView.findViewById(R.id.register);

		terms.setOnCheckedChangeListener(this);
		register.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			onRegistrationListener = (OnRegistrationListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnRegistrationListener");
		}

		if (task != null && task.get() != null) {
			task.get().attach(this);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		onRegistrationListener = null;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.equals(terms)) {
			register.setEnabled(isChecked);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.equals(register)) {
			doRegister();
		}
	}

	private void doRegister() {
		InputMethodManager imm = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(msisdn.getWindowToken(), 0);

		String msisdnStr = msisdn.getText().toString();

		if (msisdnStr.startsWith("+")) {
			msisdnStr = msisdnStr.replaceAll("\\D", "");

			if (msisdnStr.length() > 5) {
				ProgressDialogFragment pdf = ProgressDialogFragment.newInstance(R.string.registration_progress_text);
				pdf.show(getFragmentManager(), PROGRESS_TAG);

				RegistrationTask regTask =  new RegistrationTask(this, Long.parseLong(msisdnStr));
				task = new WeakReference<RegistrationTask>(regTask);
				regTask.execute();
			}
		}
	}

	private void handleRegistrationResult(long msisdn, boolean result) {
		task.clear();

		FragmentManager fm = getFragmentManager();
		ProgressDialogFragment pdf = (ProgressDialogFragment) fm.findFragmentByTag(PROGRESS_TAG);

		if (pdf != null) {
			pdf.dismiss();
		}

		if (result && onRegistrationListener != null) {
			onRegistrationListener.onRegistration(msisdn);
		} else {
			ErrorDialogFragment error = ErrorDialogFragment.newInstance(R.string.registration_failed_text);
			error.show(fm, null);
		}
	}

	public interface OnRegistrationListener {
		public void onRegistration(long msisdn);
	}

	private static class RegistrationTask extends AsyncTask<Void, Void, Boolean> {

		private WeakReference<RegistrationFragment> fragment;
		private long msisdn;

		public RegistrationTask(RegistrationFragment fragment, long msisdn) {
			this.msisdn = msisdn;
			attach(fragment);
		}

		public void attach(RegistrationFragment fragment) {
			this.fragment = new WeakReference<RegistrationFragment>(fragment);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean success;
			try {
				success = DeviceEndpoint.registerDevice(msisdn);
			} catch (ApiException e) {
				success = false;
			}

			return success;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			fragment.get().handleRegistrationResult(msisdn, result);
		}
	}
}
