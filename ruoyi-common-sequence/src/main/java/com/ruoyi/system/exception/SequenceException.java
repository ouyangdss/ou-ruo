package com.ruoyi.system.exception;

/**
 * @author dss
 * @version 1.0.0
 * @description 序列号异常
 * @className SequenceException.java
 * @createTime 2020年12月16日 16:55:00
 */
public class SequenceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SequenceException() {
        super();
    }

    public SequenceException(String message, Throwable cause) {
        super (message, cause);
    }

    public SequenceException(String message) {
        super (message);
    }
    public SequenceException (Throwable cause){
        super (cause);
    }

    public Throwable getRootCause() {
        Throwable rootCause = null;
        Throwable cause = getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            cause = cause.getCause();
        }
        return rootCause;
    }

    public Throwable getMostSpecificCause() {
        Throwable rootCause = getRootCause();
        return (rootCause != null ? rootCause: this);
    }

    public boolean contains(Class<?> exType) {
        if (exType == null) {
            return false;
        }
        if (exType.isInstance( this)) {
            return true;
        }

        Throwable cause = getCause();
        if (cause == this) {
            return false;
        }
        if (cause instanceof SequenceException) {
            return ((SequenceException) cause).contains (exType);
        } else {
            while (cause != null) {
                if (exType.isInstance(cause)) { return true;
                }
                if (cause.getCause() == cause) {
                    break;
                }
                cause = cause.getCause();
            }
            return false;
        }
    }
}
