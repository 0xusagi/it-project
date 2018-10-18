package com.comp30023.spain_itproject.ui.dependenthome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.comp30023.spain_itproject.ui.NetworkFragment;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DisplayName;
import com.comp30023.spain_itproject.ui.views.ItemButton;

import java.util.List;

/**
 * Fragment that displays a list of items that have a display name
 */

public abstract class ListFragment<T extends DisplayName> extends NetworkFragment {

    /**
     * The maximum number of list buttons that are viewed within the frame
     */
    public static final int BUTTONS_PER_PAGE = 4;

    /**
     * The spacing between the location buttons
     */
    public static final float BUTTON_SPACING_WEIGHT = 0.2f;
    public static final int FIRST_BUTTON_ID = 2000;

    //Reference to signed in user's list
    private List<T> list;

    //The parent view/layout
    private View view;

    private LinearLayout frame;
    private Button previousPageButton;
    private Button nextPageButton;
    private ItemButton<T>[] buttons;
    private TextView title;

    //Index in the list of the item that is at the top of the frame
    private int topIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list, container, false);

        frame = (LinearLayout) view.findViewById(R.id.frame);

        title = (TextView) view.findViewById(R.id.list_title);
        title.setVisibility(View.INVISIBLE);

        buttons = new ItemButton[BUTTONS_PER_PAGE];
        addButtonsToFrame();

        previousPageButton = (Button) view.findViewById(R.id.previousPageButton);
        previousPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtons(-1);
            }
        });

        previousPageButton.setVisibility(View.INVISIBLE);

        nextPageButton = (Button) view.findViewById(R.id.nextPageButton);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtons(1);
            }
        });

        nextPageButton.setVisibility(View.INVISIBLE);

        // Inflate the layout for this fragment
        return view;
    }

    //Evenly distribute and instantiate the buttons about the frame
    private void addButtonsToFrame() {

        boolean first = true;

        for (int i = 0; i < BUTTONS_PER_PAGE; i++) {

            //Include space before next button (if not the first button)
            if (!first) {
                Space space = new Space(view.getContext());
                space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, BUTTON_SPACING_WEIGHT));
                frame.addView(space);
            }

            buttons[i] = createItemButton(FIRST_BUTTON_ID + i);
            first = false;
        }
    }

    //Initialises the buttons within the frame
    private ItemButton<T> createItemButton(int id) {
        //Create the button
        ItemButton<T> button = new ItemButton<T>(view.getContext()) {
            @Override
            public void setItem(T item) {
                super.setItem(item);
                setText(item.getDisplayName());
            }
        };

        //Add the button to the frame and set the layout parameters
        frame.addView(button);
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
        button.setVisibility(View.INVISIBLE);
        button.setId(id);

        return button;
    }

    /**
     * Set the behaviour for when the buttons are clicked
     * @param listener
     */
    public void setButtonListeners(View.OnClickListener listener) {
        for (ItemButton<T> button : buttons) {
            button.setOnClickListener(listener);
        }
    }

    //Changes the buttons viewed on the screen
    //If direction == 0, goes to start of list
    //If direction > 0, gives next page
    //If direction < 0, gives previous page
    private void setButtons(int direction) {

        setNextTopIndex(direction);

        for (int i = topIndex; i < topIndex + BUTTONS_PER_PAGE; i++) {

            //Attach an item to the button
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

    //Sets the next top index depending on the direction
    //If direction > 0, goes forward in list
    //If direction < 0, goes back in list
    //If direction == 0, goes to start of list
    private void setNextTopIndex(int direction) {
        if (topIndex == 0) {
            topIndex = 1;

        } else if (direction > 0 && topIndex + BUTTONS_PER_PAGE <= list.size()) {
            topIndex += BUTTONS_PER_PAGE;

        } else if (direction < 0 && topIndex - BUTTONS_PER_PAGE >= 0) {
            topIndex -= BUTTONS_PER_PAGE;
        }
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

    /**
     * Sets the list and sets the buttons to correspond with this list
     * @param list
     */
    public void setList(List<T> list) {
        this.list = list;

        setButtons(0);
    }

    /**
     * Sets and displays the text above the list
     * @param title
     */
    public void setTitle(String title) {
        this.title.setText(title);
        this.title.setVisibility(View.VISIBLE);
    }
}
