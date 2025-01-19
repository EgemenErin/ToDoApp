package net.egemenerin.doit;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import net.egemenerin.doit.R;

/**
 * Splash screen activity that displays for 2 seconds before transitioning to the MainActivity.
 * The action bar is hidden, and a delayed transition is triggered to start the main activity.
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * Called when the activity is created.
     * This method sets the layout for the splash screen and handles the transition to the main activity.
     *
     * @param savedInstanceState The saved instance state (not used in this case).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the splash screen layout
        setContentView(R.layout.activity_splash);

        // Hide the action bar for the splash screen
        getSupportActionBar().hide();

        // Create an intent to transition to MainActivity
        final Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        // Use a Handler to delay the transition for 2 seconds (2000 milliseconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the MainActivity and finish the SplashActivity
                startActivity(intent);
                finish();
            }
        }, 2000); // Delay in milliseconds
    }
}
