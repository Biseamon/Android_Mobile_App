package com.example.authorisationfirebase;

import android.view.View;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

public class RegisterTest {

    @Rule
    public ActivityTestRule<Register> registerActivityTestRule = new ActivityTestRule<>(Register.class);
    private Register register = null;

    @Before
    public void setUp() throws Exception {

        register = registerActivityTestRule.getActivity();

    }

    @Test
    public void testLaunch(){
        View view = register.findViewById(R.id.fullName);
        View view1 = register.findViewById(R.id.Email);
        View view2 = register.findViewById(R.id.password);
        View view3 = register.findViewById(R.id.phone);
        View view4 = register.findViewById(R.id.registerBtn);
        View view5 = register.findViewById(R.id.createText);
        Assert.assertNotNull(view);
        Assert.assertNotNull(view1);
        Assert.assertNotNull(view2);
        Assert.assertNotNull(view3);
        Assert.assertNotNull(view4);
        Assert.assertNotNull(view5);
    }

    @After
    public void tearDown() throws Exception {

        register = null;

    }
}