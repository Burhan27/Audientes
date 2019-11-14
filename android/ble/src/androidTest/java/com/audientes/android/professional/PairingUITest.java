package com.audientes.android.professional;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by morten on 18/07/2017.
 */

@RunWith(AndroidJUnit4.class)
public class PairingUITest
{

    @Rule
    public ActivityTestRule<PairingActivity> mActivityRule =
            new ActivityTestRule<>(PairingActivity.class);

    @Test
    public void ensureTextChangesWork()
    {
        // Type text and then press the button.
        onView(withId(R.id.pairButton))
                .perform(click());

        // Check that the text was changed.
        onView(withId(R.id.pairButton)).check(matches(withText("Pair Now")));
    }

}
