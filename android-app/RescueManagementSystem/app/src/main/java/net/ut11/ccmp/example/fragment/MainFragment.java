package  net.ut11.ccmp.example.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import net.ut11.ccmp.example.adapter.MessagesAdapter;
import net.ut11.ccmp.lib.db.Message;
import net.ut11.ccmp.lib.net.InboxUpdateService;
import net.ut11.ccmp.lib.util.MessageUtil;

public class MainFragment extends ListFragment {

	private MessagesAdapter adapter = null;
	private MessageUpdateReceiver updateReceiver = null;
	private OnMessageClickedListener onMessageClickedListener = null;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		adapter = new MessagesAdapter();
		setListAdapter(adapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InboxUpdateService.startUpdate();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		IntentFilter filter = new IntentFilter();
		filter.addAction(MessageUtil.INTENT_MESSAGE_INSERTED);
		filter.addAction(MessageUtil.INTENT_MESSAGE_UPDATED);

		updateReceiver = new MessageUpdateReceiver();
		activity.registerReceiver(updateReceiver, filter);

		try {
			onMessageClickedListener = (OnMessageClickedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnMessageClickedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (updateReceiver != null) {
			try {
				getActivity().unregisterReceiver(updateReceiver);
			} catch (Exception e) {
			}
		}

		onMessageClickedListener = null;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (onMessageClickedListener != null) {
			onMessageClickedListener.onMessageClicked(adapter.getItem(position));
		}
	}

	public interface OnMessageClickedListener {
		public void onMessageClicked(Message msg);
	}

	private class MessageUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (adapter != null) {
				boolean scroll = MessageUtil.INTENT_MESSAGE_INSERTED.equals(intent.getAction());
				adapter.updateMessages();

				if (scroll) {
					getListView().setSelection(0);
				}
			}
		}
	}
}
