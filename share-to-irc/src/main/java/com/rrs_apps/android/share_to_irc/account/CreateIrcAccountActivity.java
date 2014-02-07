package com.rrs_apps.android.share_to_irc.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;

import com.rrs_apps.android.auth.AccountAuthenticatorFragmentActivity;
import com.rrs_apps.android.share_to_irc.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;

/**
 * CreateIrcAccountActivity handles the creation of Share To IRC accounts.
 */
@EActivity(R.layout.create_irc_account_activity)
public class CreateIrcAccountActivity extends AccountAuthenticatorFragmentActivity implements
        IrcAccountEditorFragment.Listener {
    @FragmentById(R.id.irc_account_editor_fragment)
    IrcAccountEditorFragment editFragment;

    @AfterViews
    void registerAsListener() {
        editFragment.setListener(this);
    }

    @Override
    public void onSave(IrcAccountEditorFragment editIrcAccountFragment) {
        if (!editFragment.validateData())
            return;

        // Prepare result bundle
        Bundle result = new Bundle();

        String accountName = editFragment.getNick() + "@" + editFragment.getServerName();

        // Set account metadata
        result.putString(IrcAccountHandler.ACCOUNT_KEY_SERVER_NAME, editFragment.getServerName());
        result.putString(IrcAccountHandler.ACCOUNT_KEY_HOST_ADDRESS, editFragment.getHostAddress());
        result.putString(IrcAccountHandler.ACCOUNT_KEY_HOST_PORT, editFragment.getHostPort());
        result.putString(IrcAccountHandler.ACCOUNT_KEY_IS_SSL, editFragment.usesSsl() ? "true" : "false");
        result.putString(IrcAccountHandler.ACCOUNT_KEY_NICK, editFragment.getNick());
        result.putString(IrcAccountHandler.ACCOUNT_KEY_SERVER_PASSWORD, editFragment.getServerPassword());
        result.putString(IrcAccountHandler.ACCOUNT_KEY_CHANNEL_LIST, editFragment.getChannelList());

        // Create new account
        Bundle userdata = new Bundle(result);
        AccountManager.get(this).addAccountExplicitly(
                new Account(accountName, IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC), null, userdata);

        result.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC);

        setAccountAuthenticatorResult(result);
        finish();
    }
}
