package com.justingallag.cmueatsforandroid;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A class used to fill a ListView with Eatery elements.
 */
public class EateryListAdapter extends ArrayAdapter<Eatery> {

    // Default colors for open/closed restaurants
    static private int OPEN_COLOR = Color.parseColor("#00C853");
    static private int CLOSED_COLOR = Color.parseColor("#F44336");

    /**
     * Class constructor.
     * @param context Current state of the application
     * @param eateries List of eateries to fill the list with
     */
    public EateryListAdapter(Context context, ArrayList<Eatery> eateries) {
        super(context, 0, eateries);
    }

    /**
     * Populates a list element with an eatery's information.
     * @param position Position in the array of items of the object to place.
     * @param convertView View to fill with data - instantiated if we're "recycling", otherwise
     *                    null.
     * @param parent Parent for the view.
     * @return The populated view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Eatery eatery = getItem(position);

        // If the view is null, create a new layout object.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.eatery_list_element,
                    parent, false);
        }

        // Get references to the layout fields to fill out
        View openIndicator = convertView.findViewById(R.id.openIndicator);
        TextView tvEateryName = (TextView) convertView.findViewById(R.id.tvEateryName);
        TextView tvEateryStatus = (TextView) convertView.findViewById(R.id.tvEateryStatus);

        // Calculate time of next closing/opening
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE 'at' h:mm a");
        String time = sdf.format(eatery.nextChange.getTime());

        // Set fields
        tvEateryName.setText(eatery.name);

        if (eatery.isOpen) {
            openIndicator.setBackgroundColor(OPEN_COLOR);
            tvEateryStatus.setText("Closes " + time);
        } else {
            openIndicator.setBackgroundColor(CLOSED_COLOR);
            tvEateryStatus.setText("Opens " + time);
        }

        // Return completed view
        return convertView;
    }
}
