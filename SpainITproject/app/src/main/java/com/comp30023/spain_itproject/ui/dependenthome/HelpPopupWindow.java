package com.comp30023.spain_itproject.ui.dependenthome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comp30023.spain_itproject.R;

/**
 * The PopupWindow to display when a user presses the help button
 */
public class HelpPopupWindow extends DimBackgroundPopupWindow {

    private Button closeButton;

    public HelpPopupWindow(Context context, ViewGroup root) {
        super(root, createView(context));

        closeButton = (Button) getContentView().findViewById(R.id.help_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    /**
     * Creates an instance of the view
     * @param context The context on which this view will be created
     * @return
     */
    public static View createView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.popup_window_help, null);
    }

}
