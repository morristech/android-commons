/**
 * 
 */
package org.mcxiaoke.commons.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.mcxiaoke.commons.util.LogUtils;

/**
 * @author mcxiaoke
 * 
 */
public class SimpleResponse {
	private static final boolean DEBUG = true;
	private static final String TAG = SimpleResponse.class.getSimpleName();

	private static void debug(String message) {
		LogUtils.v(TAG, message);
	}

	private static final long INVALID_CONTENT_LENGTH = -1L;

	private final StatusLine statusLine;
	private final int statusCode;
	private final String statusMessage;
	private final Header[] headers;
	private final HttpResponse response;
	private final BufferedHttpEntity entity;

	private boolean empty;
	private Exception exception;

	public SimpleResponse() {
		this.response = null;
		this.statusLine = null;
		this.statusCode = 0;
		this.statusMessage = null;
		this.headers = null;
		this.entity = null;
	}

	public SimpleResponse(final HttpResponse response) throws IOException {
		this.response = response;
		this.statusLine = this.response.getStatusLine();
		this.statusCode = this.statusLine.getStatusCode();
		this.statusMessage = this.statusLine.getReasonPhrase();
		this.headers = response.getAllHeaders();
		this.entity = wrapEntity(response);
		// this.entity = new BufferedHttpEntity(response.getEntity());
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public boolean isSuccess() {
		return StatusCodes.isSuccess(statusCode);
	}

	public boolean isRedirect() {
		return StatusCodes.isRedirect(statusCode);
	}

	public boolean isRequestError() {
		return StatusCodes.isRequestError(statusCode);
	}

	public boolean isServerError() {
		return StatusCodes.isServerError(statusCode);
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public long getContentLength() {
		if (this.entity != null) {
			return this.entity.getContentLength();
		} else {
			return INVALID_CONTENT_LENGTH;
		}
	}

	public InputStream getContent() throws IOException {
		if (this.entity != null) {
			return this.entity.getContent();
		} else {
			return null;
		}
	}

	public String getAsString() throws IOException {
		if (this.entity != null) {
			return EntityUtils.toString(this.entity, HTTP.UTF_8);
		} else {
			return null;
		}
	}

	public byte[] getAsByteArray() throws IOException {
		if (this.entity != null) {
			return EntityUtils.toByteArray(this.entity);
		} else {
			return null;
		}
	}

	private BufferedHttpEntity wrapEntity(final HttpResponse response) {
		BufferedHttpEntity wrappedEntity = null;
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			try {
				wrappedEntity = new BufferedHttpEntity(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wrappedEntity;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		String content = null;
		try {
			content = getAsString();
		} catch (IOException e) {
			content = "" + e;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("SimpleResponse [statusLine=");
		builder.append(statusLine);
		builder.append(", statusCode=");
		builder.append(statusCode);
		builder.append(", statusMessage=");
		builder.append(statusMessage);
		builder.append(", headers=");
		builder.append(headers != null ? Arrays.asList(headers).subList(0,
				Math.min(headers.length, maxLen)) : null);
		builder.append(", response=");
		builder.append(content);
		builder.append(", empty=");
		builder.append(empty);
		builder.append(", exception=");
		builder.append(exception);
		builder.append("]");
		return builder.toString();
	}

}
