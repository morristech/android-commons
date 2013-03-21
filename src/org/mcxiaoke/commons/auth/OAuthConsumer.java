package org.mcxiaoke.commons.auth;

/**
 * @author mcxiaoke
 * 
 */
public class OAuthConsumer {
	private final String key;
	private final String secret;
	private final String callbackUrl;

	public OAuthConsumer(String key, String secret, String callbackUrl) {
		this.key = key;
		this.secret = secret;
		this.callbackUrl = callbackUrl;
	}

	public String getKey() {
		return key;
	}

	public String getSecret() {
		return secret;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OAuthConsumer [key=");
		builder.append(key);
		builder.append(", secret=");
		builder.append(secret);
		builder.append(", callbackUrl=");
		builder.append(callbackUrl);
		builder.append("]");
		return builder.toString();
	}

}
