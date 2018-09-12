package com.comp30023.spain_itproject.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.comp30023.spain_itproject.R;

public class DependentsOnClickDialog extends Dialog {

    public DependentsOnClickDialog(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dependent_on_click_dialog);
    }
}
