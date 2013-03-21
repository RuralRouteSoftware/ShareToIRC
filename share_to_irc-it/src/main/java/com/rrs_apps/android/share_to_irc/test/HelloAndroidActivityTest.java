package com.rrs_apps.android.share_to_irc.test;

import android.test.ActivityInstrumentationTestCase2;

import com.rrs_apps.android.share_to_irc.HelloAndroidActivity;
import com.rrs_apps.android.share_to_irc.HelloAndroidActivity_;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<HelloAndroidActivity_> {

    public HelloAndroidActivityTest() {
        super(HelloAndroidActivity_.class);
    }

    public void testActivity() {
        HelloAndroidActivity activity = getActivity();
        assertNotNull(activity);
    }
}
