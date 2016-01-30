package com.justingallag.cmueatsforandroid;

import java.util.Calendar;

/**
 * Class for eateries in our list.
 */
public class Eatery {
    public String name;
    public boolean isOpen;
    public Calendar nextChange;

    public Eatery(String name, boolean isOpen, Calendar nextChange) {
        this.name = name;
        this.isOpen = isOpen;
        this.nextChange = nextChange;
    }
}
