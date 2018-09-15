package com.comp30023.spain_itproject.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

/**
 * Activity that calls on services of other components such as I/O which requires the use of other
 * threads.
 * Provides a different OnClickListener to check existing threads and execute a new thread with the
 * execution stream as specified by the Runnable input.
 */
public abstract class ThreadActivity extends AppCompatActivity {

    private ArrayList<Thread> threads;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        threads = new ArrayList<Thread>();
    }

    /**
     * Responds to a click by creating a new thread and executing the Runnable input
     */
    protected class OnClickThreadedListener implements View.OnClickListener {

        private Runnable runnable;

        public OnClickThreadedListener(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        /**
         * When clicked, will create a new thread and execute as specified by the Runnable input.
         * If there is an existing active thread, will not create the new thread
         */
        public void onClick(View v) {

            for (Thread thread : threads) {
                if (thread.isAlive()) return;
            }

            Thread thread = new Thread(runnable);

            threads.add(thread);

            thread.start();
            thread.run();
        }
    }
}
