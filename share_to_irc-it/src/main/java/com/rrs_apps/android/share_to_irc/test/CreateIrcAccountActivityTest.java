package com.rrs_apps.android.share_to_irc.test;

import android.test.ActivityInstrumentationTestCase2;

import com.rrs_apps.android.share_to_irc.account.CreateIrcAccountActivity;
import com.rrs_apps.android.share_to_irc.account.CreateIrcAccountActivity_;

public class CreateIrcAccountActivityTest extends ActivityInstrumentationTestCase2<CreateIrcAccountActivity_> {

    public CreateIrcAccountActivityTest() {
        super(CreateIrcAccountActivity_.class);
    }

    public void testActivity() {
        CreateIrcAccountActivity activity = getActivity();
        assertNotNull(activity);
    }
}
