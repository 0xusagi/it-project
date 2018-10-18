package com.comp30023.spain_itproject.ui.dependenthome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.external_services.ServiceFactory;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;
import com.comp30023.spain_itproject.ui.DimBackgroundPopupWindow;

/**
 * The PopupWindow to display when a user presses the help button
 */
public class HelpPopupWindow extends DimBackgroundPopupWindow {

    private DependentUser user;

    private Button closeButton;
    private Button helpOption1Button;

    public HelpPopupWindow(Context context, DependentUser user, ViewGroup root) {
        super(root, createView(context));

        this.user = user;

        setCloseButton();

        helpOption1Button = (Button) getContentView().findViewById(R.id.help_generalHelp);
        helpOption1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendHelpRequest("1");
                dismiss();
            }
        });
    }

    private void setCloseButton() {
        closeButton = (Button) getContentView().findViewById(R.id.help_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setGeneralHelp() {

    }

    private void setRequestLocation() {

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


    private void sendHelpRequest(final String message) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                try {
                    ServiceFactory.getInstance().notificationSendingService().sendHelp(user, message);
                    System.out.println("Notification sent");
                } catch (BadRequestException e) {
                    e.printStackTrace();
                } catch (NoConnectionException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        task.execute();
    }

}
