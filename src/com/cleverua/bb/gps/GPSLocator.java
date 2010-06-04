package com.cleverua.bb.gps;

import java.util.Vector;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

/**
 * Wrapper over the Location Provider
 */
public class GPSLocator implements LocationListener {
    private static final int DEFAULT_TIMEOUT = 5;
    public static final int LOCATION_PROVIDER_UPDATE_TIMEOUT    = 5;
    public static final int LOCATION_PROVIDER_UPDATE_INTERVAL   = 5;
    public static final int LOCATION_PROVIDER_UPDATE_MAXAGE     = -1; // default value
    
    private static final String PROVIDER_AVAILABLE_MSG               = "Location provider is available.";
    private static final String LOCATION_EXCEPTOIN_MSG               = "All location providers are currently permanently unavailable!";
    private static final String PROVIDER_TEMPORARILY_UNAVAILABLE_MSG = "Location provider is temporarily unavailable!";
    private static final String PROVIDER_OUT_OF_SERVICE_MSG          = "The location provider is permanently unavailable!";
    private static final String PROVIDER_NULL_MSG                    = "Can not find the location provider that meets the defined criteria!";
    private static final String PROVIDER_UNDEFINED_STATE_MSG         = "Provider state is undefined";
    
    private Vector providerStateListeners;
    private LocationProvider provider;
    private int state;

    /**
     * Returns the locator's message for the location provider's state.
     */
    public static String getStateMessage(int state) {
        switch (state) {
            case LocationProvider.OUT_OF_SERVICE: 
                return PROVIDER_OUT_OF_SERVICE_MSG;
            case LocationProvider.TEMPORARILY_UNAVAILABLE: 
                return PROVIDER_TEMPORARILY_UNAVAILABLE_MSG;
            case LocationProvider.AVAILABLE: 
                return PROVIDER_AVAILABLE_MSG;
            default: 
                return PROVIDER_UNDEFINED_STATE_MSG;
        }
    }
    
    public GPSLocator() {
        providerStateListeners = new Vector();
    }
    
    /**
     * Initialize the GPS locator.
     * @param criteria - GPS criteria for initialization.
     * @throws GPSException if GPS provider is unavailable 
     * or there are no providers can meet the given criteria. 
     */
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
    
    /**
     * Resets the GPS locator
     */
    public void reset() {
        if (provider != null) {
            provider.reset();
            provider.setLocationListener(null, -1, -1, -1);
        }
    }
    
    /**
     * Returns the current state of the locator
     */
    public int getState() {
        if (provider != null) {
            return provider.getState();
        } else {
            return -1;
        }
    }
    
    /**
     * Retrieves a Location with the constraints given by the Criteria associated with this class.
     * @return Location object or null if no result could be retrieved.
     */
    public Location getLocation() {
        if (provider != null) {
            try {
                return provider.getLocation(DEFAULT_TIMEOUT);
            } catch (Exception e) {
                // suppose location to be null
            }
        }
        return null;
    }
    
    /**
     * Adds listener for location updates and location provider's state. 
     */
    public void addProviderStateListener(GPSLocatorListener listener) {
        providerStateListeners.addElement(listener);
    }
    
    /**
     * Removes listener for location updates and location provider's state.
     */
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
