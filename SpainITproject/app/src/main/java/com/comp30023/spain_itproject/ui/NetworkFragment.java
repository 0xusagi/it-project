package com.comp30023.spain_itproject.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import com.comp30023.spain_itproject.R;

/**
 * Base fragment to enable spinning on a network call
 */
public class NetworkFragment extends Fragment {

    private ProgressBar spinner;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = (ProgressBar) view.findViewById(R.id.progressBar);
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

            getActivity().runOnUiThread(new Runnable() {
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
