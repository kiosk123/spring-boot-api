package com.study.springboot.exception;

public class NotEnoughStockException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = -3772843093603474151L;

    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

    
    
}
