package org.mcxiaoke.commons.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.protocol.HTTP;
import org.mcxiaoke.commons.http.Parameter;
import org.mcxiaoke.commons.http.SimpleRequest;
import org.mcxiaoke.commons.http.util.URIUtilsEx;
import org.mcxiaoke.commons.util.Base64;
import org.mcxiaoke.commons.util.StringUtils;

/**
 * Authorization Utilities
 * 
 * @author mcxiaoke
 * 
 */
public final class AuthService {
	private final static Random RAND = new Random();
	private static final String PARAMETER_SEPARATOR = "&";
	private static final String NAME_VALUE_SEPARATOR = "=";
	private static final String EMPTY_STRING = "";
	private static final String OAUTH_SCHEME = "OAuth";
	private static final String BASIC_SCHEME = "Basic";

	private final Authorization mOAuthConfig;

	private AuthService(Authorization config) {
		this.mOAuthConfig = config;
	}

	public Authorization getOAuthConfig() {
		return mOAuthConfig;
	}

	public static void authorize(final SimpleRequest request) {
		final Authorization authorization = request.getAuthorization();
		if (request == null || authorization == null
				|| !authorization.isAuthorized()) {
			return;
		}
		int type = authorization.getAuthType();
		if (type == Authorization.BASIC) {
			addBasicAuthToHeaders(request);
		} else if (type == Authorization.OAUTH1) {
			final SignatureStyle style = authorization.getSignatureStyle();
			if (style == SignatureStyle.QUERY_STRING) {
				addOAuthToQueryParamters(request);
			} else {
				addOAuthToHeaders(request);
			}
		} else if (type == Authorization.OAUTH2) {
			final SignatureStyle style = authorization.getSignatureStyle();
			if (style == SignatureStyle.QUERY_STRING) {
				addOAuth2ToQueryParamters(request);
			} else {
				addOAuth2ToHeaders(style, request);
			}
		}
	}

	private static void addOAuthToQueryParamters(final SimpleRequest request) {
		final List<Parameter> oauthParameters = generateOAuthParameters(request
				.getAuthorization());
		oauthParameters.add(new Parameter(AuthConstants.OAUTH_SIGNATURE,
				getSignature(request, oauthParameters)));
		appendAuthorizationToQueryParameters(request, oauthParameters);
	}

	private static void addOAuth2ToQueryParamters(final SimpleRequest request) {
		String accessToken = request.getAuthorization().getToken();
		request.addQueryParameter(AuthConstants.OAUTH2_ACCESS_TOKEN,
				accessToken);
	}

	private static void addOAuthToHeaders(final SimpleRequest request) {
		final List<Parameter> oauthParameters = generateOAuthParameters(request
				.getAuthorization());
		oauthParameters.add(new Parameter(AuthConstants.OAUTH_SIGNATURE,
				getSignature(request, oauthParameters)));
		appendAuthorizationToHeaders(request, oauthParameters);
	}

	private static void addOAuth2ToHeaders(final SignatureStyle style,
			final SimpleRequest request) {
		final String headerPrefix = style.getTypeValue();
		String accessToken = request.getAuthorization().getToken();
		StringBuilder sb = new StringBuilder();
		sb.append(headerPrefix).append(" ").append(accessToken);
		request.addHeader(AuthConstants.HEADER, sb.toString());
	}

	private static void addBasicAuthToHeaders(final SimpleRequest request) {
		final Authorization authorization = request.getAuthorization();
		if (request == null || authorization == null
				|| !authorization.isAuthorized()) {
			return;
		}
		String userName = authorization.getToken();
		String password = authorization.getSecret();
		StringBuilder sb = new StringBuilder();
		sb.append(BASIC_SCHEME).append(" ");
		sb.append(userName).append(NAME_VALUE_SEPARATOR).append(password);
		String basicHeader = Base64.encodeBytes(getBytes(sb.toString()));
		request.addHeader(AuthConstants.HEADER, basicHeader);

	}

	private static void appendAuthorizationToQueryParameters(
			final SimpleRequest request, final List<Parameter> oauthParameters) {
		request.addQueryParameters(oauthParameters);
	}

	private static void appendAuthorizationToHeaders(
			final SimpleRequest request, final List<Parameter> oauthParameters) {
		request.addHeader(AuthConstants.HEADER,
				getAuthorizationHeader(oauthParameters));
	}

	private static String getAuthorizationHeader(
			final List<Parameter> authParameters) {
		StringBuilder sb = new StringBuilder();
		if (authParameters != null) {
			for (Parameter parameter : authParameters) {
				if (sb.length() > 0)
					sb.append(",");
				sb.append(" ");
				sb.append(percentEncode(parameter.getName())).append("=\"");
				sb.append(percentEncode(parameter.getValue())).append('"');
			}
		}
		sb.insert(0, OAUTH_SCHEME);
		return sb.toString();
	}

	private static String getSignature(final SimpleRequest request,
			final List<Parameter> oauthParameters) {
		final Authorization authorization = request.getAuthorization();
		String baseString = getBaseString(request, oauthParameters);
		SecretKeySpec secretKey = getSecretKeySpec(
				authorization.getApiSecret(), authorization.getSecret());
		return signature(baseString, secretKey);
	}

	private static String signature(final String data, final SecretKeySpec spec) {
		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance(AuthConstants.HMAC_SHA1);
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
		} catch (InvalidKeyException ike) {
			throw new AssertionError(ike);
		} catch (NoSuchAlgorithmException nsae) {
			throw new AssertionError(nsae);
		}
		return Base64.encodeBytes(byteHMAC);
	}

	private static SecretKeySpec getSecretKeySpec(final String consumerSecret,
			final String accessTokenSecret) {
		if (StringUtils.isEmpty(accessTokenSecret)) {
			String oauthSignature = encode(consumerSecret);
			return new SecretKeySpec(oauthSignature.getBytes(),
					AuthConstants.HMAC_SHA1);
		} else {
			String oauthSignature = encode(consumerSecret + "&"
					+ accessTokenSecret);
			return new SecretKeySpec(oauthSignature.getBytes(),
					AuthConstants.HMAC_SHA1);
		}
	}

	private static String getBaseString(final SimpleRequest request,
			final List<Parameter> oauthParameters) {
		StringBuilder sb = new StringBuilder();
		final String url = request.getUrl();
		String encodedUrl = percentEncode(url);
		final String method = request.getMethodName();
		String encodedMethod = percentEncode(method);
		List<Parameter> allParameters = new ArrayList<Parameter>();
		allParameters.addAll(request.getParameters());
		allParameters.addAll(request.getQueryParameters());
		allParameters.addAll(oauthParameters);
		final String encodedParameters = getSortedAndEncodedParametersAsString(allParameters);
		sb.append(encodedUrl).append("&").append(encodedMethod).append("&")
				.append(encodedParameters);
		return sb.toString();
	}

	private static String getSortedAndEncodedParametersAsString(
			final List<Parameter> parameters) {
		if (parameters == null || parameters.size() == 0) {
			return EMPTY_STRING;
		}
		ArrayList<Parameter> queryParameters = new ArrayList<Parameter>(
				parameters);
		Collections.sort(queryParameters);
		StringBuilder sb = new StringBuilder();
		for (Parameter pair : queryParameters) {
			sb.append(PARAMETER_SEPARATOR).append(pair.getEncoded());
		}
		return sb.substring(1).toString();
	}

	private static List<Parameter> generateOAuthParameters(
			final Authorization authorization) {
		final Authorization auth = authorization;
		final List<Parameter> oauthParamters = new ArrayList<Parameter>();
		if (auth != null) {
			if (StringUtils.isNotEmpty(auth.getToken())) {
				oauthParamters.add(new Parameter(AuthConstants.OAUTH_TOKEN,
						auth.getToken()));
			}
			oauthParamters.add(new Parameter(AuthConstants.OAUTH_CONSUMER_KEY,
					auth.getApiKey()));
			oauthParamters.add(new Parameter(
					AuthConstants.OAUTH_SIGNATURE_METHOD,
					AuthConstants.HMAC_SHA1));
			oauthParamters.add(new Parameter(AuthConstants.OAUTH_TIMESTAMP,
					computeTimestamp()));
			oauthParamters.add(new Parameter(AuthConstants.OAUTH_NONCE,
					computeNonce()));
			oauthParamters.add(new Parameter(AuthConstants.OAUTH_VERSION,
					AuthConstants.VERSION_1_0));
		}
		return oauthParamters;
	}

	public static String computeNonce() {
		return Long.toHexString(Math.abs(RAND.nextLong()));
	}

	public static String computeTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	private static byte[] getBytes(String text) {
		if (text == null) {
			return null;
		}
		try {
			return text.getBytes(HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static String percentEncode(String text) {
		if (text == null) {
			return EMPTY_STRING;
		}
		return URIUtilsEx.percentEncode(text);
	}

	private static String encode(String text) {
		if (text == null) {
			return EMPTY_STRING;
		}
		try {
			return URLEncoder.encode(text, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
