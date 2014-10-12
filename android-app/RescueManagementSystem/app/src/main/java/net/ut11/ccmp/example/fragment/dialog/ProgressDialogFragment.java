package net.ut11.ccmp.example.fragment.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {

	private static final String ARGUMENT_RES_ID = "resId";

	public static ProgressDialogFragment newInstance(int resId) {
		ProgressDialogFragment pdf = new ProgressDialogFragment();

		Bundle args = new Bundle();
		args.putInt(ARGUMENT_RES_ID, resId);
		pdf.setArguments(args);

		return pdf;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(false);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int resId = getArguments().getInt(ARGUMENT_RES_ID, 0);
		return ProgressDialog.show(getActivity(), null, resId > 0 ? getString(resId) : null, true);
	}
}
