package com.luckysweetheart.exception;

/**
 * @Description: 返回的异常信息处理
 * @author yfx
 * @date 2015年9月10日 上午9:32:12 
 */
public class ResultInfoException extends RuntimeException{
	
	private static final long serialVersionUID = -962336849220646170L;
	
	private String resultCode;

	public ResultInfoException() {
		super();
	}

	public ResultInfoException(String resultCode, String message) {
		super(message);
		this.resultCode=resultCode;
	}
	
	public ResultInfoException(String resultCode, String message, Throwable cause) {
		super(message, cause);
		this.resultCode=resultCode;
	}
	
	public ResultInfoException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getResultCode() {
		return resultCode;
	}

}
