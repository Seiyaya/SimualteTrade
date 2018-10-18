package com.seiyaya.common.exception;

/**
 * 
 * @author Seiyaya
 *
 */
public class CommonException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int errorType;//
	
	public int getErrorType() {
		return errorType;
	}

	public CommonException(int errorType) {
		super();
		this.errorType = errorType;
	}

	public CommonException(int errorType, String errMsg) {
		super(errMsg);
		this.errorType = errorType;
	}

	public CommonException(int errorType, Throwable t) {
		super(t);
		this.errorType = errorType;
	}

	public CommonException(int errorType, String errMsg, Throwable t) {
		super(errMsg, t);
		this.errorType = errorType;
	}
}
