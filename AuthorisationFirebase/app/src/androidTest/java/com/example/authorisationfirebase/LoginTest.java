package com.example.authorisationfirebase;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

public class LoginTest {

    @Rule
    public ActivityTestRule<Login> loginActivityTestRule = new ActivityTestRule<>(Login.class);
    private Login login = null;

    @Before
    public void setUp() throws Exception {
        login = loginActivityTestRule.getActivity();
    }

    @Test
    public void launchTest(){
        View view = login.findViewById(R.id.Email);
        View view1 = login.findViewById(R.id.password);
        View view2 = login.findViewById(R.id.progressBar);
        View view3 = login.findViewById(R.id.loginBtn);
        View view4 = login.findViewById(R.id.createText);
        FirebaseAuth view5 = FirebaseAuth.getInstance();
        Assert.assertNotNull(view);
        Assert.assertNotNull(view1);
        Assert.assertNotNull(view2);
        Assert.assertNotNull(view3);
        Assert.assertNotNull(view4);
        Assert.assertNotNull(view5);
    }

    @After
    public void tearDown() throws Exception {
        login = null;
    }
}