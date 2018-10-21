package com.comp30023.spain_itproject.ui.dependenthome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.external_services.ServiceFactory;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;

/**
 * Dialog to display when the dependent presses the help button
 */
public class HelpDialog extends AlertDialog.Builder {

    public static final String CONFIRM_GET_HELP_TITLE = "Get Help";
    public static final String CONFIRM_GET_HELP_MESSAGE = "Send a help request to your carers?";

    public static final String CONFIRM_GET_HELP_POSITIVE = "Yes";
    public static final String CONFIRM_GET_HELP_NEGATIVE = "No";

    public static final String GET_HELP_SUCCESS = "Your request has been sent";
    public static final String GET_HELP_FAIL = "That didn't work, please try again";

    public HelpDialog(final @NonNull Context context, final DependentUser user) {
        super(context);

        setMessage(CONFIRM_GET_HELP_MESSAGE);
        setTitle(CONFIRM_GET_HELP_TITLE);

        setPositiveButton(CONFIRM_GET_HELP_POSITIVE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                @SuppressLint("StaticFieldLeak")
                AsyncTask task = new AsyncTask() {

                    private boolean success;

                    @Override
                    protected Object doInBackground(Object[] objects) {

                        success = false;

                        try {
                            ServiceFactory.getInstance().notificationSendingService().sendHelp(user, null);
                            success = true;
                        } catch (BadRequestException e) {
                            e.printStackTrace();
                        } catch (NoConnectionException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        if (success) {
                            Toast.makeText(context, GET_HELP_SUCCESS, Toast.LENGTH_LONG).show();
                        } else {

                            Toast.makeText(context, GET_HELP_FAIL, Toast.LENGTH_LONG).show();
                            new HelpDialog(context, user).show();
                        }
                    }
                };
                task.execute();
            }
        });

        setNegativeButton(CONFIRM_GET_HELP_NEGATIVE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
}
