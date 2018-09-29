package com.comp30023.spain_itproject.ui.dependenthome;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 */
public class CarersListFragment extends ListFragment<CarerUser> {

    public static final String ARGUMENT_USER = "USER";

    public static final String TITLE = "Carers";

    private DependentUser user;

    private static boolean added = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        Bundle arguments = getArguments();
        user = (DependentUser) arguments.getSerializable(ARGUMENT_USER);

        ArrayList<CarerUser> carers = user.getCarers();

        setList(carers);

        setTitle(TITLE);

        return view;
    }
}
