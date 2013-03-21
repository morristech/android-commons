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

import org.apache.commons.codec.Charsets;
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
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;
import org.mcxiaoke.commons.http.SimpleRequest.FileHolder;
import org.mcxiaoke.commons.http.util.URIUtilsEx;
import org.mcxiaoke.commons.util.StringUtils;

/**
 * @author mcxiaoke
 * 
 */
final class SimpleHelper {
	private static final String ENCODING_UTF8 = HTTP.UTF_8;
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";
	private static final Charset CHARSET_UTF8 = Charsets.UTF_8;

	public static HttpEntity createHttpEntity(final SimpleRequest request) {
		return request.hasFileParameters() ? encodeMultiPartEntity(request)
				: encodeFormEntity(request);
	}

	public static HttpEntity encodeFormEntity(final SimpleRequest request) {
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(request.getParameters(),
					ENCODING_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return entity;
	}

	public static HttpEntity encodeMultiPartEntity(final SimpleRequest request) {
		MultipartEntity entity = new MultipartEntity();
		encodeTextParams(entity, request.getParameters());
		encodeBinaryParams(entity, request.getFileParameters());
		return entity;
	}

	public static void encodeTextParams(final MultipartEntity multipartEntity,
			final List<Parameter> parameters) {
		for (Parameter param : parameters) {
			multipartEntity.addPart(param.getName(),
					StringBody.create(param.getValue(), CHARSET_UTF8));
		}
	}

	public static void encodeBinaryParams(MultipartEntity multipartEntity,
			final Map<String, FileHolder> fileParameters) {
		for (Map.Entry<String, FileHolder> entry : fileParameters.entrySet()) {
			FileHolder holder = entry.getValue();
			if (holder.inputStream != null) {
				if (holder.contentType != null) {
					multipartEntity.addPart(entry.getKey(),
							new InputStreamBody(holder.inputStream,
									holder.contentType, holder.fileName));
				} else {
					multipartEntity.addPart(entry.getKey(),
							new InputStreamBody(holder.inputStream,
									holder.fileName));
				}
			}
		}
	}

	public static HttpUriRequest createHttpRequest(final SimpleRequest request) {
		HttpUriRequest httpRequest = null;
		String baseUrl = request.getUrl();
		HttpMethod method = request.getMethod();
		Map<String, String> headers = new HashMap<String, String>(
				request.getHeaders());
		ArrayList<Parameter> queryParameters = new ArrayList<Parameter>(
				request.getQueryParameters());
		ArrayList<Parameter> parameters = new ArrayList<Parameter>(
				request.getParameters());
		String requestUrl = URIUtilsEx.appendQueryParameters(baseUrl,
				queryParameters);
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

		if (request.isEnableGZipContent()) {
			request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
		}

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
					parameters.add(new Parameter(pair));
				}
			}
			requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
		}
		return requestUrl;
	}
}
