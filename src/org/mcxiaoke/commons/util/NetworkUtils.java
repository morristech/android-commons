package org.mcxiaoke.commons.util;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author mcxiaoke
 * @version 1.0 2011.12.02
 * @version 1.1 2011.12.07
 * @version 1.2 2011.12.12
 * @version 1.3 2011.12.21
 * @version 1.4 2013.03.16
 * 
 */
public final class NetworkUtils {
	private static final String TAG = NetworkUtils.class.getSimpleName();
	public static final String WIFI = "WIFI";
	public static final String MOBILE = "MOBILE";
	private static final String MOBILE_CTWAP = "ctwap";
	private static final String MOBILE_CMWAP = "cmwap";
	private static final String MOBILE_3GWAP = "3gwap";
	private static final String MOBILE_UNIWAP = "uniwap";

	/**
	 * 根据当前网络状态填充代理
	 * 
	 * @param context
	 * @param httpParams
	 */
	public static final void setProxyForChina(final Context context,
			final HttpParams httpParams) {
		if (context == null || httpParams == null) {
			return;
		}
		boolean needCheckProxy = true;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null
				|| WIFI.equalsIgnoreCase(networkInfo.getTypeName())
				|| networkInfo.getExtraInfo() == null) {
			needCheckProxy = false;
		}
		if (needCheckProxy) {
			String typeName = networkInfo.getExtraInfo();
			if (MOBILE_CTWAP.equalsIgnoreCase(typeName)) {
				HttpHost proxy = new HttpHost("10.0.0.200", 80);
				httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			} else if (MOBILE_CMWAP.equalsIgnoreCase(typeName)
					|| MOBILE_UNIWAP.equalsIgnoreCase(typeName)
					|| MOBILE_3GWAP.equalsIgnoreCase(typeName)) {
				HttpHost proxy = new HttpHost("10.0.0.172", 80);
				httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
		}

		// String defaultProxyHost = android.net.Proxy.getDefaultHost();
		// int defaultProxyPort = android.net.Proxy.getDefaultPort();
		// if (defaultProxyHost != null && defaultProxyHost.length() > 0
		// && defaultProxyPort > 0) {
		// HttpHost proxy = new HttpHost(defaultProxyHost, defaultProxyPort);
		// httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
		// }
	}

	public static final boolean isNotConnected(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connec.getActiveNetworkInfo();
		return info == null || info.isConnectedOrConnecting() == false;
	}

	public static final boolean isConnected(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connec.getActiveNetworkInfo();
		return info != null && info.isConnectedOrConnecting();
	}

	public static final boolean isWifi(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connec.getActiveNetworkInfo();
		return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
	}

	public static final boolean isMobile(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connec.getActiveNetworkInfo();
		return info != null
				&& info.getType() == ConnectivityManager.TYPE_MOBILE;
	}
}
