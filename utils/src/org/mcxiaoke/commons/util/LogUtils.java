/**
 * 
 */
package org.mcxiaoke.commons.util;

import java.util.HashMap;

import android.util.Log;

/**
 * @author mcxiaoke
 * 
 */
public final class LogUtils {
	public static final boolean DEBUG = true;
	public static final String TAG_DEFAULT = "DEBUG";
	public static final String TAG_TRACE = "TRACE";

	private static HashMap<String, Long> sTraceMap = new HashMap<String, Long>();

	private LogUtils() {
	}

	public static void startTrace(String key) {
		sTraceMap.put(key, System.currentTimeMillis());
	}

	public static void stopTrace(String key) {
		Long start = sTraceMap.remove(key);
		if (start != null) {
			long end = System.currentTimeMillis();
			long interval = end - start;
			Log.v(TAG_TRACE, "key use time: " + interval + "ms.");
		}
	}

	public void removeTrace(String key) {
		sTraceMap.remove(key);
	}

	public void clearTrace() {
		sTraceMap.clear();
		Log.v(TAG_TRACE, "trace is cleared.");
	}

	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}

	public static void w(String tag, String msg) {
		Log.w(tag, msg);
	}

	public static void i(String tag, String msg) {
		Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	public static void v(String tag, String msg) {
		Log.v(tag, msg);
	}

	public static void e(String msg) {
		Log.e(TAG_DEFAULT, msg);
	}

	public static void w(String msg) {
		Log.w(TAG_DEFAULT, msg);
	}

	public static void i(String msg) {
		Log.i(TAG_DEFAULT, msg);
	}

	public static void d(String msg) {
		Log.d(TAG_DEFAULT, msg);
	}

	public static void v(String msg) {
		Log.v(TAG_DEFAULT, msg);
	}

}
