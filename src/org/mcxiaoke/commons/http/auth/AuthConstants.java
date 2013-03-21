package org.mcxiaoke.commons.http.auth;

/**
 * Authorization constants
 * 
 * @author mcxiaoke
 * 
 */
public class AuthConstants {
	private AuthConstants() {
	}

	public enum OAuthVersion {
		BASIC, OAUTH1, OAUTH2;
	}

	public enum OAuthParameterStyle {
		AUTHORIZATION_HEADER, QUERY_STRING;
	}

	public enum ResponseType {
		CODE("code"), TOKEN("token"), CODE_AND_TOKEN("code_and_token");

		private String typeValue;

		private ResponseType(String typeValue) {
			this.typeValue = typeValue;
		}

		public String getTypeValue() {
			return typeValue;
		}
	}

	public enum GrantType {
		AUTHORIZATION_CODE("authorization_code"), RESOURCE_OWNER_PASSWORD_CREDENTIALS(
				"password"), IMPLICIT(""), REFRESH_TOKEN("refresh_token");

		private String typeValue;

		private GrantType(String typeValue) {
			this.typeValue = typeValue;
		}

		public String getTypeValue() {
			return typeValue;
		}
	}

	// common
	public static final String HEADER = "Authorization";
	public static final String FORM_ENCODED = "application/x-www-form-urlencoded";
	public static final String ENCODING = "UTF-8";

	public static final String HMAC_SHA1 = "HMAC-SHA1";
	public static final String RSA_SHA1 = "RSA-SHA1";

	// OAuth 1.0
	public static final String VERSION_1_0 = "1.0";

	public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
	public static final String OAUTH_TOKEN = "oauth_token";
	public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";
	public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
	public static final String OAUTH_SIGNATURE = "oauth_signature";
	public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
	public static final String OAUTH_NONCE = "oauth_nonce";
	public static final String OAUTH_VERSION = "oauth_version";
	public static final String OAUTH_CALLBACK = "oauth_callback";
	public static final String OAUTH_CALLBACK_CONFIRMED = "oauth_callback_confirmed";
	public static final String OAUTH_VERIFIER = "oauth_verifier";
	public static final String PARAM_PREFIX = "oauth_";
	public static final String OUT_OF_BAND = "oob";

	// OAuth 2.0
	public static final String VERSION_2_0 = "2.0";
	public static final String OAUTH2_ACCESS_TOKEN = "access_token";
	public static final String OAUTH2_CLIENT_ID = "client_id";
	public static final String OAUTH2_CLIENT_SECRET = "client_secret";
	public static final String OAUTH2_REDIRECT_URI = "redirect_uri";
	public static final String OAUTH2_CODE = "code";
	public static final String OAUTH2_RESPONSE_TYPE = "response_type";
	public static final String OAUTH2_GRANT_TYPE = "grant_type";
	public static final String OAUTH2_DISPLAY_TYPE = "display";
	public static final String OAUTH2_STATE = "state";
	public static final String OAUTH2_EXPIRES_IN = "expires_in";
	public static final String OAUTH2_ERROR = "error";
	public static final String OAUTH2_ERROR_DESCRIPTION = "error_description";
	public static final String OAUTH2_ERROR_URI = "error_uri";

	public static final String OAUTH2_ASSERTION_TYPE = "assertion_type";
	public static final String OAUTH2_REFRESH_TOKEN = "refresh_token";
	public static final String OAUTH2_TOKEN_TYPE = "token_type";

	public static final String OAUTH2_USERNAME = "username";
	public static final String OAUTH2_PASSWORD = "password";

}
