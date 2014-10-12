package  net.ut11.ccmp.example.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rms.rescuemanagementsystem.R;
import net.ut11.ccmp.example.util.DateUtil;
import net.ut11.ccmp.lib.db.Message;
import net.ut11.ccmp.lib.util.MessageUtil;

public class MessageDetailFragment extends Fragment {

	private static final String EXTRA_MESSAGE_ID = "messageId";

	private Message msg = null;

	public static MessageDetailFragment newInstance(long messageId) {
		MessageDetailFragment mdf = new MessageDetailFragment();

		Bundle args = new Bundle();
		args.putLong(EXTRA_MESSAGE_ID, messageId);
		mdf.setArguments(args);

		return mdf;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		long messageId = getArguments().getLong(EXTRA_MESSAGE_ID, 0);
		if (messageId > 0) {
			msg = MessageUtil.getMessage(messageId);
			MessageUtil.readMessage(msg);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.message_detail_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		TextView date = (TextView) view.findViewById(R.id.date);
		TextView message = (TextView) view.findViewById(R.id.message);

		if (msg != null) {
			long sent = msg.getDateSent();
			date.setText(DateUtil.formatDate(sent) + " " + DateUtil.formatTime(sent));
			message.setText(msg.getMessage());
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (msg != null) {
			getActivity().setTitle(msg.getAddress());
		}
	}
}
