/**
 * 
 */
package org.mcxiaoke.commons.http;

/**
 * @author mcxiaoke
 * 
 */
public final class SimpleException extends Exception {

	private static final int IO_EXCEPTION = StatusCodes.STATUS_CODE_IO_ERROR;

	private static final long serialVersionUID = 1L;

	private int errorCode;
	private String errorMessage;

	public SimpleException() {
		super();
		initialize(IO_EXCEPTION, getMessage());
	}

	public SimpleException(int errorCode) {
		super();
		initialize(errorCode, getMessage());
	}

	public SimpleException(String errorMessage) {
		super();
		initialize(IO_EXCEPTION, errorMessage);
	}

	public SimpleException(int errorCode, String errorMessage,
			Throwable throwable) {
		super(errorMessage, throwable);
		initialize(errorCode, errorMessage);
	}

	public SimpleException(int errorCode, String errorMessage) {
		super(errorMessage);
		initialize(errorCode, errorMessage);
	}

	public SimpleException(int errorCode, Throwable throwable) {
		super(throwable);
		initialize(errorCode, getMessage());
	}

	private void initialize(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
