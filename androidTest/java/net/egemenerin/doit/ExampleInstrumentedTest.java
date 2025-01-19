package net.egemenerin.doit;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test that runs on an Android device.
 * This test checks if the app context is correctly set and the package name matches the expected value.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    /**
     * Tests if the application context returns the correct package name.
     * This ensures the correct app context is being used in the test.
     */
    @Test
    public void useAppContext() {
        // Get the app context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Assert that the package name of the app context matches the expected package name.
        assertEquals("net.egemenerin.doit", appContext.getPackageName());
    }
}
