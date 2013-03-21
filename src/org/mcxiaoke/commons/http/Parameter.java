/**
 * 
 */
package org.mcxiaoke.commons.http;

import org.apache.http.NameValuePair;
import org.mcxiaoke.commons.http.util.URIUtilsEx;

/**
 * @author mcxiaoke
 * 
 */
public final class Parameter implements NameValuePair,
		Comparable<Parameter> {
	private final String name;
	private final String value;

	public Parameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public Parameter(NameValuePair pair) {
		this.name = pair.getName();
		this.value = pair.getValue();
	}

	public String getEncoded() {
		StringBuilder sb = new StringBuilder();
		sb.append(URIUtilsEx.percentEncode(name)).append("=")
				.append(URIUtilsEx.percentEncode(value));
		return sb.toString();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int compareTo(Parameter another) {
		int nameDiff = name.compareTo(another.name);
		return nameDiff != 0 ? nameDiff : value.compareTo(another.value);
	}

}
