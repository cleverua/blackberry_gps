package com.cleverua.bb.gps;

import java.util.Vector;

import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

public class GPSLocationListener implements LocationListener {
    public static final int LOCATION_PROVIDER_UPDATE_TIMEOUT    = 5;
    public static final int LOCATION_PROVIDER_UPDATE_INTERVAL   = 5;
    public static final int LOCATION_PROVIDER_UPDATE_MAXAGE     = -1; // default value
    
    private static final String PROVIDER_AVAILABLE_MSG               = "Location provider is available.";
    private static final String LOCATION_EXCEPTOIN_MSG               = "All location providers are currently permanently unavailable!";
    private static final String PROVIDER_TEMPORARILY_UNAVAILABLE_MSG = "Location provider is temporarily unavailable!";
    private static final String PROVIDER_OUT_OF_SERVICE_MSG          = "The location provider is permanently unavailable!";
    private static final String PROVIDER_NULL_MSG                    = "Cat not find the location provider that meets the defined criteria!";
    
    private Location currentLocation;
    private Vector providerStateListeners;
    private LocationProvider provider;

    public GPSLocationListener() {
        providerStateListeners = new Vector();
    }
    
    public Location getCurrentLocation() {
        return currentLocation;
    }

    public static GPSProviderStatus isGPSAvailable() {
        try {
            LocationProvider provider = LocationProvider.getInstance(null);
            if (provider == null) {
                return new GPSProviderStatus(false, PROVIDER_NULL_MSG);
            } 
            if (provider.getState() == LocationProvider.OUT_OF_SERVICE) {
                return new GPSProviderStatus(false, PROVIDER_OUT_OF_SERVICE_MSG);
            }
            if (provider.getState() == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                return new GPSProviderStatus(false, PROVIDER_TEMPORARILY_UNAVAILABLE_MSG);
            }
            return new GPSProviderStatus(true, PROVIDER_AVAILABLE_MSG);
        } catch (LocationException e) {
            return new GPSProviderStatus(false, LOCATION_EXCEPTOIN_MSG + ' ' + e);
        }
    }
    
    public void initLocationListener() throws GPSException {
        try {
            provider = LocationProvider.getInstance(null);
            if (provider != null) {
                provider.setLocationListener(this,
                        LOCATION_PROVIDER_UPDATE_INTERVAL,
                        LOCATION_PROVIDER_UPDATE_TIMEOUT,
                        LOCATION_PROVIDER_UPDATE_MAXAGE);
            } else {
                throw new GPSException(new Throwable(PROVIDER_NULL_MSG));
            }
        } catch (LocationException e) {
            throw new GPSException(e);
        }
    }
    
    public void resetLocationListener() {
        if (provider != null) {
            provider.reset();
            provider.setLocationListener(null, -1, -1, -1);
        }
    }
    
    public void addProviderStateListener(ProviderStateListener listener) {
        providerStateListeners.addElement(listener);
    }
    
    public void removeProviderStateListener(ProviderStateListener listener) {
        providerStateListeners.removeElement(listener);
    }
    
    public void locationUpdated(LocationProvider provider, Location location) {
        GPSProviderStatus status = null;
        if ((provider.getState() == LocationProvider.AVAILABLE) && (location.isValid())) {
            setCurrentLocation(location);
            status = new GPSProviderStatus(true, PROVIDER_AVAILABLE_MSG);
        } else {
            clearCurrentLocation();
            status = new GPSProviderStatus(false, PROVIDER_TEMPORARILY_UNAVAILABLE_MSG);
        }
        updateProviderStateListeners(status);
    }

    public void providerStateChanged(LocationProvider provider, int state) {
        GPSProviderStatus status = null;
        if (provider.getState() == LocationProvider.OUT_OF_SERVICE) {
            status = new GPSProviderStatus(false, PROVIDER_OUT_OF_SERVICE_MSG);
        } else if (provider.getState() == LocationProvider.TEMPORARILY_UNAVAILABLE) {
            status = new GPSProviderStatus(false, PROVIDER_TEMPORARILY_UNAVAILABLE_MSG);
        } else {
            status = new GPSProviderStatus(true, PROVIDER_AVAILABLE_MSG);
        }
        updateProviderStateListeners(status);
    }
    
    private void setCurrentLocation(Location location) {
        currentLocation = location;
    }

    private void clearCurrentLocation() {
        currentLocation = null;
    }
    
    private void updateProviderStateListeners(GPSProviderStatus status) {
        final int size = providerStateListeners.size();
        for (int i = 0;  i < size; i++) {
            ((ProviderStateListener) providerStateListeners.elementAt(i)).providerStatusChanged(status);
        }
    }
}
