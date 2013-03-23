package com.rrs_apps.android.share_to_irc.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * IrcAccountHandlerService returns an AccountHandler's IBinder for managing Share To IRC accounts.
 */
public class IrcAccountHandlerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new IrcAccountHandler(this).getIBinder();
    }
}
