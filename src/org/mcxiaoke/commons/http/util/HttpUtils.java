/**
 * 
 */
package org.mcxiaoke.commons.http.util;

import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.mcxiaoke.commons.http.HttpMethod;
import org.mcxiaoke.commons.http.impl.GzipRequestInterceptor;
import org.mcxiaoke.commons.http.impl.GzipResponseInterceptor;
import org.mcxiaoke.commons.http.impl.RequestRetryHandler;

import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;

/**
 * @author mcxiaoke
 * 
 */
public final class HttpUtils {
	private static final int DEFAULT_MAX_ALL_CONNECTIONS = 100;
	private static final int DEFAULT_MAX_CONNECTIONS = 20;
	private static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;
	private static final int DEFAULT_MAX_RETRIES = 3;
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
	public final static String HEADER_CONTENT_TYPE = "content-type";
	public final static String HEADER_CONTENT_ENCODING = "content-encoding";

	public static DefaultHttpClient createSharedHttpClient(Context context) {
		return createSharedHttpClient(context, true, true);
	}

	public static DefaultHttpClient createSharedHttpClient(Context context,
			boolean enableGzip, boolean enableRetry) {
		BasicHttpParams httpParams = createHttpParams();
		ThreadSafeClientConnManager cm = createThreadSafeClientConnManager(
				context, httpParams);
		DefaultHttpClient client = new DefaultHttpClient(cm, httpParams);
		if (enableGzip) {
			client.addResponseInterceptor(new GzipResponseInterceptor());
			client.addRequestInterceptor(new GzipRequestInterceptor());
		}
		if (enableRetry) {
			client.setHttpRequestRetryHandler(new RequestRetryHandler(
					DEFAULT_MAX_RETRIES));
		}
		return client;
	}

	public static DefaultHttpClient createSingleHttpClient(Context context) {
		BasicHttpParams httpParams = createHttpParams();
		SingleClientConnManager cm = createSingleClientConnManager(httpParams);
		DefaultHttpClient client = new DefaultHttpClient(cm, httpParams);
		client.addResponseInterceptor(new GzipResponseInterceptor());
		client.addRequestInterceptor(new GzipRequestInterceptor());
		client.setHttpRequestRetryHandler(new RequestRetryHandler(
				DEFAULT_MAX_RETRIES));
		return client;
	}

	public static SingleClientConnManager createSingleClientConnManager(
			BasicHttpParams httpParams) {

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));
		SingleClientConnManager cm = new SingleClientConnManager(httpParams,
				schemeRegistry);
		return cm;
	}

	public static ThreadSafeClientConnManager createThreadSafeClientConnManager(
			BasicHttpParams httpParams) {

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
				httpParams, schemeRegistry);
		return cm;
	}

	public static ThreadSafeClientConnManager createThreadSafeClientConnManager(
			Context context, BasicHttpParams httpParams) {
		// Use a session cache for SSL sockets
		SSLSessionCache sessionCache = context == null ? null
				: new SSLSessionCache(context);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		// schemeRegistry.register(new Scheme("https", SSLSocketFactory
		// .getSocketFactory(), 443));
		schemeRegistry.register(new Scheme("https", SSLCertificateSocketFactory
				.getHttpSocketFactory(DEFAULT_SOCKET_TIMEOUT, sessionCache),
				443));
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
				httpParams, schemeRegistry);
		return cm;
	}

	public static BasicHttpParams createHttpParams() {
		return createHttpParams(DEFAULT_SOCKET_TIMEOUT, DEFAULT_MAX_CONNECTIONS);
	}

	public static BasicHttpParams createHttpParams(int socketTimeout,
			int maxConnections) {
		BasicHttpParams httpParams = new BasicHttpParams();

		ConnManagerParams.setTimeout(httpParams, socketTimeout);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
				new ConnPerRouteBean(maxConnections));
		ConnManagerParams.setMaxTotalConnections(httpParams,
				DEFAULT_MAX_ALL_CONNECTIONS);

		HttpProtocolParams.setUseExpectContinue(httpParams, false);
		HttpProtocolParams.setContentCharset(null, HTTP.UTF_8);
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		// Turn off stale checking. Our connections break all the time anyway,
		// and it's not worth it to pay the penalty of checking every time.
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
		HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams,
				DEFAULT_SOCKET_BUFFER_SIZE);

		HttpClientParams.setRedirecting(httpParams, false);
		return httpParams;
	}

	public static String getMethodName(final HttpMethod method) {
		String methodName = null;
		switch (method) {
		case HEAD:
			methodName = HttpHead.METHOD_NAME;
			break;
		case GET:
			methodName = HttpGet.METHOD_NAME;
			break;
		case DELETE:
			methodName = HttpDelete.METHOD_NAME;
			break;
		case POST:
			methodName = HttpPost.METHOD_NAME;
			break;
		case PUT:
			methodName = HttpPut.METHOD_NAME;
			break;
		default:
			// throw new NullPointerException("http method must not be null.");
			break;
		}
		return methodName;
	}

}
