package example.util;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import net.ut11.ccmp.example.activity.MessageDetailActivity;
import net.ut11.ccmp.lib.db.Message;
import net.ut11.ccmp.lib.util.MessageUtil;

public class NotificationService extends IntentService {

	private static final int MESSAGE_NOTIFICATION_ID = 1000;

	public NotificationService() {
		super("NotificationService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		long msgId = intent.getLongExtra(MessageUtil.INTENT_EXTRA_MESSAGE_ID, 0);
		boolean msgRead = intent.getBooleanExtra(MessageUtil.INTENT_EXTRA_MESSAGE_READ, false);

		if (MessageUtil.INTENT_MESSAGE_INSERTED.equals(action)) {
			notifyMessage(msgId);
		} else if (MessageUtil.INTENT_MESSAGE_UPDATED.equals(action) && msgRead) {
			cancelNotification(msgId);
		}
	}

	private void notifyMessage(long msgId) {
		if (msgId > 0) {
			Message msg = MessageUtil.getMessage(msgId);
			Context context = getApplicationContext();

			if (msg != null && !msg.isRead()) {
				NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				NotificationCompat.Builder b = new NotificationCompat.Builder(context);
				b.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
				b.setContentTitle(msg.getAddress());
				b.setContentText(msg.getMessage());
				b.setStyle(new NotificationCompat.BigTextStyle().bigText(msg.getMessage()));
				b.setAutoCancel(true);
				b.setSmallIcon(android.R.drawable.ic_dialog_alert);
				b.setLights(Color.RED, 500, 100);
				b.setWhen(msg.getDateSent());

				Intent i = new Intent(context, MessageDetailActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra(MessageDetailActivity.INTENT_EXTRA_MESSAGE_ID, msgId);
				PendingIntent pi = PendingIntent.getActivity(context, msg.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT);

				b.setContentIntent(pi);

				nm.notify(getTag(msgId), MESSAGE_NOTIFICATION_ID, b.build());
			}
		}
	}

	private void cancelNotification(long msgId) {
		if (msgId > 0) {
			Message msg = MessageUtil.getMessage(msgId);

			if (msg != null) {
				NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
				nm.cancel(getTag(msgId), MESSAGE_NOTIFICATION_ID);
			}
		}
	}

	private String getTag(long msgId) {
		return "msg#" + msgId;
	}

	public static class MessageUpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			intent.setClass(context, NotificationService.class);
			context.startService(intent);
		}
	}
}
