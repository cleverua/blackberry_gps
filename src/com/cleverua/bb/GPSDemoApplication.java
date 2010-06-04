package com.cleverua.bb;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.cleverua.bb.gps.GPSException;
import com.cleverua.bb.gps.GPSLocator;

public class GPSDemoApplication extends UiApplication {
    private static GPSDemoApplication instance;
    private static GPSLocator gpsLocator;
    
    public static void main(String[] args) {
        instance = new GPSDemoApplication();
        instance.startGPSListening();
        instance.pushScreen(new GPSDemoScreen());
        instance.enterEventDispatcher();
    }
    
    public static void exit() {
        instance.stopGPSListening();
        System.exit(0);
    }
    
    public static GPSLocator getGPSLocator() {
        return gpsLocator;
    }

    private void startGPSListening() {
        try {
            gpsLocator.init(null);
        } catch (GPSException e) {
            alert(e.getMessage());
        }
    }

    private void stopGPSListening() {
        gpsLocator.reset();
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
    
    private GPSDemoApplication() {
        gpsLocator = new GPSLocator();
    }
}
