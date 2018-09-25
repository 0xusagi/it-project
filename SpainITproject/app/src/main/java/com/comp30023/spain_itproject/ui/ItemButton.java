package com.comp30023.spain_itproject.ui;

import android.content.Context;

import com.comp30023.spain_itproject.domain.Location;

import java.io.Serializable;

/**
 * Extension of the Button View that stores a item
 */
public abstract class ItemButton<T extends Serializable> extends android.support.v7.widget.AppCompatButton {

    private T item;

    public ItemButton(Context context) {
        super(context);
    }

    /**
     * Returns the lcoation stored in the button
     * @return
     */
    public T getItem() {
        return item;
    }

    /**
     * Sets the button's item and changes the button text to reflect the display name
     * @param item
     */
    public void setItem(T item) {
        this.item = item;
    }
}
