package com.example.authorisationfirebase;

import android.view.View;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

public class MapsActivityTest {

    @Rule
    public ActivityTestRule<MapsActivity> mapsActivityActivityTestRule = new ActivityTestRule<>(MapsActivity.class);
    private MapsActivity mapsActivity = null;

    @Before
    public void setUp()  {
        mapsActivity = mapsActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view = mapsActivity.findViewById(R.id.distDur);
        Assert.assertNotNull(view);
        View view1 = mapsActivity.findViewById(R.id.generate);
        Assert.assertNotNull(view1);
        View view2 = mapsActivity.findViewById(R.id.geneticAlg);
        Assert.assertNotNull(view2);
    }

    @After
    public void tearDown() throws Exception {
        mapsActivity = null;
    }
}