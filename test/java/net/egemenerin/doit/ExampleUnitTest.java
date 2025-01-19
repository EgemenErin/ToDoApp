package net.egemenerin.doit;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test that executes on the development machine (host).
 * This simple test checks whether the addition operation is correct.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    /**
     * Tests if the addition of 2 and 2 results in 4.
     * This basic unit test verifies the functionality of the addition operator.
     */
    @Test
    public void addition_isCorrect() {
        // Assert that the sum of 2 and 2 equals 4
        assertEquals(4, 2 + 2);
    }
}
