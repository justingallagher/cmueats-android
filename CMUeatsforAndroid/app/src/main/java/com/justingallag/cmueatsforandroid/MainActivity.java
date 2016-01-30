package com.justingallag.cmueatsforandroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView tvDisplay;
    ListView lvEateryList;
    ArrayList<Eatery> eateries;

    /**
     * Run when the app is launched for the first time - initialize your global variables here!
     * @param savedInstanceState Data saved during the last run (don't worry about this for now!)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Log a message to the console - think of this like System.out.println in typical Java
        Log.d("MainActivity", "I'm in onCreate!");

        // Get references to our UI elements
        tvDisplay = (TextView) findViewById(R.id.tvDisplay);
        lvEateryList = (ListView) findViewById(R.id.lvEateryList);

        // Set some dummy data to start with
        Calendar oneHourFromNow = Calendar.getInstance();
        oneHourFromNow.add(Calendar.HOUR, 1);

        Calendar twoHoursFromNow = Calendar.getInstance();
        twoHoursFromNow.add(Calendar.HOUR, 2);

        eateries = new ArrayList<Eatery>();
        eateries.add(new Eatery("The Exchange", true, oneHourFromNow));
        eateries.add(new Eatery("Entropy", false, twoHoursFromNow));
        eateries.add(new Eatery("Gallo de Oro", false, oneHourFromNow));

        // Populate list
        EateryListAdapter adapter = new EateryListAdapter(this, eateries);
        lvEateryList.setAdapter(adapter);
    }

    /**
     * Run just before your app becomes active, open to user input and able to execute code. This
     * happens right after onCreate during the first launch, and after the app is brought back into
     * focus from the background.
     *
     * Use this to load saved data, start threads, and open resources you need during execution!
     */
    @Override
    protected void onResume() {
        super.onResume();

        Log.d("MainActivity", "I'm in onResume!");
    }

    /**
     * Run when your app is suspended to the background or killed entirely - like when the user has
     * pressed the home button or turned off the screen.
     *
     * Use this to close any resources you're using so other apps can access them, and save your
     * data so it can be reloaded later.
     */
    @Override
    protected void onPause() {
        Log.d("MainActivity", "I'm in onPause!");

        super.onPause();
    }

    /**
     * Called when the refresh button is clicked. Sets the text display to show the current data.
     * @param view The button pressed.
     */
    public void refreshClick(View view) {
        new DownloadApiDataTask().execute();
    }

    /**
     * Asynchronous task that queries the Scottylabs dining API.
     */
    private class DownloadApiDataTask extends AsyncTask<Void, Void, ArrayList<Eatery>> {

        /**
         * Code to be executed in another thread.
         * @param params Parameters passed by execute()
         * @return An array of the Eatery objects returned by the Dining API.
         */
        @Override
        protected ArrayList<Eatery> doInBackground(Void... params) {
            try {
                // Open connection to the Scottylabs dining-api
                URL diningApi = new URL("http://apis.scottylabs.org/dining/v1/locations");
                BufferedReader in = new BufferedReader(new InputStreamReader(diningApi.openStream()));

                // Read data line by line
                String inputLine;
                String data = "";
                while ((inputLine = in.readLine()) != null) {
                    data += inputLine;
                }

                // Convert data into Java objects
                JSONObject json = new JSONObject(data);
                ArrayList<Eatery> newEateries = Eatery.fromJson(json.getJSONArray("locations"));

                // Return set of eateries
                return newEateries;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Code to be executed when returning to the main thread
         * @param result An array of the Eatery objects returned by the Dining API.
         */
        @Override
        protected void onPostExecute(ArrayList<Eatery> result) {
            if (result != null) {
                // Clear the list adapter and refill with our new objects
                ArrayAdapter adapter = (ArrayAdapter) lvEateryList.getAdapter();
                adapter.clear();
                adapter.addAll(result);
                tvDisplay.setText("Success!");
            } else {
                tvDisplay.setText("Whoops, something went wrong!");
            }
        }
    }
}
