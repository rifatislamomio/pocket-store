package com.example.pocketstore_shopownerapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import com.example.phonenumberverification_module.RegisterNumberActivity;
import com.example.phonenumberverification_module.VerificationPageActivity;
import com.example.pocketstore_shopownerapp.getStarted.GetStartedActivity;
import com.example.pocketstore_shopownerapp.home.InsideShopActivity;
import com.example.pocketstore_shopownerapp.signup.setupProfile.SetupProfileActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class UnitTest {

    @Rule
    public ActivityTestRule<GetStartedActivity> getStartedActivityActivityTestRule = new ActivityTestRule<GetStartedActivity>(GetStartedActivity.class);
    private GetStartedActivity getStartedActivity = null;


    @Before
    public void setUp() throws Exception {
        getStartedActivity = getStartedActivityActivityTestRule.getActivity();
    }


    @Test
    public void getStartedActivityTest() throws Exception
    {
        View buttonView = getStartedActivity.findViewById(R.id.btn_getStarted);
        assertNotNull(buttonView);
    }

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(RegisterNumberActivity.class.getName(),null,false);
    @Test
    public void LaunchRegisterNumberActivity()
    {
        Activity registerNumber = getInstrumentation().waitForMonitorWithTimeout(monitor,50000);
        assertNotNull(registerNumber);
        registerNumber.finish();
    }


    Instrumentation.ActivityMonitor monitorVer = getInstrumentation().addMonitor(VerificationPageActivity.class.getName(),null,false);
    @Test
    public void LaunchVerificationCodeActivity()
    {
        Activity verification = getInstrumentation().waitForMonitorWithTimeout(monitorVer,60000);
        assertNotNull(verification);
        verification.finish();
    }

    Instrumentation.ActivityMonitor monitorSetup = getInstrumentation().addMonitor(SetupProfileActivity.class.getName(),null,false);

    @Test
    public void LaunchSetupProfileActivity()
    {
        Activity setupProfile = getInstrumentation().waitForMonitorWithTimeout(monitorSetup,60000);
        assertNotNull(setupProfile);
        Espresso.onView(withId(R.id.shopowner_name)).perform(typeText("Any Valid Name"));
        Espresso.onView(withId(R.id.shop_name)).perform(typeText("Any Valid Shop Name"));
        Espresso.onView(withId(R.id.et_address)).perform(typeText("Needed to be picked from map"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.btn_submit_profile)).perform(click());
    }


    Instrumentation.ActivityMonitor monitorHome = getInstrumentation().addMonitor(InsideShopActivity.class.getName(),null,false);

    @Test
    public void LoadHome()
    {
        Activity homeActivity = getInstrumentation().waitForMonitorWithTimeout(monitorHome,60000);
        assertNotNull(homeActivity);
    }

    @After
    public void tearDown() throws Exception {
        getStartedActivity = null;
    }
}