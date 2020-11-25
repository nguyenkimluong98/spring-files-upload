package com.viettel.luongnk.main.exception;

/**
 * @author: luongnk
 * @since: 25/11/2020
 */

public class DocumentStorageException extends RuntimeException {
    public DocumentStorageException(String message) {
        super(message);
    }
    public DocumentStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
