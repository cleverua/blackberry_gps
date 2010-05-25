package com.cleverua.bb;

import javax.microedition.location.Location;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.MainScreen;

import com.cleverua.bb.gps.GPSLocator;
import com.cleverua.bb.gps.GPSLocatorListener;

public class GPSDemoScreen extends MainScreen implements GPSLocatorListener {
    private static final String CLOSE_BTN_LABEL = "OK";
    private static final String GPS_STATUS_LABEL = "GPS Status: ";
    private static final String LOCATION_LABEL   = "GPS Coordinates: ";
    
    private EditField locationField;
    private EditField gpsStatus;
    
    public GPSDemoScreen() {
        super();
        setTitle("BlackBerry GPS Demo");
        
        locationField = new EditField(READONLY | NON_FOCUSABLE);
        locationField.setLabel(LOCATION_LABEL);
        add(locationField);
        
        gpsStatus = new EditField(READONLY | NON_FOCUSABLE);
        gpsStatus.setLabel(GPS_STATUS_LABEL);
        add(gpsStatus);
        
        ButtonField closeBtn = new ButtonField(CLOSE_BTN_LABEL, FIELD_HCENTER);
        closeBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field arg0, int arg1) {
                close();
                
            }
        });
        setStatus(closeBtn);
        
        GPSDemoApplication.getLocationListener().addProviderStateListener(this);
    }
    
    public void close() {
        GPSDemoApplication.getLocationListener().removeProviderStateListener(this);
        super.close();
    }
    
    protected boolean onSavePrompt() {
        return true;
    }

    public void stateChanged(final int newState) {
        UiApplication.getUiApplication().invokeLater(new Runnable() {
            public void run() {
                gpsStatus.setText(GPSLocator.getStateMessage(newState));
            }
        });
    }

    public void locationUpdated(Location newLocation) {
        if (newLocation != null) {
            final StringBuffer sb = new StringBuffer();
            sb.append(newLocation.getQualifiedCoordinates().getLatitude()).append(' ');
            sb.append(newLocation.getQualifiedCoordinates().getLongitude());
            UiApplication.getUiApplication().invokeLater(new Runnable() {
                public void run() {
                    locationField.setText(sb.toString());
                }
            });
        } else {
            UiApplication.getUiApplication().invokeLater(new Runnable() {
                public void run() {
                    locationField.setText(null);
                }
            });
        }
    }
}
