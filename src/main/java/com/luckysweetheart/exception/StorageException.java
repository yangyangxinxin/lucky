package com.luckysweetheart.exception;

/**
 * Created by wlinguo on 14-4-14.
 */
public class StorageException extends Exception {
	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageException(Throwable cause) {
		super(cause);
	}
}
