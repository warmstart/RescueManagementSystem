package  net.ut11.ccmp.example.adapter;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import rms.rescuemanagementsystem.R;
import net.ut11.ccmp.example.util.DateUtil;
import net.ut11.ccmp.lib.LibApp;
import net.ut11.ccmp.lib.db.Message;
import net.ut11.ccmp.lib.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends BaseAdapter {

	private final List<Message> messages = new ArrayList<Message>();

	public MessagesAdapter() {
		messages.addAll(MessageUtil.getMessages());
	}

	public void updateMessages() {
		messages.clear();
		messages.addAll(MessageUtil.getMessages());

		notifyDataSetChanged();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Message getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return messages.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(LibApp.getContext()).inflate(R.layout.message_list_item, parent, false);

			holder = new ViewHolder();
			holder.sender = (TextView) convertView.findViewById(R.id.sender);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.date = (TextView) convertView.findViewById(R.id.date);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Message msg = getItem(position);
		CharSequence sender = msg.getAddress();
		CharSequence message = msg.getMessage();
		CharSequence date = DateUtil.formatDateOrTime(msg.getDateSent());

		if (!msg.isRead()) {
			SpannableStringBuilder builder = new SpannableStringBuilder(sender);
			builder.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.length(), 0);
			sender = builder;

			builder = new SpannableStringBuilder(message);
			builder.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.length(), 0);
			message = builder;

			builder = new SpannableStringBuilder(date);
			builder.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.length(), 0);
			date = builder;
		}

		holder.sender.setText(sender);
		holder.message.setText(message);
		holder.date.setText(date);

		return convertView;
	}

	private class ViewHolder {
		private TextView sender;
		private TextView message;
		private TextView date;
	}
}
