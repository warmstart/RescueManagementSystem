package  net.ut11.ccmp.example.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rms.rescuemanagementsystem.R;

public class VerificationSuccessfulFragment extends Fragment implements View.OnClickListener {

	private View next;
	private OnCompleteRegistrationListener onCompleteRegistrationListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.verification_successful_fragment, container, false);

		next = rootView.findViewById(R.id.next);
		next.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			onCompleteRegistrationListener = (OnCompleteRegistrationListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnCompleteRegistrationListener");
		}
	}

	@Override
	public void onClick(View v) {
		if (v.equals(next)) {
			if (onCompleteRegistrationListener != null) {
				onCompleteRegistrationListener.onCompleteRegistration();
			}
		}
	}

	public interface OnCompleteRegistrationListener {
		public void onCompleteRegistration();
	}
}
