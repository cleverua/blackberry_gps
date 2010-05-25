package com.cleverua.bb.gps;

public class GPSException extends Exception {
    private Throwable cause;
    
    public GPSException(Throwable cause) {
        super();
        this.cause = cause;
    }
    
    public GPSException(String message) {
        super();
        this.cause = new Throwable(message);
    }
    
    public Throwable getCause() {
        return cause;
    }
    
    public String getMessage() {
        return cause.getMessage();
    }
}
