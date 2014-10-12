package  net.ut11.ccmp.example.util;

import android.text.format.DateFormat;

import rms.rescuemanagementsystem.R;
import net.ut11.ccmp.lib.LibApp;

import java.util.TimeZone;

public class DateUtil {

	private static TimeZone timezone = TimeZone.getDefault();

	public static boolean isToday(long date) {
		long now = System.currentTimeMillis();
		return (((now + timezone.getOffset(now)) / 86400000)) == (((date + timezone.getOffset(date)) / 86400000));
	}

	public static String formatDate(long date) {
		return DateFormat.format(LibApp.getContext().getString(R.string.message_date_format), date).toString();
	}

	public static String formatTime(long date) {
		return DateFormat.getTimeFormat(LibApp.getContext()).format(date);
	}

	public static String formatDateOrTime(long date) {
		if (isToday(date)) {
			return formatTime(date);
		}

		return formatDate(date);
	}
}
