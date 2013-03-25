package com.rrs_apps.android.share_to_irc.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.rrs_apps.android.share_to_irc.R;
import com.rrs_apps.android.share_to_irc.account.IrcAccountListFragment.Listener;

/**
 * ListIrcAccountsActivity displays a list of Share To IRC accounts and allows the user to edit and add accounts.
 */
@EActivity(R.layout.list_irc_accounts_activity)
public class ListIrcAccountsActivity extends SherlockFragmentActivity implements Listener {
    @FragmentById(R.id.irc_account_list_fragment)
    IrcAccountListFragment listFragment;

    @AfterViews
    void onLoad() {
        listFragment.setListener(this);

        // Send user to account creation if there are no accounts
        Account[] accounts = AccountManager.get(this).getAccountsByType(
                IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC);

        if (accounts.length == 0) {
            startActivity(new Intent(this, CreateIrcAccountActivity_.class));
        }
    }

    @Override
    public void accountClicked(Account acct) {
        // TODO Let user edit the account
    }
}
