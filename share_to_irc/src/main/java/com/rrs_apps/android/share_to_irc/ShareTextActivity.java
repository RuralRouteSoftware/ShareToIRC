package com.rrs_apps.android.share_to_irc;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.OnActivityResult;
import com.rrs_apps.android.share_to_irc.account.IrcAccountHandler;
import com.rrs_apps.java.jirclib.IRCConnection;
import com.rrs_apps.java.jirclib.ssl.SSLIRCConnection;

/**
 * ShareTextActivity receives text via an intent and shares it to a selected IRC account.
 */
@EActivity(R.layout.share_text_activity)
public class ShareTextActivity extends SherlockFragmentActivity {
    private static final int REQ_CODE_PICK_ACCOUNT = 0;

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
        }
        else {
            // Determine destination account
            IrcAccountHandler.launchAccountPicker(this, REQ_CODE_PICK_ACCOUNT, null, false,
                    new String[] { IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC });
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

            sendTextToServer(text, address, port, nick, useSsl, password, channels);
        }
        else {
            finish();
        }
    }

    @Background
    void sendTextToServer(String text, String address, int port, String nick, boolean useSsl,
            String password, final String[] channels) {
        // Create connection
        final IRCConnection conn;
        if (useSsl) {
            conn = new SSLIRCConnection(address, port, port, password, nick, nick, nick);
        }
        else {
            conn = new IRCConnection(address, port, port, password, nick, nick, nick);
        }

        // Add listener that sends the text and finishes the activity upon connecting
        conn.addIRCEventListener(new SendTextAndFinishListener(this, text, channels, conn, getResources()
                .getString(R.string.quit_message)));

        try {
            conn.connect();
        }
        catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(this, R.string.error_connecting, Toast.LENGTH_LONG).show();
        }
    }
}
