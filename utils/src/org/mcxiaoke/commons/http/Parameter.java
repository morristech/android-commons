package org.mcxiaoke.commons.http;

import java.io.File;

import org.apache.http.NameValuePair;
import org.mcxiaoke.commons.http.util.URIUtilsEx;

/**
 * @author mcxiaoke
 * @version 1.0 2011.05.02
 * @version 1.1 2011.05.03
 * @version 1.2 2011.05.04
 * @version 2.0 2011.11.03
 * 
 */
public final class Parameter implements NameValuePair, Comparable<Parameter> {

	private final String name;
	private final String value;
	private final File file;

	public static Parameter of(final NameValuePair pair) {
		return new Parameter(pair.getName(), pair.getValue());
	}

	public static Parameter of(final String name, final File file) {
		return new Parameter(name, file);
	}

	public Parameter(final String name, final String value) {
		this.file = null;
		this.name = name;
		this.value = value;
	}

	public Parameter(final String name, final File file) {
		this.file = file;
		this.name = name;
		this.value = file.getName();
	}

	@Override
	public int compareTo(final Parameter that) {
		int compared;
		compared = this.name.compareTo(that.getName());
		if (0 == compared) {
			compared = this.value.compareTo(that.getValue());
		}
		return compared;
	}

	public File getFile() {
		return this.file;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	public String getEncodedString() {
		return URIUtilsEx.percentEncode(name + "=" + value);
	}

	public boolean hasFile() {
		return this.file != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
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
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Parameter [name=");
		builder.append(name);
		builder.append(", value=");
		builder.append(value);
		builder.append(", file=");
		builder.append(file);
		builder.append("]");
		return builder.toString();
	}
}
