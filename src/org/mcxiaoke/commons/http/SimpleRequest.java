/**
 * 
 */
package org.mcxiaoke.commons.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mcxiaoke.commons.auth.Authorization;
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
	private final Authorization authorization;
	private final HashMap<String, String> mHeaders;
	private final ArrayList<Parameter> mParameters;
	private final ArrayList<Parameter> mQueryParameters;
	private final HashMap<String, FileHolder> mFileParameters;
	private boolean mEnableGZipContent;

	public SimpleRequest(final RequestBuilder builder) {
		this.mOriginalUrl = builder.getUrl();
		this.mMethod = builder.getMethod();
		this.authorization = builder.getAuthorization();
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
		return getMethodName(mMethod);
	}

	public Authorization getAuthorization() {
		return authorization;
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

	public static String getMethodName(final HttpMethod method) {
		if (method != null) {
			return method.name();
		} else {
			return null;
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
