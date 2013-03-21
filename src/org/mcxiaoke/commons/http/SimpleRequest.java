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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.mcxiaoke.commons.http.auth.AuthConfig;
import org.mcxiaoke.commons.http.util.HttpUtils;
import org.mcxiaoke.commons.util.LogUtils;
import org.mcxiaoke.commons.util.StringUtils;

/**
 * @author mcxiaoke
 * 
 */
public class SimpleRequest {

	private static final boolean DEBUG = true;
	private static final String TAG = SimpleRequest.class.getSimpleName();

	private static void debug(String message) {
		LogUtils.v(TAG, message);
	}

	private final String mOriginalUrl;
	private final String mUrl;
	private final HttpMethod mMethod;
	private final AuthConfig mAuthConfig;
	private final HashMap<String, String> mHeaders;
	private final ArrayList<Parameter> mParameters;
	private final ArrayList<Parameter> mQueryParameters;
	private final HashMap<String, FileHolder> mFileParameters;
	private boolean mEnableGZipContent;

	public SimpleRequest(final SimpleRequest.RequestBuilder builder) {
		this.mOriginalUrl = builder.getUrl();
		this.mMethod = builder.getMethod();
		this.mAuthConfig = builder.getAuthConfig();
		this.mHeaders = new HashMap<String, String>();
		this.mParameters = new ArrayList<Parameter>();
		this.mQueryParameters = new ArrayList<Parameter>();
		this.mFileParameters = new HashMap<String, SimpleRequest.FileHolder>();

		if (builder.getParameters() != null) {
			mParameters.addAll(builder.getParameters());
		}
		if (builder.getQueryParameters() != null) {
			mQueryParameters.addAll(builder.getQueryParameters());
		}
		if (builder.getFileParameters() != null) {
			mFileParameters.putAll(builder.getFileParameters());
		}
		this.mEnableGZipContent = builder.isEnableGZipContent();
		this.mUrl = SimpleHelper.extractUrlQueryParameters(mOriginalUrl,
				mQueryParameters);
	}

	public boolean isEnableGZipContent() {
		return this.mEnableGZipContent;
	}

	public void setEnableGZipContent(boolean enableGZipContent) {
		this.mEnableGZipContent = enableGZipContent;
	}

	public void disableGzip() {
		this.mEnableGZipContent = false;
	}

	public String getUrl() {
		return mUrl;
	}

	public HttpMethod getMethod() {
		return mMethod;
	}

	public String getMethodName() {
		return HttpUtils.getMethodName(mMethod);
	}

	public AuthConfig getAuthConfig() {
		return mAuthConfig;
	}

	public final String getHeader(String name) {
		return mHeaders.get(name);
	}

	public Map<String, String> getHeaders() {
		return mHeaders;
	}

	public void addHeader(String name, String value) {
		mHeaders.put(name, value);
	}

	public void addHeaders(Map<String, String> headers) {
		if (headers != null) {
			this.mHeaders.putAll(headers);
		}
	}

	public void removeHeader(String key) {
		mHeaders.remove(key);
	}

	public void removeAllHeaders() {
		mHeaders.clear();
	}

	public void addParameter(String name, String value) {
		if (StringUtils.isNotEmpty(name)) {
			mParameters.add(new Parameter(name, value));
		}
	}

	public void addQueryParameter(String name, String value) {
		if (StringUtils.isNotEmpty(name)) {
			mQueryParameters.add(new Parameter(name, value));
		}
	}

	public void addParameters(Collection<? extends Parameter> params) {
		if (params != null) {
			this.mParameters.addAll(params);
		}
	}

	public void addQueryParameters(Collection<? extends Parameter> params) {
		if (params != null) {
			this.mQueryParameters.addAll(params);
		}
	}

	public void addParameters(Map<String, String> params) {
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				Parameter p = new Parameter(entry.getKey(), entry.getValue());
				this.mParameters.add(p);
			}
		}
	}

	public void addQueryParameters(Map<String, String> params) {
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				Parameter p = new Parameter(entry.getKey(), entry.getValue());
				this.mQueryParameters.add(p);
			}
		}
	}

	public void addFileParameter(String key, File file)
			throws FileNotFoundException {
		addFileParameter(key, new FileInputStream(file), file.getName());
	}

	public void addFileParameter(String key, byte[] bytes) {
		addFileParameter(key, new ByteArrayInputStream(bytes));
	}

	public void addFileParameter(String name, InputStream stream) {
		addFileParameter(name, stream, null);
	}

	public void addFileParameter(String key, InputStream stream, String fileName) {
		addFileParameter(key, stream, fileName);
	}

	public void addFileParameter(String key, InputStream stream,
			String fileName, String contentType) {
		if (StringUtils.isNotEmpty(key) && stream != null) {
			mFileParameters.put(key, new FileHolder(stream, fileName,
					contentType));
		}
	}

	public List<Parameter> getParameters() {
		return this.mParameters;
	}

	public List<Parameter> getQueryParameters() {
		return this.mQueryParameters;
	}

	public HashMap<String, FileHolder> getFileParameters() {
		return mFileParameters;
	}

	public boolean hasParameters() {
		return mParameters.size() > 0;
	}

	public boolean hasQueryParameters() {
		return mQueryParameters.size() > 0;
	}

	public boolean hasFileParameters() {
		return mFileParameters.size() > 0;
	}

	// public boolean hasOAuthParameters() {
	// return mOAuthParameters.size() > 0;
	// }

	public void removeAllParameters() {
		mParameters.clear();
		mQueryParameters.clear();
		mFileParameters.clear();
	}

	public final static class RequestBuilder {
		private String url;
		private HttpMethod method;
		private AuthConfig oauthConfig;
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

		public AuthConfig getAuthConfig() {
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

		public RequestBuilder setMethod(final AuthConfig authorization) {
			this.oauthConfig = authorization;
			return this;
		}

		public RequestBuilder setBasicAuth(final String userName,
				final String password) {
			this.oauthConfig = new AuthConfig(userName, password);
			return this;
		}

		public RequestBuilder setOAuth(final String apiKey,
				final String apiSecret, final String accessToken,
				final String accessTokenSecret) {
			this.oauthConfig = new AuthConfig(apiKey, apiSecret, accessToken,
					accessTokenSecret);
			return this;
		}

		public RequestBuilder setOAuth2(final String accessToken) {
			this.oauthConfig = new AuthConfig(accessToken);
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
					Parameter p = new Parameter(entry.getKey(),
							entry.getValue());
					this.parameters.add(p);
				}
			}
			return this;
		}

		public RequestBuilder addQuery(String key, String value) {
			if (StringUtils.isNotEmpty(key)) {
				parameters.add(new Parameter(key, value));
			}
			return this;
		}

		public RequestBuilder addQuery(NameValuePair pair) {
			if (pair != null) {
				parameters.add(new Parameter(pair));
			}
			return this;
		}

		public void addQuerys(Collection<? extends Parameter> params) {
			if (params != null) {
				parameters.addAll(params);
			}
		}

		public RequestBuilder addQuery(Map<String, String> params) {
			if (params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					Parameter p = new Parameter(entry.getKey(),
							entry.getValue());
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

	static class FileHolder {
		private static final String DEFAULT_FILENAME = "filename";
		public final InputStream inputStream;
		public final String fileName;
		public final String contentType;

		public FileHolder(InputStream inputStream, String fileName,
				String contentType) {
			this.inputStream = inputStream;
			this.fileName = (fileName == null) ? DEFAULT_FILENAME : fileName;
			this.contentType = contentType;
		}
	}

}
