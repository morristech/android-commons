package org.mcxiaoke.commons.auth;

public enum SignatureStyle {
	HEADER_BEARER("Bearer"), HEADER_OAUTH("OAuth2"), HEADER_MAC("MAC"), QUERY_STRING(
			"QueryString");

	private String typeValue;

	private SignatureStyle(String typeValue) {
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return typeValue;
	}
}
