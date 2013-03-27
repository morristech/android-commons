package org.mcxiaoke.commons.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.widget.ListView;

public class ViewCompatUtils {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void smoothScrollToPositionCompat(ListView listView,
			int position, int offset) {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			listView.smoothScrollToPositionFromTop(position, offset);
		} else if (android.os.Build.VERSION.SDK_INT >= 8) {
			int firstVisible = listView.getFirstVisiblePosition();
			int lastVisible = listView.getLastVisiblePosition();
			if (position < firstVisible)
				listView.smoothScrollToPosition(position);
			else
				listView.smoothScrollToPosition(position + lastVisible
						- firstVisible - 2);
		} else {
			listView.setSelectionFromTop(position, 0);
		}
	}
}
