package  net.ut11.ccmp.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import rms.rescuemanagementsystem.R;
import net.ut11.ccmp.example.fragment.MessageDetailFragment;

public class MessageDetailActivity extends ActionBarActivity {

	public static final String INTENT_EXTRA_MESSAGE_ID = "messageId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail_activity);

		if (savedInstanceState == null) {
			handleIntent(getIntent());
		}

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		long msgId = intent.getLongExtra(INTENT_EXTRA_MESSAGE_ID, 0);
		if (msgId > 0) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.container, MessageDetailFragment.newInstance(msgId))
					.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.message_detail_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
