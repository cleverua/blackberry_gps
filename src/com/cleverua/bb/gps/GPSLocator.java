package com.cleverua.bb.gps;

import java.util.Vector;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

public class GPSLocator implements LocationListener {
    public static final int LOCATION_PROVIDER_UPDATE_TIMEOUT    = 5;
    public static final int LOCATION_PROVIDER_UPDATE_INTERVAL   = 5;
    public static final int LOCATION_PROVIDER_UPDATE_MAXAGE     = -1; // default value
    
    private static final String PROVIDER_AVAILABLE_MSG               = "Location provider is available.";
    private static final String LOCATION_EXCEPTOIN_MSG               = "All location providers are currently permanently unavailable!";
    private static final String PROVIDER_TEMPORARILY_UNAVAILABLE_MSG = "Location provider is temporarily unavailable!";
    private static final String PROVIDER_OUT_OF_SERVICE_MSG          = "The location provider is permanently unavailable!";
    private static final String PROVIDER_NULL_MSG                    = "Can not find the location provider that meets the defined criteria!";
    private static final String PROVIDER_UNKNOWN_STATE_MSG           = "Unknown provider state";
    
    private Vector providerStateListeners;
    private LocationProvider provider;
    private int state;

    public static String getStateMessage(int state) {
        switch (state) {
            case LocationProvider.OUT_OF_SERVICE: 
                return PROVIDER_OUT_OF_SERVICE_MSG;
            case LocationProvider.TEMPORARILY_UNAVAILABLE: 
                return PROVIDER_TEMPORARILY_UNAVAILABLE_MSG;
            case LocationProvider.AVAILABLE: 
                return PROVIDER_AVAILABLE_MSG;
            default: 
                return PROVIDER_UNKNOWN_STATE_MSG;
        }
    }
    
    public GPSLocator() {
        providerStateListeners = new Vector();
    }
    
    public void init(Criteria criteria) throws GPSException {
        try {
            provider = LocationProvider.getInstance(criteria);
        } catch (LocationException e) {
            StringBuffer message = new StringBuffer();
            message.append(LOCATION_EXCEPTOIN_MSG).append(' ').append(e);
            throw new GPSException(message.toString());
        }
        
        if (provider == null) {
            throw new GPSException(PROVIDER_NULL_MSG);
        }
        
        final int providerState = provider.getState();
        if (providerState == LocationProvider.AVAILABLE) {
            provider.setLocationListener(this,
                    LOCATION_PROVIDER_UPDATE_INTERVAL,
                    LOCATION_PROVIDER_UPDATE_TIMEOUT,
                    LOCATION_PROVIDER_UPDATE_MAXAGE);
        } else {
            throw new GPSException(getStateMessage(providerState));
        }
    }
    
    public void reset() {
        if (provider != null) {
            provider.reset();
            provider.setLocationListener(null, -1, -1, -1);
        }
    }
    
    public void addProviderStateListener(GPSLocatorListener listener) {
        providerStateListeners.addElement(listener);
    }
    
    public void removeProviderStateListener(GPSLocatorListener listener) {
        providerStateListeners.removeElement(listener);
    }
    
    public void locationUpdated(LocationProvider provider, Location location) {
        final int newState = provider.getState();
        if (newState != state) {
            providerStateChanged(provider, newState);
        }
        
        if ((newState == LocationProvider.AVAILABLE) && (location.isValid())) {
            updateLocationListeners(location);
        } else {
            updateLocationListeners(null);
        }
    }

    public void providerStateChanged(LocationProvider provider, int state) {
        this.state = state;
        
        final int size = providerStateListeners.size();
        for (int i = 0;  i < size; i++) {
            ((GPSLocatorListener) providerStateListeners.elementAt(i)).stateChanged(state);
        }
    }
    
    private void updateLocationListeners(Location location) {
        final int size = providerStateListeners.size();
        for (int i = 0;  i < size; i++) {
            ((GPSLocatorListener) providerStateListeners.elementAt(i)).locationUpdated(location);
        }
    }
    
}
