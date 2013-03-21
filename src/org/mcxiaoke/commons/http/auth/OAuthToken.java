/**
 * 
 */
package org.mcxiaoke.commons.http.auth;

/**
 * @author mcxiaoke
 * 
 */
public final class OAuthToken {
	private String token;
	private String secret;

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

}
