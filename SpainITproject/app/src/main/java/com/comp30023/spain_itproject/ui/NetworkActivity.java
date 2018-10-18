package com.comp30023.spain_itproject.ui;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.ui.BroadcastActivity;

/**
 * Base Activity to enable spinning on a network call
 */
public class NetworkActivity extends BroadcastActivity {

    private ProgressBar spinner;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
    }

    /**
     * Extension of AsyncTask that handles displaying the spinner
     * @param <U>
     * @param <V>
     * @param <W>
     */
    public abstract class NetworkTask<U, V, W> extends AsyncTask<U, V, W> {

        @Override
        protected W doInBackground(U... us) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displaySpinner(true);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(W w) {
            super.onPostExecute(w);

            displaySpinner(false);
        }
    }

    /**
     * @param display Boolean value whether to display the spinner
     */
    public void displaySpinner(boolean display) {

        int visibility = display ? View.VISIBLE : View.GONE;
        spinner.setVisibility(visibility);

    }
}
