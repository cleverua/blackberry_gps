package com.cleverua.bb.gps;

import javax.microedition.location.Location;


public interface GPSLocatorListener {
    public void locationUpdated(Location newLocation);
    public void stateChanged(int newState);
}
