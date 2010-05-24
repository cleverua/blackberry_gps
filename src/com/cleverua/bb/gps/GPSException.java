package com.cleverua.bb.gps;

public class GPSException extends Exception {
    private Throwable cause;
    
    public GPSException(Throwable cause) {
        super();
        this.cause = cause;
    }
    
    public Throwable getCause() {
        return cause;
    }
}
