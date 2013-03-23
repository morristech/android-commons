package org.mcxiaoke.commons.auth;

import org.mcxiaoke.commons.util.StringUtils;

/**
 * Authorization Configs
 * 
 * @author mcxiaoke
 * 
 */
public class Authorization implements java.io.Serializable {
	private static final long serialVersionUID = -413250347293002921L;

	public static final int BASIC = 0;
	public static final int OAUTH1 = 1;
	public static final int OAUTH2 = 2;

	private int type;
	private String apiKey;
	private String apiSecret;
	private SignatureStyle signatureStyle = SignatureStyle.QUERY_STRING;
	private String token;
	private String secret;;

	public Authorization(int type) {
		this.type = type;
	}

	public Authorization(String token) {
		this.type = OAUTH2;
		this.token = token;
	}

	public Authorization(String userName, String password) {
		this.type = BASIC;
		this.apiKey = userName;
		this.apiSecret = password;
	}

	public Authorization(String apiKey, String apiSecret, String token,
			String secret) {
		this.type = OAUTH1;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.token = token;
		this.secret = secret;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public int getAuthType() {
		return type;
	}

	public SignatureStyle getSignatureStyle() {
		return signatureStyle;
	}

	public String getSecret() {
		return secret;
	}

	public String getToken() {
		return token;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public void setSignatureStyle(SignatureStyle style) {
		this.signatureStyle = style;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isAuthorized() {
		boolean authorized = false;
		switch (type) {
		case BASIC:
			authorized = (StringUtils.isNotEmpty(getToken()) && StringUtils
					.isNotEmpty(getSecret()));
			break;
		case OAUTH1:
			authorized = (StringUtils.isNotEmpty(getApiKey())
					&& StringUtils.isNotEmpty(getApiSecret())
					&& StringUtils.isNotEmpty(getToken()) && StringUtils
					.isNotEmpty(getSecret()));
			break;
		case OAUTH2:
			authorized = (StringUtils.isNotEmpty(getToken()));
			break;
		default:
			break;
		}
		return authorized;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiKey == null) ? 0 : apiKey.hashCode());
		result = prime * result
				+ ((apiSecret == null) ? 0 : apiSecret.hashCode());
		result = prime * result + type;
		result = prime * result
				+ ((signatureStyle == null) ? 0 : signatureStyle.hashCode());
		result = prime * result + ((secret == null) ? 0 : secret.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Authorization other = (Authorization) obj;
		if (apiKey == null) {
			if (other.apiKey != null)
				return false;
		} else if (!apiKey.equals(other.apiKey))
			return false;
		if (apiSecret == null) {
			if (other.apiSecret != null)
				return false;
		} else if (!apiSecret.equals(other.apiSecret))
			return false;
		if (type != other.type)
			return false;
		if (signatureStyle != other.signatureStyle)
			return false;
		if (secret == null) {
			if (other.secret != null)
				return false;
		} else if (!secret.equals(other.secret))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthConfig [authType=");
		builder.append(type);
		builder.append(", apiKey=");
		builder.append(apiKey);
		builder.append(", apiSecret=");
		builder.append(apiSecret);
		builder.append(", parameterStyle=");
		builder.append(signatureStyle);
		builder.append(", token=");
		builder.append(token);
		builder.append(", secret=");
		builder.append(secret);
		builder.append("]");
		return builder.toString();
	}

}