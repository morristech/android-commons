/**
 * 
 */
package org.mcxiaoke.commons.http;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mcxiaoke.commons.auth.Authorization;
import org.mcxiaoke.commons.util.CollectionUtils;
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
	private Authorization authorization;
	private final HashMap<String, String> mHeaders;
	private final ArrayList<Parameter> mParameters;

	public SimpleRequest(String url, HttpMethod method) {
		this.mOriginalUrl = url;
		this.mMethod = method == null ? HttpMethod.GET : method;
		this.mHeaders = new HashMap<String, String>();
		this.mParameters = new ArrayList<Parameter>();
		this.mUrl = SimpleHelper.extractUrlQueryParameters(mOriginalUrl,
				mParameters);
	}

	public String getUrl() {
		return mUrl;
	}

	public HttpMethod getMethod() {
		return mMethod;
	}

	public String getMethodName() {
		if (mMethod != null) {
			return mMethod.name();
		} else {
			return null;
		}
	}

	public void setAuthorization(Authorization authorization) {
		this.authorization = authorization;
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

	public void addParameter(String name, File file) {
		if (StringUtils.isNotEmpty(name)) {
			mParameters.add(new Parameter(name, file));
		}
	}

	public void addParameters(Collection<? extends Parameter> params) {
		if (params != null) {
			this.mParameters.addAll(params);
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

	public List<Parameter> getAllParameters() {
		return this.mParameters;
	}

	public void removeAllParameters() {
		mParameters.clear();
	}

	public List<Parameter> getTextParameters() {
		List<Parameter> fileParameters = new ArrayList<Parameter>();
		for (Parameter param : mParameters) {
			if (!param.hasFile()) {
				fileParameters.add(param);
			}
		}
		return fileParameters;
	}

	public List<Parameter> getFileParameters() {
		List<Parameter> fileParameters = new ArrayList<Parameter>();
		for (Parameter param : mParameters) {
			if (param.hasFile()) {
				fileParameters.add(param);
			}
		}
		return fileParameters;
	}

	public boolean hasFileParameters() {
		return parametersContainsFile(mParameters);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SimpleRequest [mOriginalUrl=");
		builder.append(mOriginalUrl);
		builder.append(", mUrl=");
		builder.append(mUrl);
		builder.append(", mMethod=");
		builder.append(mMethod);
		builder.append(", authorization=");
		builder.append(authorization);
		builder.append(", mHeaders=");
		builder.append(mHeaders);
		builder.append(", mParameters=");
		builder.append(mParameters);
		builder.append("]");
		return builder.toString();
	}

	public static boolean parametersContainsFile(final List<Parameter> params) {
		if (CollectionUtils.isEmpty(params)) {
			return false;
		}
		boolean containsFile = false;
		for (final Parameter param : params) {
			if (param.hasFile()) {
				containsFile = true;
				break;
			}
		}
		return containsFile;
	}

}
