package org.mcxiaoke.commons.http.auth;

import org.mcxiaoke.commons.http.auth.AuthConstants.OAuthParameterStyle;
import org.mcxiaoke.commons.util.StringUtils;

/**
 * Authorization Configs
 * 
 * @author mcxiaoke
 * 
 */
public class AuthConfig implements java.io.Serializable {
	private static final long serialVersionUID = -413250347293002921L;

	public static final int BASIC = 0;
	public static final int OAUTH1 = 1;
	public static final int OAUTH2 = 2;

	private int authType;
	private String apiKey;
	private String apiSecret;
	private OAuthParameterStyle parameterStyle = OAuthParameterStyle.AUTHORIZATION_HEADER;
	private String token;
	private String secret;;

	public AuthConfig(int type) {
		this.authType = type;
	}

	public AuthConfig(String token) {
		this.authType = OAUTH2;
		this.token = token;
	}

	public AuthConfig(String userName, String password) {
		this.authType = BASIC;
		this.apiKey = userName;
		this.apiSecret = password;
	}

	public AuthConfig(String apiKey, String apiSecret, String token,
			String secret) {
		this.authType = OAUTH1;
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
		return authType;
	}

	public OAuthParameterStyle getParameterStyle() {
		return parameterStyle;
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

	public void setParameterStyle(OAuthParameterStyle style) {
		this.parameterStyle = style;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isAuthorized() {
		boolean authorized = false;
		switch (authType) {
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
		result = prime * result + authType;
		result = prime * result
				+ ((parameterStyle == null) ? 0 : parameterStyle.hashCode());
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
		AuthConfig other = (AuthConfig) obj;
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
		if (authType != other.authType)
			return false;
		if (parameterStyle != other.parameterStyle)
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
		builder.append(authType);
		builder.append(", apiKey=");
		builder.append(apiKey);
		builder.append(", apiSecret=");
		builder.append(apiSecret);
		builder.append(", parameterStyle=");
		builder.append(parameterStyle);
		builder.append(", token=");
		builder.append(token);
		builder.append(", secret=");
		builder.append(secret);
		builder.append("]");
		return builder.toString();
	}

}