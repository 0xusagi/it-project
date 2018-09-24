package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.comp30023.spain_itproject.domain.Location;

/**
 * Extension of the Button View that stores a location
 */
public class LocationButton extends android.support.v7.widget.AppCompatButton {

    private Location location;

    public LocationButton(Context context) {
        super(context);
    }

    /**
     * Returns the lcoation stored in the button
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the button's location and changes the button text to reflect the display name
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
        if (location != null) {
            this.setText(location.getDisplayName());
        } else {
            this.setText("");
        }

    }
}
