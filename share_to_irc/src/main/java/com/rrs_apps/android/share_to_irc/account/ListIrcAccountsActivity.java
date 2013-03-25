package com.rrs_apps.android.share_to_irc.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.ViewById;
import com.rrs_apps.android.share_to_irc.R;
import com.rrs_apps.android.share_to_irc.account.IrcAccountListFragment.Listener;

/**
 * ListIrcAccountsActivity displays a list of Share To IRC accounts and allows the user to edit and add accounts.
 */
@EActivity(R.layout.list_irc_accounts_activity)
public class ListIrcAccountsActivity extends SherlockFragmentActivity implements Listener,
        com.rrs_apps.android.share_to_irc.account.IrcAccountEditorFragment.Listener {
    @FragmentById(R.id.irc_account_list_fragment)
    IrcAccountListFragment listFragment;

    @FragmentById(R.id.irc_account_editor_fragment)
    IrcAccountEditorFragment editFragment;

    @ViewById(R.id.no_account_selected)
    TextView noAccountSelectedText;

    @Override
    protected void onResume() {
        super.onResume();

        // Send user to account creation if there are no accounts
        Account[] accounts = AccountManager.get(this).getAccountsByType(
                IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC);

        if (accounts.length == 0) {
            startActivity(new Intent(this, CreateIrcAccountActivity_.class));
        }
    }

    @AfterViews
    void onLoad() {
        listFragment.setListener(this);

        if (editFragment != null) {
            editFragment.setListener(this);

            // Nothing should be selected at this point; hide the editor
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(editFragment);
            ft.commit();
        }
    }

    @Override
    public void accountClicked(Account acct) {
        // Let user edit the account
        if (editFragment != null && editFragment.isInLayout()) {
            editFragment.loadAccount(acct);

            // Show the editor
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.show(editFragment);
            ft.commit();

            if (noAccountSelectedText != null) {
                noAccountSelectedText.setVisibility(View.GONE);
            }
        }
        else {
            // Launch separate editor activity
            startActivity(new Intent(this, EditIrcAccountActivity_.class).putExtra(
                    IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC, acct));
        }
    }

    @Override
    public void onSave(IrcAccountEditorFragment editFragment) {
        Account acct = listFragment.getSelectedAccount();
        editFragment.saveAccount(acct);

        Toast.makeText(this, R.string.account_updated, Toast.LENGTH_LONG).show();
    }
}
