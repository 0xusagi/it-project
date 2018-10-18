package com.comp30023.spain_itproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

public class NetworkFragment extends Fragment {

    private ProgressBar spinner;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = (ProgressBar) view.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
    }

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



    public void displaySpinner(boolean display) {

        int visibility = display ? View.VISIBLE : View.GONE;
        spinner.setVisibility(visibility);

    }

}
