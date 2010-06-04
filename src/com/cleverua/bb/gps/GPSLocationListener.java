package com.cleverua.bb.gps;

import javax.microedition.location.Location;

public interface GPSLocationListener {
    public void gpsLocationUpdated(Location newLocation);
}
