package com.example.authorisationfirebase;

import android.view.View;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

public class PreferencesTest {

    /**
     * Test for Preferences Activity.
     */

    @Rule
    public ActivityTestRule<Preferences> preferencesActivityTestRule = new ActivityTestRule<>(Preferences.class);
    private Preferences preferences = null;

    @Before
    public void setUp(){
        preferences = preferencesActivityTestRule.getActivity();
    }

    /**
     * Test the textViews and Buttons
     */
    @Test
    public void testLaunch(){
        View view = preferences.findViewById(R.id.restaurants);
        View view1 = preferences.findViewById(R.id.atm);
        View view2 = preferences.findViewById(R.id.bars);
        View view3 = preferences.findViewById(R.id.saveButton);
        View view4 = preferences.findViewById(R.id.button_continue);
        View view5 = preferences.findViewById(R.id.logOut);
        Assert.assertNotNull(view);
        Assert.assertNotNull(view1);
        Assert.assertNotNull(view2);
        Assert.assertNotNull(view3);
        Assert.assertNotNull(view4);
        Assert.assertNotNull(view5);
    }

    @After
    public void tearDown(){
        preferences = null;
    }
}