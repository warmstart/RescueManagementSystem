package  net.ut11.ccmp.example.activity;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import net.ut11.ccmp.example.App;
import rms.rescuemanagementsystem.R;
import net.ut11.ccmp.example.fragment.PopupFragment;
import net.ut11.ccmp.lib.db.Message;
import net.ut11.ccmp.lib.util.MessageUtil;

public class PopupActivity extends FragmentActivity {

	private static final String INTENT_FINISH_POPUP = "net.ut11.ccmp.example.FINISH_POPUP";

	private FinishPopupReceiver finishPopupReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_activity);

		long msgId = getIntent().getLongExtra(MessageUtil.INTENT_EXTRA_MESSAGE_ID, 0);
		if (msgId == 0) {
			finish();
			return;
		}

		finishPopupReceiver = new FinishPopupReceiver();
		registerReceiver(finishPopupReceiver, new IntentFilter(INTENT_FINISH_POPUP));

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.container, PopupFragment.newInstance(msgId))
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateWindowFlags();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (finishPopupReceiver != null) {
			unregisterReceiver(finishPopupReceiver);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		long msgId = getIntent().getLongExtra(MessageUtil.INTENT_EXTRA_MESSAGE_ID, 0);
		if (msgId > 0) {
			Message msg = MessageUtil.getMessage(msgId);
			MessageUtil.readMessage(msg);
		}
	}

	private void updateWindowFlags() {
		KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
		if (keyguardManager.inKeyguardRestrictedInputMode() && App.getPreferences().showPopupIfLocked()) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
					WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
					WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
					WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
					WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
					WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
		}
	}

	private class FinishPopupReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (INTENT_FINISH_POPUP.equals(intent.getAction())) {
				finish();
			}
		}
	}

	public static class MessageInsertedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, final Intent insertedIntent) {
			if (!App.getPreferences().showPopup()) {
				return;
			}

			boolean read = insertedIntent.getBooleanExtra(MessageUtil.INTENT_EXTRA_MESSAGE_READ, true);

			if (!read) {
				context.sendOrderedBroadcast(new Intent(INTENT_FINISH_POPUP), null, new BroadcastReceiver() {
					@Override
					public void onReceive(Context context, Intent resultIntent) {
						insertedIntent.setClass(context, PopupActivity.class);
						insertedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);

						context.startActivity(insertedIntent);
					}
				}, null, 0, null, null);
			}
		}
	}
}
