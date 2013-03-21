/**
 * 
 */
package org.mcxiaoke.commons.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

/**
 * @author mcxiaoke
 * 
 */
public final class SystemUtils {

	public static boolean hasGooglePlay(Context context) {
		Uri uri = Uri
				.parse("http://play.google.com/store/apps/details?id=com.google.android.gms");
		Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
		mapCall.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		mapCall.setPackage("com.android.vending");
		PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				mapCall, 0);
		return activities.size() > 0;
	}

	public static boolean isIntentSafe(Context context, Uri uri) {
		Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
		PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				mapCall, 0);
		return activities.size() > 0;
	}

	public static boolean hasIntent(Context context, Intent intent) {
		PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				intent, 0);
		return activities.size() > 0;
	}

}
