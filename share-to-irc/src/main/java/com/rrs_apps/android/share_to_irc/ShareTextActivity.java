package com.rrs_apps.android.share_to_irc;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.rrs_apps.android.share_to_irc.account.IrcAccountHandler;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.parceler.Parcels;

/**
 * ShareTextActivity receives text via an intent and shares it to a selected IRC account.
 */
@EActivity(R.layout.share_text_activity)
public class ShareTextActivity extends FragmentActivity {
    private static final int REQ_CODE_PICK_ACCOUNT = 0;
    private final String TAG = getClass().getName();

    @Extra(Intent.EXTRA_SUBJECT)
    String subject;

    @Extra(Intent.EXTRA_TEXT)
    String text;

    @Override
    protected void onStart() {
        super.onStart();

        if (text == null || text.isEmpty()) {
            Toast.makeText(this, R.string.no_text_to_share, Toast.LENGTH_LONG).show();

            finish();
        } else {
            // Determine destination account
            IrcAccountHandler.launchAccountPicker(this, REQ_CODE_PICK_ACCOUNT, null, false,
                    new String[]{IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC});
        }
    }

    @OnActivityResult(REQ_CODE_PICK_ACCOUNT)
    void sendText(Intent data) {
        if (data != null) {
            String acctName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            String acctType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

            Account acct = new Account(acctName, acctType);

            // Load account details
            String address = AccountManager.get(this).getUserData(acct,
                    IrcAccountHandler.ACCOUNT_KEY_HOST_ADDRESS);
            int port = Integer.parseInt(AccountManager.get(this).getUserData(acct,
                    IrcAccountHandler.ACCOUNT_KEY_HOST_PORT));
            String nick = AccountManager.get(this).getUserData(acct, IrcAccountHandler.ACCOUNT_KEY_NICK);
            boolean useSsl = Boolean.parseBoolean(AccountManager.get(this).getUserData(acct,
                    IrcAccountHandler.ACCOUNT_KEY_IS_SSL));
            String password = AccountManager.get(this).getUserData(acct,
                    IrcAccountHandler.ACCOUNT_KEY_SERVER_PASSWORD);
            String channelList = AccountManager.get(this).getUserData(acct,
                    IrcAccountHandler.ACCOUNT_KEY_CHANNEL_LIST);
            String[] channels = channelList.split(" ");

            // Prepend subject if it was supplied
            if (subject != null && !subject.isEmpty()) {
                text = subject + ": " + text;
            }

            queueMessageWithService(text, address, port, nick, useSsl, password, channels);
        } else {
            finish();
        }
    }

    private void queueMessageWithService(String text, String address, int port, String nick, boolean useSsl, String password, String[] channels) {
        Log.d(TAG, "Queueing text with service");

        Intent sendTextIntent = new Intent(this, ShareTextService_.class);

        IRCMessage message = new IRCMessage(text, address, port, nick, password, channels);
        message.setUsesSsl(true);

        sendTextIntent.putExtra(ShareTextService.EXTRA_IRC_MESSAGE, Parcels.wrap(message));

        startService(sendTextIntent);

        finish();
    }
}
