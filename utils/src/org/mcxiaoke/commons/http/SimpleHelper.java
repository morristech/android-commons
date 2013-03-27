/**
 * 
 */
package org.mcxiaoke.commons.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;
import org.mcxiaoke.commons.Config;
import org.mcxiaoke.commons.auth.AuthService;
import org.mcxiaoke.commons.http.util.URIUtilsEx;
import org.mcxiaoke.commons.util.MimeUtils;
import org.mcxiaoke.commons.util.StringUtils;

import android.util.Log;

/**
 * @author mcxiaoke
 * 
 */
final class SimpleHelper {
	private static final boolean DEBUG = Config.DEBUG;
	private static final String TAG = "SimpleClient";
	private static final String ENCODING_UTF8 = HTTP.UTF_8;
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";
	private static final Charset CHARSET_UTF8 = Charset.forName(ENCODING_UTF8);

	private static StringBody createStringBody(String text) {
		try {
			return new StringBody(text, CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(" UTF8 is not supported?");
		}
	}

	public static HttpEntity createHttpEntity(final SimpleRequest request) {
		boolean hasFile = request.hasFileParameters();
		if (hasFile) {
			return encodeMultiPartEntity(request);
		} else {
			return encodeFormEntity(request);
		}
	}

	public static HttpEntity encodeFormEntity(final SimpleRequest request) {
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(request.getAllParameters(),
					ENCODING_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return entity;
	}

	public static HttpEntity encodeMultiPartEntity(final SimpleRequest request) {
		if (DEBUG) {
			Log.d(TAG, "encodeMultiPartEntity() request=" + request);
		}
		MultipartEntity entity = new MultipartEntity();
		List<Parameter> parameters = request.getAllParameters();
		for (Parameter param : parameters) {
			if (param.hasFile()) {
				final String filename = param.getValue();
				final String mimeType = MimeUtils.getMimeTypeFromPath(filename);
				entity.addPart(param.getName(), new FileBody(param.getFile(),
						mimeType));
			} else {
				entity.addPart(param.getName(),
						createStringBody(param.getValue()));
			}
		}

		return entity;
	}

	public static HttpUriRequest createHttpRequest(final SimpleRequest request) {
		AuthService.authorize(request);

		HttpUriRequest httpRequest = null;
		String requestUrl = request.getUrl();
		HttpMethod method = request.getMethod();
		Map<String, String> headers = new HashMap<String, String>(
				request.getHeaders());
		ArrayList<Parameter> parameters = new ArrayList<Parameter>(
				request.getAllParameters());
		if (HttpMethod.GET == method || HttpMethod.DELETE == method) {

			requestUrl = URIUtilsEx.appendQueryParameters(requestUrl,
					parameters);
			httpRequest = (HttpMethod.GET == method) ? new HttpGet(requestUrl)
					: new HttpDelete(requestUrl);

		} else if (HttpMethod.POST == method || HttpMethod.PUT == method) {

			HttpEntityEnclosingRequestBase httpEntityEnclosingRequest = null;
			httpEntityEnclosingRequest = (method == HttpMethod.POST) ? new HttpPost(
					requestUrl) : new HttpPut(requestUrl);
			HttpEntity entity = createHttpEntity(request);
			if (entity != null) {
				httpEntityEnclosingRequest.setEntity(entity);
			}
			httpRequest = httpEntityEnclosingRequest;
		}

		request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpRequest.addHeader(entry.getKey(), entry.getValue());
			}
		}

		return httpRequest;
	}

	/**
	 * 从URL中提取Query参数
	 * 
	 * @param url
	 *            原始URL
	 * @param params
	 *            提取的Query参数存放位置
	 * @return 返回去掉Query参数后的纯净URL
	 */
	public static String extractUrlQueryParameters(final String url,
			final List<Parameter> parameters) {
		String requestUrl = url;
		URI uri = URI.create(requestUrl);
		if (StringUtils.isNotEmpty(uri.getQuery())) {
			List<NameValuePair> queryParameters = URIUtilsEx
					.getQueryParameters(uri);
			if (queryParameters != null) {
				for (NameValuePair pair : queryParameters) {
					parameters.add(Parameter.of(pair));
				}
			}
			requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
		}
		return requestUrl;
	}
}
