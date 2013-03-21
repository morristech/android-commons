/**
 * 
 */
package org.mcxiaoke.commons.auth;

/**
 * @author mcxiaoke
 * 
 */
public final class OAuthToken {
	private String token; // accesstoken or username
	private String secret; // accesstokensecret or password or refreshtoken
	private String rawResponse;
	private long expiresAt;

	public OAuthToken(String response) {
		this.rawResponse = response;
	}

	public OAuthToken(String token, String secret) {
		this.token = token;
		this.secret = secret;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getToken() {
		return this.token;
	}

	public String getSecret() {
		return this.secret;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}

	public long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OAuthToken [token=");
		builder.append(token);
		builder.append(", secret=");
		builder.append(secret);
		builder.append(", rawResponse=");
		builder.append(rawResponse);
		builder.append(", expiresAt=");
		builder.append(expiresAt);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (expiresAt ^ (expiresAt >>> 32));
		result = prime * result
				+ ((rawResponse == null) ? 0 : rawResponse.hashCode());
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
		OAuthToken other = (OAuthToken) obj;
		if (expiresAt != other.expiresAt)
			return false;
		if (rawResponse == null) {
			if (other.rawResponse != null)
				return false;
		} else if (!rawResponse.equals(other.rawResponse))
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

}
