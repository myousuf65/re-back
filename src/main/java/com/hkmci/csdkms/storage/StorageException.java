package com.hkmci.csdkms.storage;

public class StorageException extends RuntimeException {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 3027835499230278623L;

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
