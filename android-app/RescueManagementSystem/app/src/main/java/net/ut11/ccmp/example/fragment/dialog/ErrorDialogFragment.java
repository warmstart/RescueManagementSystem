package net.ut11.ccmp.example.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorDialogFragment extends DialogFragment {

	private static final String ARGUMENT_RES_ID = "resId";

	public static ErrorDialogFragment newInstance(int resId) {
		ErrorDialogFragment edf = new ErrorDialogFragment();

		Bundle args = new Bundle();
		args.putInt(ARGUMENT_RES_ID, resId);
		edf.setArguments(args);

		return edf;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int resId = getArguments().getInt(ARGUMENT_RES_ID, 0);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(resId);
		builder.setPositiveButton(android.R.string.ok, null);

		return builder.create();
	}
}
