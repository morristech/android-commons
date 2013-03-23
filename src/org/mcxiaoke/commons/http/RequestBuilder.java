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
	private Authorization oauthConfig;
	private HashMap<String, String> headers;
	private ArrayList<Parameter> parameters;
	private ArrayList<Parameter> queryParameters;
	private HashMap<String, FileHolder> fileParameters;
	private boolean enableGZipContent;

	public static RequestBuilder create() {
		return new RequestBuilder();
	}

	public RequestBuilder() {
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

	public Authorization getAuthConfig() {
		return oauthConfig;
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
		this.url = url.toString();
		return this;
	}

	public RequestBuilder setUrl(URI uri) {
		this.url = uri.toString();
		return this;
	}

	public RequestBuilder setMethod(HttpMethod method) {
		this.method = method;
		return this;
	}

	public RequestBuilder setAuth(final Authorization oauthConfig) {
		this.oauthConfig = oauthConfig;
		return this;
	}

	public RequestBuilder setBasicAuth(final String userName,
			final String password) {
		this.oauthConfig = new Authorization(userName, password);
		return this;
	}

	public RequestBuilder setOAuth(final String apiKey, final String apiSecret,
			final String accessToken, final String accessTokenSecret) {
		this.oauthConfig = new Authorization(apiKey, apiSecret, accessToken,
				accessTokenSecret);
		return this;
	}

	public RequestBuilder setOAuth2(final String accessToken) {
		this.oauthConfig = new Authorization(accessToken);
		return this;
	}

	public RequestBuilder addHeader(String name, String value) {
		this.headers.put(name, value);
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
		try {
			addParameter(key, new FileInputStream(file), file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return this;
	}

	public RequestBuilder addParameter(String key, byte[] bytes) {
		addParameter(key, new ByteArrayInputStream(bytes));
		return this;
	}

	public RequestBuilder addParameter(String name, InputStream stream) {
		addParameter(name, stream, null);
		return this;
	}

	public RequestBuilder addParameter(String key, InputStream stream,
			String fileName) {
		addParameter(key, stream, fileName, null);
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
