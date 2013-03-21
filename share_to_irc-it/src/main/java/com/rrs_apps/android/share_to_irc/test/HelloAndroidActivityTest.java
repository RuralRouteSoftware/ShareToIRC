package com.rrs_apps.android.share_to_irc.test;

import android.test.ActivityInstrumentationTestCase2;

import com.rrs_apps.android.share_to_irc.HelloAndroidActivity;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<HelloAndroidActivity> {

    public HelloAndroidActivityTest() {
        super(HelloAndroidActivity.class);
    }

    public void testActivity() {
        HelloAndroidActivity activity = getActivity();
        assertNotNull(activity);
    }
}
