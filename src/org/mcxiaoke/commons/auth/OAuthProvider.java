package org.mcxiaoke.commons.auth;

/**
 * @author mcxiaoke
 * 
 */
public class OAuthProvider {
	private final String callbackUrl;
	private final String authorizeUrl;
	private final String requestTokenUrl;
	private final String accessTokenUrl;
	private final int version;
	private final OAuthParameterStyle parameterStyle;

	public OAuthProvider(Builder builder) {
		this.callbackUrl = builder.callbackUrl;
		this.authorizeUrl = builder.authorizeUrl;
		this.requestTokenUrl = builder.requestTokenUrl;
		this.accessTokenUrl = builder.accessTokenUrl;
		this.version = builder.version;
		this.parameterStyle = builder.parameterStyle;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public String getAuthorizeUrl() {
		return authorizeUrl;
	}

	public String getRequestTokenUrl() {
		return requestTokenUrl;
	}

	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}

	public int getVersion() {
		return version;
	}

	public OAuthParameterStyle getParameterStyle() {
		return parameterStyle;
	}

	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("OAuthConsumer [");
		builder2.append(", callbackUrl=");
		builder2.append(callbackUrl);
		builder2.append(", authorizeUrl=");
		builder2.append(authorizeUrl);
		builder2.append(", requestTokenUrl=");
		builder2.append(requestTokenUrl);
		builder2.append(", accessTokenUrl=");
		builder2.append(accessTokenUrl);
		builder2.append(", version=");
		builder2.append(version);
		builder2.append(", parameterStyle=");
		builder2.append(parameterStyle);
		builder2.append("]");
		return builder2.toString();
	}

	public static class Builder {
		String callbackUrl;
		String authorizeUrl;
		String requestTokenUrl;
		String accessTokenUrl;
		int version;
		OAuthParameterStyle parameterStyle;

		public Builder() {
		}

		public Builder callback(String callbackUrl) {
			this.callbackUrl = callbackUrl;
			return this;
		}

		public Builder authorizeUrl(String authorizeUrl) {
			this.authorizeUrl = authorizeUrl;
			return this;
		}

		public Builder requestTokenUrl(String requestTokenUrl) {
			this.requestTokenUrl = requestTokenUrl;
			return this;
		}

		public Builder accessTokenUrl(String accessTokenUrl) {
			this.accessTokenUrl = accessTokenUrl;
			return this;
		}

		public Builder version(int version) {
			this.version = version;
			return this;
		}

		public Builder version(OAuthParameterStyle parameterStyle) {
			this.parameterStyle = parameterStyle;
			return this;
		}

		public OAuthProvider build() {
			return new OAuthProvider(this);
		}

	}

}
