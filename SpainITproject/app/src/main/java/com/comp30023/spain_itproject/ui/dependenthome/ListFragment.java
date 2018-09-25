package com.comp30023.spain_itproject.ui.dependenthome;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DisplayName;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.ui.ItemButton;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Display fragment of the dependent's list of list
 */
public class ListFragment<T extends DisplayName> extends Fragment {

    public static final String ITEM_ARGUMENT = "ITEM";

    public ListFragment() {
    }

    /**
     * The maximum number of list buttons that are viewed within the frame
     */
    public static final int BUTTONS_PER_PAGE = 4;

    /**
     * The spacing between the location buttons
     */
    public static final float BUTTON_SPACING_WEIGHT = 0.2f;

    //Reference to signed in user's list of list
    private ArrayList<T> list;
    private LinearLayout frame;

    private Button previousPageButton;
    private Button nextPageButton;

    private ItemButton<T>[] buttons;

    //Index in the list of the location that is at the top of the frame
    private int topIndex;

    private User user;

    private View view;

    private Class nextClass;
    private String name;

    @SuppressLint("ValidFragment")
    public ListFragment(String name, User user, ArrayList<T> list, Class nextClass) {
        this.name = name;
        this.user = user;
        this.list = list;
        this.nextClass = nextClass;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list, container, false);

        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(name);

        frame = (LinearLayout) view.findViewById(R.id.frame);

        buttons = new ItemButton[BUTTONS_PER_PAGE];
        addButtonsToFrame();

        previousPageButton = (Button) view.findViewById(R.id.previousPageButton);
        previousPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtons(-1);
            }
        });

        nextPageButton = (Button) view.findViewById(R.id.nextPageButton);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtons(1);
            }
        });

        setButtons(0);

        // Inflate the layout for this fragment
        return view;
    }

    //Evenly distribute and instantiate the buttons about the frame
    private void addButtonsToFrame() {

        boolean first = true;

        for (int i = 0; i < BUTTONS_PER_PAGE; i++) {

            if (!first) {
                Space space = new Space(view.getContext());
                space.setLayoutParams(new LinearLayout.LayoutParams(0,1, BUTTON_SPACING_WEIGHT));
                frame.addView(space);
            }

            buttons[i] = new ItemButton<T>(view.getContext()) {
                @Override
                public void setItem(T item) {
                    super.setItem(item);
                    setText(item.getDisplayName());
                }
            };

            buttons[i].setOnClickListener(itemButtonListener);

            frame.addView(buttons[i]);
            buttons[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
            first = false;
        }
    }

    //Changes the buttons viewed on the screen
    //If direction > 0, gives next page
    //If direction < 0, gives previous page
    private void setButtons(int direction) {

        //Set what the topIndex is to be after execution of this method
        if (topIndex == 0) {
            topIndex = 1;

        } else if (direction > 0 && topIndex + BUTTONS_PER_PAGE <= list.size()) {
            topIndex += BUTTONS_PER_PAGE;

        } else if (direction < 0 && topIndex - BUTTONS_PER_PAGE >= 0) {
            topIndex -= BUTTONS_PER_PAGE;
        }

        for (int i = topIndex; i < topIndex + BUTTONS_PER_PAGE; i++) {

            //Attach a location to the button
            if (i - 1 < list.size()) {
                T item = list.get(i - 1);
                buttons[i - topIndex].setItem(item);
                buttons[i - topIndex].setVisibility(View.VISIBLE);

            } else {

                //Remove button from view if no location to correspond to
                buttons[i - topIndex].setVisibility(View.INVISIBLE);
            }
        }

        setNavigationButtons();
    }

    //Displays the navigation buttons if required
    private void setNavigationButtons() {

        //If on first page, hide the previousPageButton from view
        if (topIndex < BUTTONS_PER_PAGE) {
            previousPageButton.setVisibility(View.INVISIBLE);
        } else {
            previousPageButton.setVisibility(View.VISIBLE);
        }

        //If on last page, hide the nextPageButton from view
        if (topIndex + BUTTONS_PER_PAGE > list.size()) {
            nextPageButton.setVisibility(View.INVISIBLE);
        } else {
            nextPageButton.setVisibility(View.VISIBLE);
        }
    }

    //When an item button is pressed, starts the next activity as determined by the class
    //passed in the constructor
    private View.OnClickListener itemButtonListener = new View.OnClickListener() {
        @Override
        /**
         * When clicked, starts the next fragment and passes the location stored within the button
         */
        public void onClick(View v) {

            ItemButton itemButton = (ItemButton) v;
            Serializable item = itemButton.getItem();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            Bundle arguments = new Bundle();
            arguments.putSerializable(ITEM_ARGUMENT, item);

            try {
                Fragment nextFragment = (Fragment) nextClass.newInstance();

                nextFragment.setArguments(arguments);

                transaction.replace(R.id.fragment_container, nextFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        }
    };

}
