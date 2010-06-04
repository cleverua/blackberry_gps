package com.cleverua.bb;

import javax.microedition.location.Location;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.cleverua.bb.gps.GPSException;
import com.cleverua.bb.gps.GPSLocator;
import com.cleverua.bb.gps.GPSLocatorListener;

public class GPSDemoApplication extends UiApplication {
    public static final int LOCATION_UPDATE_TIMEOUT    = 5;
    public static final int LOCATION_UPDATE_INTERVAL   = 5;
    public static final int LOCATION_UPDATE_MAXAGE     = -1; //use default values
    
    private static GPSDemoApplication instance;
    
    private GPSLocator gpsLocator;
    
    public static void main(String[] args) {
        instance = new GPSDemoApplication();
        instance.startGPSListening();
        instance.pushScreen(new GPSDemoScreen());
        instance.enterEventDispatcher();
    }
    
    public static GPSDemoApplication getInstance() {
        return instance;
    }

    public static void exit() {
        instance.stopGPSListening();
        System.exit(0);
    }

    public Location getLocation() {
        return gpsLocator.getLocation();
    }

    public int getState() {
        return gpsLocator.getState();
    }
    
    public void addLocatorListener(GPSLocatorListener listener) {
        gpsLocator.addLocatorListener(listener);
    }
    
    public void removeLocatorListener(GPSLocatorListener listener) {
        gpsLocator.removeLocatorListener(listener);
    }
    
    private void startGPSListening() {
        try {
            gpsLocator.init(null, 
                    LOCATION_UPDATE_INTERVAL,
                    LOCATION_UPDATE_TIMEOUT,
                    LOCATION_UPDATE_MAXAGE);
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
