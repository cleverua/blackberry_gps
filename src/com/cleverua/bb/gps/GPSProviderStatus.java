package com.cleverua.bb.gps;


public class GPSProviderStatus {
    private boolean available;
    private String statusMessage;
    
    public GPSProviderStatus(boolean available, String statusMessage) {
        this.available = available;
        this.statusMessage = statusMessage;
    }
    
    public boolean isGPSAvailable() {
        return available;
    }

    public void setGPSAvailable(boolean available) {
        this.available = available;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String toString() {
        return statusMessage;
    }
}
