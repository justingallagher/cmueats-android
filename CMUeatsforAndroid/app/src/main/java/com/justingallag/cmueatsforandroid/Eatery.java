package com.justingallag.cmueatsforandroid;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class for eateries in our list.
 */
public class Eatery {
    public String name = null;
    public boolean isOpen = false;
    public Calendar nextChange = null;

    public Eatery(String name, boolean isOpen, Calendar nextChange) {
        this.name = name;
        this.isOpen = isOpen;
        this.nextChange = nextChange;
    }

    public Eatery(JSONObject obj) {
        try {
            this.name = obj.getString("name");
            getTimes(obj.getJSONArray("times"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a day/hour/minute time into an integer for easy comparison.
     * @param day Day of the week, where 0 is Sunday and 6 is Saturday
     * @param hour Hour of the day, in 24 hour time
     * @param minute Minute
     * @return Integer encoding this time for comparison
     */
    private static int apiFmtToInt(int day, int hour, int minute) {
        return day * 10000 + hour * 100 + minute;
    }

    /**
     * Determines a restaurant's open status and next time of opening/closing from a JSON object.
     * @param times The JSON object representing this restaurant's timeslots from the API.
     */
    private void getTimes(JSONArray times) {
        // Get current time
        Calendar now = Calendar.getInstance();
        int currTimeInt =  apiFmtToInt(now.get(Calendar.DAY_OF_WEEK) - 1, now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE));

        // Test the timeslots, trying to find where we are
        for (int i = 0; i < times.length() && !isOpen; i++) {
            try {
                // Get start/end times for this slot
                JSONObject slot = times.getJSONObject(i);
                JSONObject start = slot.getJSONObject("start");
                JSONObject end = slot.getJSONObject("end");

                int startInt = apiFmtToInt(start.getInt("day"), start.getInt("hour"),
                        start.getInt("min"));
                int endInt = apiFmtToInt(end.getInt("day"), end.getInt("hour"),
                        end.getInt("min"));

                if ((startInt < endInt && startInt <= currTimeInt && currTimeInt < endInt) ||
                        (startInt >= endInt && (startInt <= currTimeInt || currTimeInt < endInt))) {
                    // If we're open, set isOpen to true and the closing time to this slot's closing
                    isOpen = true;
                    nextChange = Calendar.getInstance();
                    nextChange.set(Calendar.DAY_OF_WEEK, end.getInt("day") + 1);
                    nextChange.set(Calendar.HOUR_OF_DAY, end.getInt("hour"));
                    nextChange.set(Calendar.MINUTE, end.getInt("min"));
                    break;
                } else if (currTimeInt < startInt) {
                    // If we've passed our time, we must be closed. Set the open time to the next
                    // slot.
                    isOpen = false;
                    nextChange = Calendar.getInstance();
                    nextChange.set(Calendar.DAY_OF_WEEK, start.getInt("day") + 1);
                    nextChange.set(Calendar.HOUR_OF_DAY, start.getInt("hour"));
                    nextChange.set(Calendar.MINUTE, start.getInt("min"));
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // If we still don't have a time, we must not open until next week
        if (nextChange == null) {
            try {
                isOpen = false;
                nextChange = Calendar.getInstance();
                JSONObject start = times.getJSONObject(0).getJSONObject("start");
                nextChange.set(Calendar.DAY_OF_WEEK, start.getInt("day") + 1);
                nextChange.set(Calendar.HOUR_OF_DAY, start.getInt("hour"));
                nextChange.set(Calendar.MINUTE, start.getInt("min"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * Decodes a JSON object from the API into an ArrayList of Java objects
     * @param jsonObjects Array of eateries as a JSON array
     * @return Array of eateries as a Java ArrayList
     */
    public static ArrayList<Eatery> fromJson(JSONArray jsonObjects) {
        ArrayList<Eatery> eateries = new ArrayList<Eatery>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                eateries.add(new Eatery(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        return eateries;
    }
}
