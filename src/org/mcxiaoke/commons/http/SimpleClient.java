package org.mcxiaoke.commons.http;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.mcxiaoke.commons.http.util.HttpUtils;
import org.mcxiaoke.commons.util.NetworkUtils;

import android.content.Context;
import android.util.Log;

public class SimpleClient {
	private static final String TAG = SimpleClient.class.getSimpleName();

	private RuntimeException mLeakedException = new IllegalStateException(
			"AndroidHttpClient created and never closed");

	private final Context mAppContext;
	private final DefaultHttpClient mHttpClient;
	private final HttpContext mHttpContext;
	private final Map<String, String> mHeaders;

	private CookieStore mCookieStore;

	/** cURL logging configuration. */
	private volatile LoggingConfiguration curlConfiguration;

	public SimpleClient(Context context) {
		mAppContext = context.getApplicationContext();
		mHeaders = new HashMap<String, String>();
		mHttpContext = new SyncBasicHttpContext(new BasicHttpContext());
		mHttpClient = createHttpClient(mAppContext);
		mCookieStore = new BasicCookieStore();
		mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
	}

	private DefaultHttpClient createHttpClient(Context context) {
		DefaultHttpClient client = HttpUtils.createSharedHttpClient(context);
		client.addRequestInterceptor(new CurlLogger());
		client.addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest request, HttpContext context) {
				for (Map.Entry<String, String> entry : mHeaders.entrySet()) {
					request.addHeader(entry.getKey(), entry.getValue());
				}
			}
		});
		return client;
	}

	public HttpClient getHttpClient() {
		return this.mHttpClient;
	}

	public HttpContext getHttpContext() {
		return this.mHttpContext;
	}

	public CookieStore getCookieStore() {
		return mCookieStore;
	}

	public void setCookieStore(CookieStore cookieStore) {
		this.mCookieStore = cookieStore;
		mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
	}

	public void setUserAgent(String userAgent) {
		HttpProtocolParams
				.setUserAgent(this.mHttpClient.getParams(), userAgent);
	}

	public void setTimeout(int timeout) {
		final HttpParams httpParams = this.mHttpClient.getParams();
		ConnManagerParams.setTimeout(httpParams, timeout);
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	}

	public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
		this.mHttpClient.getConnectionManager().getSchemeRegistry()
				.register(new Scheme("https", sslSocketFactory, 443));
	}

	public void addHeader(String header, String value) {
		mHeaders.put(header, value);
	}

	public void setBasicAuth(String userName, String password) {
		setBasicAuth(userName, password, AuthScope.ANY);
	}

	public void setBasicAuth(String userName, String password, AuthScope scope) {
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				userName, password);
		this.mHttpClient.getCredentialsProvider().setCredentials(scope,
				credentials);
	}

	public SimpleResponse execute(final SimpleRequest simpleRequest)
			throws IOException {
		final HttpResponse response = sendRequest(simpleRequest);
		return new SimpleResponse(response);
	}

	public HttpResponse sendRequest(final SimpleRequest simpleRequest)
			throws IOException {
		final DefaultHttpClient client = mHttpClient;
		final HttpContext httpContext = mHttpContext;
		final HttpUriRequest request = SimpleHelper
				.createHttpRequest(simpleRequest);
		return sendRequest(client, httpContext, request);
	}

	public HttpResponse sendRequest(HttpUriRequest request) throws IOException {
		final DefaultHttpClient client = mHttpClient;
		final HttpContext context = mHttpContext;
		return client.execute(request, context);
	}

	public <T> T sendRequest(HttpUriRequest request,
			ResponseHandler<? extends T> responseHandler) throws IOException {
		final DefaultHttpClient client = mHttpClient;
		final HttpContext httpContext = mHttpContext;
		return sendRequest(client, httpContext, request, responseHandler);
	}

	private HttpResponse sendRequest(DefaultHttpClient client,
			HttpContext httpContext, HttpUriRequest request)
			throws ClientProtocolException, IOException {
		final Context context = mAppContext;
		setProxyForChina(context, client.getParams());
		return client.execute(request, httpContext);
	}

	private <T> T sendRequest(DefaultHttpClient client,
			HttpContext httpContext, HttpUriRequest request,
			ResponseHandler<? extends T> responseHandler) throws IOException {
		final Context context = mAppContext;
		setProxyForChina(context, client.getParams());
		return client.execute(request, responseHandler, httpContext);
	}

	public void setHttpProxy(final String proxyHost, int proxyPort) {
		if (proxyHost != null && proxyPort > 0) {
			final HttpParams params = mHttpClient.getParams();
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
	}

	public void setHttpProxy(final HttpHost proxy) {
		if (proxy != null) {
			final HttpParams params = mHttpClient.getParams();
			params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
	}

	public void removeHttpProxy() {
		final HttpParams params = mHttpClient.getParams();
		params.removeParameter(ConnRoutePNames.DEFAULT_PROXY);
	}

	private void setProxyForChina(final Context context,
			final HttpParams httpParams) {
		NetworkUtils.setProxyForChina(context, httpParams);
	}

	/**
	 * Release resources associated with this client. You must call this, or
	 * significant resources (sockets and memory) may be leaked.
	 * 
	 */
	public void close() {
		if (mLeakedException != null) {
			getConnectionManager().shutdown();
			mLeakedException = null;
		}
	}

	public HttpParams getParams() {
		return mHttpClient.getParams();
	}

	public ClientConnectionManager getConnectionManager() {
		return mHttpClient.getConnectionManager();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (mLeakedException != null) {
			Log.e(TAG, "Leak found", mLeakedException);
			mLeakedException = null;
		}
	}

	/* cURL logging support. */

	/**
	 * Logging tag and level.
	 */
	private static class LoggingConfiguration {

		private final String tag;
		private final int level;

		private LoggingConfiguration(String tag, int level) {
			this.tag = tag;
			this.level = level;
		}

		private boolean isLoggable() {
			return Log.isLoggable(tag, level);
		}

		private void println(String message) {
			Log.println(level, tag, message);
		}
	}

	/**
	 * Enables cURL request logging for this client.
	 * 
	 * @param name
	 *            to log messages with
	 * @param level
	 *            at which to log messages (see {@link android.util.Log})
	 */
	public void enableCurlLogging(String name, int level) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		if (level < Log.VERBOSE || level > Log.ASSERT) {
			throw new IllegalArgumentException("Level is out of range ["
					+ Log.VERBOSE + ".." + Log.ASSERT + "]");
		}

		curlConfiguration = new LoggingConfiguration(name, level);
	}

	/**
	 * Disables cURL logging for this client.
	 */
	public void disableCurlLogging() {
		curlConfiguration = null;
	}

	/**
	 * Logs cURL commands equivalent to requests.
	 */
	private class CurlLogger implements HttpRequestInterceptor {
		public void process(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			LoggingConfiguration configuration = curlConfiguration;
			if (configuration != null && configuration.isLoggable()
					&& request instanceof HttpUriRequest) {
				// Never print auth token -- we used to check ro.secure=0 to
				// enable that, but can't do that in unbundled code.
				configuration.println(toCurl((HttpUriRequest) request, true));
			}
		}
	}

	/**
	 * Generates a cURL command equivalent to the given request.
	 */
	private static String toCurl(HttpUriRequest request, boolean logAuthToken)
			throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append("curl ");
		for (Header header : request.getAllHeaders()) {
			if (!logAuthToken
					&& (header.getName().equals("Authorization") || header
							.getName().equals("Cookie"))) {
				continue;
			}
			builder.append("--header \"");
			builder.append(header.toString().trim());
			builder.append("\" ");
		}
		URI uri = request.getURI();

		// If this is a wrapped request, use the URI from the original
		// request instead. getURI() on the wrapper seems to return a
		// relative URI. We want an absolute URI.
		if (request instanceof RequestWrapper) {
			HttpRequest original = ((RequestWrapper) request).getOriginal();
			if (original instanceof HttpUriRequest) {
				uri = ((HttpUriRequest) original).getURI();
			}
		}

		builder.append("\"");
		builder.append(uri);
		builder.append("\"");
		return builder.toString();
	}

}
