package com.cleverua.bb;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.MainScreen;

public class GPSDemoScreen extends MainScreen {
    private static final String CLOSE_BTN_LABEL = "OK";
    private static final String GPS_STATUS_LABEL = "GPS Status";
    private static final String LOCATION_LABEL   = "GPS Coordinates: ";
    
    private EditField locationField;
    private EditField gpsStatus;
    
    public GPSDemoScreen() {
        super();
        setTitle("BlackBerry GPS Demo");
        
        locationField = new EditField(READONLY);
        locationField.setLabel(LOCATION_LABEL);
        add(locationField);
        
        gpsStatus = new EditField(READONLY);
        gpsStatus.setLabel(GPS_STATUS_LABEL);
        add(gpsStatus);
        
        ButtonField closeBtn = new ButtonField(CLOSE_BTN_LABEL, FIELD_HCENTER);
        closeBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field arg0, int arg1) {
                close();
                
            }
        });
        setStatus(closeBtn);
    }
    
    protected boolean onSavePrompt() {
        return true;
    }
}
