package com.justingallag.cmueatsforandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvDisplay;

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
        tvDisplay.setText("You refreshed! I don't have any data yet though :(");
    }
}
