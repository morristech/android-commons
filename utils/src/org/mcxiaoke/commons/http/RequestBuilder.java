/**
 * 
 */
package org.mcxiaoke.commons.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.mcxiaoke.commons.auth.Authorization;
import org.mcxiaoke.commons.http.SimpleRequest.FileHolder;
import org.mcxiaoke.commons.util.StringUtils;

/**
 * @author mcxiaoke
 * 
 */
public final class RequestBuilder {
	private String url;
	private HttpMethod method;
	private Authorization authorization;
	private boolean enableGZipContent;
	private HashMap<String, String> headers;
	private ArrayList<Parameter> parameters;
	private ArrayList<Parameter> queryParameters;
	private HashMap<String, FileHolder> fileParameters;

	public static RequestBuilder create() {
		return new RequestBuilder();
	}

	public RequestBuilder() {
		this.url = null;
		this.method = HttpMethod.GET;
		this.enableGZipContent = true;
		this.headers = new HashMap<String, String>();
		this.queryParameters = new ArrayList<Parameter>();
		this.parameters = new ArrayList<Parameter>();
		this.fileParameters = new HashMap<String, SimpleRequest.FileHolder>();
	}

	public SimpleRequest build() {
		return new SimpleRequest(this);
	}

	public String getUrl() {
		return url;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public Authorization getAuthorization() {
		return authorization;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public List<Parameter> getQueryParameters() {
		return queryParameters;
	}

	public Map<String, FileHolder> getFileParameters() {
		return fileParameters;
	}

	public boolean isEnableGZipContent() {
		return enableGZipContent;
	}

	public RequestBuilder setUrl(String url) {
		this.url = url;
		return this;
	}

	public RequestBuilder setUrl(URL url) {
		if (url != null) {
			this.url = url.toString();
		}
		return this;
	}

	public RequestBuilder setUrl(URI uri) {
		if (uri != null) {
			this.url = uri.toString();
		}
		return this;
	}

	public RequestBuilder setMethod(HttpMethod method) {
		if (method != null) {
			this.method = method;
		}
		return this;
	}

	public RequestBuilder setAuthorization(final Authorization authorization) {
		this.authorization = authorization;
		return this;
	}

	public RequestBuilder setBasicAuth(final String userName,
			final String password) {
		if (StringUtils.isNotEmpty(userName)
				&& StringUtils.isNotEmpty(password)) {
			this.authorization = new Authorization(userName, password);
		}
		return this;
	}

	public RequestBuilder setOAuth(final String apiKey, final String apiSecret,
			final String accessToken, final String accessTokenSecret) {
		if (StringUtils.isNotEmpty(apiKey) && StringUtils.isNotEmpty(apiSecret)
				&& StringUtils.isNotEmpty(accessToken)
				&& StringUtils.isNotEmpty(accessTokenSecret)) {
			this.authorization = new Authorization(apiKey, apiSecret,
					accessToken, accessTokenSecret);
		}
		return this;
	}

	public RequestBuilder setOAuth2(final String accessToken) {
		if (StringUtils.isNotEmpty(accessToken)) {
			this.authorization = new Authorization(accessToken);
		}
		return this;
	}

	public RequestBuilder addHeader(String key, String value) {
		if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
			this.headers.put(key, value);
		}
		return this;
	}

	public RequestBuilder addHeaders(Map<String, String> headers) {
		if (headers != null) {
			this.headers.putAll(headers);
		}
		return this;
	}

	public RequestBuilder addParameter(String key, String value) {
		if (StringUtils.isNotEmpty(key)) {
			parameters.add(new Parameter(key, value));
		}
		return this;
	}

	public RequestBuilder addParameter(NameValuePair pair) {
		if (pair != null) {
			parameters.add(new Parameter(pair));
		}
		return this;
	}

	public void addParameters(Collection<? extends Parameter> params) {
		if (params != null) {
			parameters.addAll(params);
		}
	}

	public RequestBuilder addParameters(Map<String, String> params) {
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				Parameter p = new Parameter(entry.getKey(), entry.getValue());
				this.parameters.add(p);
			}
		}
		return this;
	}

	public RequestBuilder addQueryParameter(String key, String value) {
		if (StringUtils.isNotEmpty(key)) {
			parameters.add(new Parameter(key, value));
		}
		return this;
	}

	public RequestBuilder addQueryParameter(NameValuePair pair) {
		if (pair != null) {
			parameters.add(new Parameter(pair));
		}
		return this;
	}

	public void addQueryParameters(Collection<? extends Parameter> params) {
		if (params != null) {
			parameters.addAll(params);
		}
	}

	public RequestBuilder addQueryParameters(Map<String, String> params) {
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				Parameter p = new Parameter(entry.getKey(), entry.getValue());
				this.parameters.add(p);
			}
		}
		return this;
	}

	public RequestBuilder addParameter(String key, File file) {
		if (StringUtils.isNotEmpty(key) && file != null) {
			try {
				addParameter(key, new FileInputStream(file), file.getName());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return this;
	}

	public RequestBuilder addParameter(String key, byte[] bytes) {
		if (StringUtils.isNotEmpty(key) && bytes != null) {
			addParameter(key, new ByteArrayInputStream(bytes));
		}
		return this;
	}

	public RequestBuilder addParameter(String key, InputStream stream) {
		if (StringUtils.isNotEmpty(key) && stream != null) {
			addParameter(key, stream, null);
		}
		return this;
	}

	public RequestBuilder addParameter(String key, InputStream stream,
			String fileName) {
		if (StringUtils.isNotEmpty(key) && stream != null) {
			addParameter(key, stream, fileName, null);
		}
		return this;
	}

	public RequestBuilder addParameter(String key, InputStream stream,
			String fileName, String contentType) {
		if (StringUtils.isNotEmpty(key) && stream != null) {
			fileParameters.put(key, new FileHolder(stream, fileName,
					contentType));
		}
		return this;
	}

	public RequestBuilder setEnableGZipContent(boolean enableGZipContent) {
		this.enableGZipContent = enableGZipContent;
		return this;
	}
}
