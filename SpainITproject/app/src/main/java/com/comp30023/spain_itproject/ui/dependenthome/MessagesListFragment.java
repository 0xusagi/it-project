package com.comp30023.spain_itproject.ui.dependenthome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.ui.chat.ChatActivity;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.ui.views.ItemButton;

import java.util.List;

/**
 *
 */
public class MessagesListFragment extends ListFragment<CarerUser> {

    public static final String ARGUMENT_USER = "USER";

    public static final String TITLE = "Messages";

    private DependentUser user;

    private static boolean added = false;

    List<CarerUser> carers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        Bundle arguments = getArguments();
        user = (DependentUser) arguments.getSerializable(ARGUMENT_USER);

        @SuppressLint("StaticFieldLeak")
        NetworkTask task = new NetworkTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                super.doInBackground(objects);

                try {
                    carers = user.getCarers();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                setList(carers);
                setButtonListeners();
                setTitle(TITLE);

            }
        };
        task.execute();

        return view;
    }


    public void setButtonListeners() {
        super.setButtonListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //The item button that is pressed
                ItemButton itemButton = (ItemButton) v;
                CarerUser carer = (CarerUser) itemButton.getItem();

                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(ChatActivity.EXTRA_CHAT_PARTNER_USER_ID, carer.getId());

                startActivity(intent);

            }
        });
    }
}
