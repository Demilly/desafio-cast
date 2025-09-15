package br.com.application.exception;

public class OptimisticLockRetryException extends RuntimeException {
    public OptimisticLockRetryException(String message) {
        super(message);
    }

    public OptimisticLockRetryException(String message, Throwable cause) {
        super(message, cause);
    }
}