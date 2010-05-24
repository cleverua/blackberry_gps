package com.cleverua.bb;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.cleverua.bb.gps.GPSException;
import com.cleverua.bb.gps.GPSLocationListener;
import com.cleverua.bb.gps.GPSProviderStatus;
import com.cleverua.bb.gps.ProviderStateListener;

public class GPSDemoApplication extends UiApplication implements ProviderStateListener {
    private static GPSDemoApplication instance;
    private static GPSLocationListener locationListener;
    
    public static void main(String[] args) {
        instance = new GPSDemoApplication();
        instance.startGPSLocationUpdate();
        instance.enterEventDispatcher();
    }
    
    public static void exit() {
        instance.stopGPGLocationUpdate();
        locationListener.removeProviderStateListener(instance);
        System.exit(0);
    }
    
    private void startGPSLocationUpdate() {
        GPSProviderStatus status = GPSLocationListener.isGPSAvailable();
        if (!status.isGPSAvailable()) {
            alert(status.getStatusMessage());
        } else {
            try {
                locationListener.initLocationListener();
                locationListener.addProviderStateListener(instance);
            } catch (GPSException e) {
                alert("Unable to init GPS!", e);
            }
        }
    }

    private void stopGPGLocationUpdate() {
        locationListener.resetLocationListener();
    }
    
    public static void alert(final String message) {
        instance.invokeLater(new Runnable() {
            public void run() {
                Dialog.alert(message);
            }
        });
    }
    
    public static void alert(final String message, final Exception e) {
        StringBuffer errorMessage = new StringBuffer();
        errorMessage.append(message).append(' ').append('(').append(e).append(')');
        alert(errorMessage.toString());
    }
    
    public void providerStatusChanged(GPSProviderStatus status) {
        alert(status.getStatusMessage());
    }
    
    private GPSDemoApplication() {
        locationListener = new GPSLocationListener();
        locationListener.addProviderStateListener(this);
    }
}
