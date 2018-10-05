package com.comp30023.spain_itproject.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.PopupWindow;

/**
 * An extension of PopupWindow with an interface for dimming the background
 */
public class DimBackgroundPopupWindow extends PopupWindow {

    public static final float DIM_AMOUNT = 0.4f;

    private ViewGroup root;

    private Drawable dim;

    public DimBackgroundPopupWindow(ViewGroup root, View view) {
        super(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.root = root;

        dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, root.getWidth(), root.getHeight());

        setDim(DIM_AMOUNT);
    }

    /**
     * Sets the level of dimming of the background
     * @param dimAmount A float between 0 (lowest) and 1.0
     */
    public void setDim(float dimAmount) {

        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = root.getOverlay();
        overlay.add(dim);
    }

    /**
     * Clears the dim from the background
     */
    public void clearDim() {
        ViewGroupOverlay overlay = root.getOverlay();
        overlay.clear();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        clearDim();
    }
}
